import com.alibaba.fastjson.JSONObject;
import com.domain.Params;
import com.inter.Generated;
import com.tools.SSLUtils;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.junit.Test;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import javax.jms.*;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by seed on 2018/5/8.
 */
public class TestCont {

    private final java.util.Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<Runnable>();

    @Test
    public void test1() throws Exception {

        String className="com.contract.Coin";
        Generated generated=new Generated("123");
        String ret="";
        //ret=generated.build_factory(className,"minter",null);
        //ret=generated.build_factory(className,"contAdd",null);
        String contractAdd="0x8411cbbf2b62141a33fbdb08c273199fb8a5189b";
        ret=generated.load_factory(className,contractAdd,"balance",new Object[]{"0x6714eb1c2911bc5f6bf36bdee01979592617aa19"});
        System.out.println("当前的代币余额是："+ret);
        ret=generated.load_factory(className,contractAdd,"mint",new Object[]{"0x6714eb1c2911bc5f6bf36bdee01979592617aa19",new BigInteger("1000")});
        System.out.println("增1000代币的结果是："+ret);
        ret=generated.load_factory(className,contractAdd,"balance",new Object[]{"0x6714eb1c2911bc5f6bf36bdee01979592617aa19"});
        System.out.println("当前的代币余额是："+ret);

    }
    @Test
    public void  test2(){
        String path="C:\\Users\\ls263\\AppData\\Roaming\\Ethereum\\keystore\\UTC--2018-04-03T05-52-26.588509100Z--582f2a8c63844001706b8f400147fe6a521e596f";
        File file=new File(path);
        System.out.println(file.exists());
    }

    //生产者测试
    @Test
    public void testProduceMQ() throws Exception{

        //连接工厂，使用默认的配置
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://47.96.181.25:61616");

        //获取连接
        Connection connection=connectionFactory.createConnection();

        //建立会话
        Session session=connection.createSession(true,Session.AUTO_ACKNOWLEDGE);

        //创建队列
        Queue queue=session.createQueue("q1");

        //创建生产者
        MessageProducer producer=session.createProducer(queue);

        //发送消息
        for(int i=0;i<10;i++){
            producer.send(session.createTextMessage("message "+i));
        }
        //提交
        session.commit();
    }

    //消费者测试
    @Test
    public void testConsumerMQ() throws Exception{
        // 使用默认用户名、密码、路径
        // 路径 tcp://host:61616
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://47.96.181.25:61616");
        // 获取一个连接
        Connection connection = connectionFactory.createConnection();

        // 开启连接
        connection.start();
        // 建立会话
        // 第一个参数，是否使用事务，如果设置true，操作消息队列后，必须使用 session.commit();
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        // 创建队列或者话题对象
        Queue queue = session.createQueue("q1");
        // 创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);

        while (true) {
            TextMessage message = (TextMessage) messageConsumer.receive(1000);
            if (message != null) {
                System.out.println(message.getText());
            } else {
                break;
            }
        }
    }

    /*
    使用MQ的方式：

     */

    @Test
    public void testSSLProducerMQ() throws Exception {

        /*
         * 配置参数 密钥和证书文件的访问目录 密钥密码 SSL链接地址
         */
        String keyStore = "E:/software/SSLStore/client1.ks";
        String trustStore = "E:/software/SSLStore/client1.ts";
        String keyStorePassword = "123456";
        //String url = "ssl://127.0.0.1:61617";
        String url = "ssl://47.96.181.25:61617";

        //创建SSL连接器工厂类
        ActiveMQSslConnectionFactory sslConnectionFactory=
                new ActiveMQSslConnectionFactory();
        //设置参数，并加载SSL密钥和证书信息
        sslConnectionFactory.setBrokerURL(url);//访问链接
        sslConnectionFactory.setKeyAndTrustManagers(SSLUtils.loadKeyManager(keyStore,keyStorePassword),
                SSLUtils.loadTrustManager(trustStore),new SecureRandom());

        //连接ACTIVEMQ
        Connection connection=sslConnectionFactory.createConnection();
        connection.start();
        Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Destination destination=session.createQueue("4ca11a510c39676b967030bb368d611f");

        //创建生产者，发送消息
        MessageProducer producer=session.createProducer(destination);

        /*for(int i=0;i<10;i++){
            producer.send(session.createTextMessage("sslMessage "+i));
        }*/
        //发送一条命令
        String command="build_factory";
        Object[] params=new Object[]{"com.contract.Coin","minter","123",new Object[]{null}};

        Params p=new Params();
        p.setCommand(command);
        p.setParams(params);
        p.setCurr_time(new Date());
        p.setTrans_id("112344");
        p.setType(0);
        p.setVersion("1.0");
        String content= JSONObject.toJSONString(p);
        System.out.println(content);

        producer.send(session.createTextMessage(content));

        session.close();
        connection.close();

    }

    @Test
    public void testSSLConsumer() throws Exception {

        String keyStore = "E:/software/SSLStore/client1.ks";
        String trustStore = "E:/software/SSLStore/client1.ts";
        String keyStorePassword = "123456";
        String url = "ssl://127.0.0.1:61617";

        // 创建SSL连接器工厂类
        ActiveMQSslConnectionFactory sslConnectionFactory = new ActiveMQSslConnectionFactory();
        // 设置参数，并加载SSL密钥和证书信息
        sslConnectionFactory.setBrokerURL(url);
        sslConnectionFactory.setKeyAndTrustManagers(SSLUtils.loadKeyManager(keyStore, keyStorePassword), SSLUtils.loadTrustManager(trustStore),
                new java.security.SecureRandom());

        // 连接ActiveMQ
        Connection conn = sslConnectionFactory.createConnection();
        conn.start();
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = session.createQueue("ssl1");

        /*MessageListenerAdapter mla=new MessageListenerAdapter();
        MessageDelegate md=new MessageDelegate();
        mla.setDelegate(md);
        mla.setDefaultListenerMethod("receiveMessage");*/

        // 设置消息消费者，在匿名内部类中打印消息内容(只接受手实时消息)
        MessageConsumer mc = session.createConsumer(dest);
        /*mc.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                if (msg instanceof TextMessage) {
                    try {
                        TextMessage tmsg = (TextMessage) msg;
                        System.out.println(tmsg.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(msg.toString());
                }
            }
        });*/

        //接受消息
        while (true) {
            TextMessage message = (TextMessage) mc.receive(1000);//阻塞1S
            if (message != null) {
                System.out.println(message.getText());
            } else {
                System.out.println("未收到消息");
                break;
            }
        }

    }
    
    

}
