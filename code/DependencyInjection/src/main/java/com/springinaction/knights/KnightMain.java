package com.springinaction.knights;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class KnightMain {

	public static void main(String[] args) throws Exception{
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/knights.xml");
	    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(com.springinaction.knights.KnightConfig.class); 
		Knight knight = context.getBean(Knight.class);
		knight.embarkOnQuest();
		 context.close();

	}

}
