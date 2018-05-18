package com.main;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

//@Service
public class SpringStart  implements InitializingBean{
	
	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("loading");
		System.out.println(jmsTemplate.toString());
	}

}
