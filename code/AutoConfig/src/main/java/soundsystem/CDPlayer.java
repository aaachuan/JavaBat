package soundsystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CDPlayer implements MediaPlayer {
  private  AnotherLonelyNight cd;
  
  @Autowired
  public CDPlayer(AnotherLonelyNight cd) {
    this.cd = cd;
  }

  public void play() {
    cd.play();
  }

}
