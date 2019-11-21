package soundsystem;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CDPlayerMain {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(soundsystem.CDPlayerConfig.class);
	    
	    CompactDisc cd = context.getBean(CompactDisc.class);
        cd.play();
 
	}

}
