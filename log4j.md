#### Log4j

- TestLog4j.java
```
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

```
- log4j.properties

```
log4j.rootLogger=debug, stdout, R
 
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
 
# Pattern to output the caller's file name and line number.
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] (%F:%L) - %m%n
 
log4j.appender.R=org.apache.log4j.RollingFileAppender
log4j.appender.R.File=example.log
 
log4j.appender.R.MaxFileSize=100KB
# Keep one backup file
log4j.appender.R.MaxBackupIndex=5
 
log4j.appender.R.layout=org.apache.log4j.PatternLayout
log4j.appender.R.layout.ConversionPattern=%p %t %c - %m%n
```
