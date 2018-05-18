package com.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.inter.Generated;
import com.tools.CommUtil;
import com.tools.HttpUtils;
import com.tools.SSLUtils;

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
	private ActiveMQSslConnectionFactory sslConnectionFactory ;
	
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
    MessageConsumer consumer;		//从这拿数据
    MessageProducer producer;		//向这发数据

    public AbstractMQImpl(){
    	 try {
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
       
            sslConnectionFactory= new ActiveMQSslConnectionFactory();
            sslConnectionFactory.setBrokerURL(url);
            sslConnectionFactory.setKeyAndTrustManagers(SSLUtils.loadKeyManager(keyStore, keyStorePassword), SSLUtils.loadTrustManager(trustStore),
                    new java.security.SecureRandom());
            conn = sslConnectionFactory.createConnection();
            conn.start();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            dest = session.createQueue(CommUtil.md5(mac.trim()+"-server"));
            src = session.createQueue(CommUtil.md5("-client"));
            consumer = session.createConsumer(dest);
            producer=session.createProducer(src);
            log.info("MQ instance success");
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    

    public  void start() {
    	
    	String mac1=CommUtil.getMACAddress();
    	if(!mac1.equals(mac)){
    		config.setProperty("mac", mac1);
    		mac=mac1;
    	}
    	listen.execute(new Listen());
    	response.execute(new Response());
    	connectServer(mac);
    }
    
    public void connectServer(String mac){
    	//连接服务器
    	Map<String,Object> params=new HashMap<>();
    	params.put("mac", mac);
    	params.put("version", "1.0");
    	HttpUtils.sendPost("http://localhost:8080/api/geth/init", params);
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
	             try {
	                Message message = (TextMessage) consumer.receive();//阻塞
                	 String text=((TextMessage)message).getText();
                	 log.info("get message:"+text);
                     reqQueue.put(text);//将任务放入队列中
	                 
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
			Thread.currentThread().setName("Response-Thread");
			while(true){
				try {
					String message=respQueue.take();
					if(!CommUtil.isEmpty(message)){
						producer.send(session.createTextMessage(message));
						 log.info("send message :"+message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
    }
}
