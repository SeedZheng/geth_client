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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by seed on 2018/5/9.
 */
public class AbstractMQImpl extends  Start  {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractMQImpl.class);
	//BOSS线程
	static Executor listen= Executors.newSingleThreadExecutor();
	static Executor response= Executors.newSingleThreadExecutor();
	private static int process_num=Runtime.getRuntime().availableProcessors();
	//worker线程
	Executor worker=Executors.newFixedThreadPool(process_num);
	
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
        
        //启动多个worker线程，阻塞在读取
        for(int i=0;i<process_num;i++){
        	worker.execute(new WorkerImpl());
        }

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
    

    public  void start() {
    	listen.execute(new Listen());
    	response.execute(new Response());
    }
    
    /**
     * 轮询MQ
     * @author Seed
     * 2018年5月10日 下午3:48:55
     */
    class Listen implements Runnable{

		@Override
		public void run() {
			Thread.currentThread().setName("Listen-Thread");
			while(true){
	             TextMessage message;
	             try {
	                 message = (TextMessage) consumer.receive(50);//阻塞0.05S
	                 if (message != null) {
	                	 String text=message.getText();
	                	 log.info("get message:"+text);
	                     taskQueue.put(text);//将任务放入队列中
	                 }
	             } catch (JMSException e) {
	                 e.printStackTrace();
	             } catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	}
		}
    }
    /**
     * 添加回复消息到MQ
     * @author Seed
     * 2018年5月10日 下午3:48:44
     */
    class Response implements Runnable{
    	
		@Override
		public void run() {
			Thread.currentThread().setName("Response-Thread-1");
			while(true){
				try {
					String message=responseQueue.take();
					if(!CommUtil.isEmpty(message)){
						producer.send(session.createTextMessage(message));
						 log.info("send message :"+message);
					}
				} catch (JMSException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
    }
}
