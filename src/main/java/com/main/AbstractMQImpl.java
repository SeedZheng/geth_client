package com.main;

import com.inter.Generated;
import com.tools.CommUtil;
import com.tools.SSLUtils;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * Created by seed on 2018/5/9.
 */
public class AbstractMQImpl extends  Start implements Runnable {
	
	 private static final Logger log = LoggerFactory.getLogger(AbstractMQImpl.class);

    // 创建SSL连接器工厂类
    ActiveMQSslConnectionFactory sslConnectionFactory ;
    // 连接ActiveMQ
    Connection conn ;
    Properties config;
    private String keyStore ;
    private String trustStore ;
    private String keyStorePassword ;
    private String url ;
    private String mac;
    Session session;
    Destination dest;
    Destination src;
    MessageConsumer consumer;
    MessageProducer producer;

    public AbstractMQImpl()  {
    	new Generated();
        config= Generated.config;
        keyStore=config.getProperty("keyStore");
        trustStore=config.getProperty("trustStore");
        keyStorePassword=config.getProperty("keyStorePassword");
        url=config.getProperty("url");
        mac=config.getProperty("mac");

        try {
            sslConnectionFactory= new ActiveMQSslConnectionFactory();
            sslConnectionFactory.setBrokerURL(url);
            sslConnectionFactory.setKeyAndTrustManagers(SSLUtils.loadKeyManager(keyStore, keyStorePassword), SSLUtils.loadTrustManager(trustStore),
                    new java.security.SecureRandom());
            conn = sslConnectionFactory.createConnection();
            conn.start();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            dest = session.createQueue(CommUtil.md5(mac+"-server"));
            src = session.createQueue(CommUtil.md5(mac+"-client"));
            consumer = session.createConsumer(dest);
            producer=session.createProducer(src);
            log.info("MQ instance success");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    
    
    private void listenMQ(){
    	while(true){
             TextMessage message;
             try {
                 message = (TextMessage) consumer.receive(1000);//阻塞1S
                 if (message != null) {
                	 log.info("get message:"+message.getText());
                     taskQueue.add(message.getText());//将任务放入队列中
                     worker.execute(new WorkerImpl());//启动一个worker线程
                 }else{
                	 break;
                 }
             } catch (JMSException e) {
                 e.printStackTrace();
             }
    	}
    }
    
    private void addMQ(){
    	String message=responseQueue.poll();
		try {
			if(!CommUtil.isEmpty(message)){
				producer.send(session.createTextMessage(message));
				 log.info("send message :"+message);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }
    

    @Override
    public void run() {
    	while(true){
    		listenMQ();
        	addMQ();
    	}
    }
}
