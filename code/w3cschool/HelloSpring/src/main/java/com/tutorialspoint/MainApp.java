package com.tutorialspoint;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class MainApp {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		HelloWorld obj = (HelloWorld) context.getBean("helloWorld"); 
		
		ApplicationContext context0 = new FileSystemXmlApplicationContext("E:/project/HelloSpring/src/main/java/Beans.xml");
		HelloWorld obj0 = (HelloWorld) context.getBean("helloWorld");
		
		XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("Beans.xml"));
		HelloWorld obj1 = (HelloWorld) factory.getBean("helloWorld");
		
        obj.getMessage();
        
        obj0.getMessage();
        
        obj1.getMessage();
	}

}
