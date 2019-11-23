package soundsystem;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CDPlayerConfig { 
	
	@Bean(name="lonelyHeartsClubBand")
	public CompactDisc sgtPeppers() {
		return new SgtPeppers();
	}
	
//	@Bean
//	public CompactDisc randomCD() {
//		int choice = (int) Math.floor(Math.random()*3);
//		if (choice == 0) {
//			return new SgtPeppers();
//		}else if (choice == 1){
//			return new AnotherLonelyNight();
//		}else {
//			return new Blue();
//		}
//	}
		
	@Bean
	public CDPlayer cdPlayer() {
		return new CDPlayer(sgtPeppers());
	}
}
