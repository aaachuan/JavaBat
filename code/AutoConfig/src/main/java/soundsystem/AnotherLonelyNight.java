package soundsystem;

import org.springframework.stereotype.Component;


@Component
public class AnotherLonelyNight implements CompactDisc {

	private String title = "Another lonely night";  
	
	private String artist = "Adam Lambert";
	  
	public void play() {
	    System.out.println("Playing " + title + " by " + artist);
	  }

}
