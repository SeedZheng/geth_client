package com.main;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    //BOSS线程
    static Executor boss= Executors.newSingleThreadExecutor();
    //worker线程
    Executor worker=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);

    static Queue<String> taskQueue=new ConcurrentLinkedQueue<>();
    static Queue<String> responseQueue=new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        boss.execute(new AbstractMQImpl());
    }
    


}
