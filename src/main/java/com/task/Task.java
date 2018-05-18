package com.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.main.Start;
import com.main.WorkerImpl;

@Component
public class Task {
	
	private static final Logger log = LoggerFactory.getLogger(Task.class);
	
	//每秒执行一次
	//@Scheduled(cron="0/1 * * * * ?")
	public void test1()throws Exception{
		System.out.println("send balance");
	}
	//@Scheduled(cron="5 0/1 * * * ?") 表示下分钟的第5秒的时候执行 再相隔一分钟的第5秒再执行
	//@Scheduled(cron="0/2 * 14-15 * * ?") 在14-15点时，每隔2秒执行一次
	
	@Scheduled(cron="0 0/1 * * * ?")
	public void test2()throws Exception{
		log.info("prepare send balance");
		new WorkerImpl().sendBalance();
	}
	

}
