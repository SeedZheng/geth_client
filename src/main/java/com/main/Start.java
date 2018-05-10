package com.main;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by seed on 2018/5/9.
 */
public abstract class Start {
    /*
    1.初始化两个个线程池
           处理请求、回复线程：数量1
          处理逻辑：数量若干
    2.初始任务队列
     */
    static BlockingQueue<String> taskQueue=new LinkedBlockingQueue<>();
    static BlockingQueue<String> responseQueue=new LinkedBlockingQueue<>();
    //static final long MAX_WAIT_TIME=Integer.MAX_VALUE;

    public static void main(String[] args) {
        //boss.execute(new AbstractMQImpl());
    	new AbstractMQImpl().start();
    }
    


}
