package log4j2se;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class TestLog4j {

	static Logger logger = Logger.getLogger(TestLog4j.class);
	public static void main(String[] args) throws InterruptedException{
//		/*
//		System.out.println("跟踪信息");
//		System.out.println("调试信息");
//		System.out.println("输出信息");
//		System.out.println("警告信息");
//		System.out.println("错误信息");
//		System.out.println("致命信息 ");*/
		
//		BasicConfigurator.configure();
//		logger.setLevel(Level.DEBUG);
//		logger.trace("跟踪信息");
//		logger.debug("调试信息");
//		logger.info("输出信息");
//		Thread.sleep(1000);
//		logger.warn("警告信息");
//		logger.error("错误信息");
//		logger.fatal("致命信息");
	PropertyConfigurator.configure("E:\\project\\log4j2se\\src\\log4j.properties");
	for (int i = 0; i < 5000; i++) {
		logger.trace("跟踪信息");
		logger.debug("调试信息");
		logger.info("输出信息");
//		Thread.sleep(1000);
		logger.warn("警告信息");
		logger.error("错误信息");
		logger.fatal("致命信息");
	}
		

	}

}
