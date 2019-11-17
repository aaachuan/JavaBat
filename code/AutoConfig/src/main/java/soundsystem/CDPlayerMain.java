package soundsystem;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CDPlayerMain {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(soundsystem.CDPlayerConfig.class);
	    
	    CompactDisc cd = context.getBean(SgtPeppers.class);
        cd.play();
        
        CompactDisc cd1 = context.getBean(AnotherLonelyNight.class);
        cd1.play();    
	}

}
