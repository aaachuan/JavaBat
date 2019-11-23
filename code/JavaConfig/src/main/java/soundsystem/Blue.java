package soundsystem;

import org.springframework.stereotype.Component;

@Component
public class Blue implements CompactDisc {

		private String title = "Blue";  
		
		private String artist = "BigBang";
		  
		public void play() {
		    System.out.print("Playing " + title + " by " + artist);
		  }

}
