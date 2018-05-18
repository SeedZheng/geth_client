package com.main;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Created by seed on 2018/5/9.
 */
@Service
public  class Start{
    /*
    1.初始化两个个线程池
           处理请求、回复线程：数量1
          处理逻辑：数量若干
    2.初始任务队列
     */
    static BlockingQueue<String> reqQueue=new LinkedBlockingQueue<>();
    static BlockingQueue<String> respQueue=new LinkedBlockingQueue<>();
    //static final long MAX_WAIT_TIME=Integer.MAX_VALUE;

    public static void main(String[] args) {
    	//先尝试连接服务器
        //boss.execute(new AbstractMQImpl());
    	new AbstractMQImpl().start();
    	new ClassPathXmlApplicationContext("spring.xml");
    }


}
