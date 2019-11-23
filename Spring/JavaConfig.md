### Wiring Bean

#### Spring配置方案

创建应用对象之间协作关系的行为称为装配（wiring），这也是DI的本质。

Spring提供了三种主要的装配机制：
* 隐式的bean发现机制和自动装配
* 在Java中进行显示配置
* 在XML中进行显示配置

建议尽可能使用自动配置的机制，显式配置越少越好。但是，当源码不是由自己维护时，而自己需要为这些代码配置bean的时候，还是需要显示配置bean的，
这时居其次使用类型安全并比XML更强大的JavaConfig。

#### JavaConfig装配Bean

当你想要将第三方库的组件装配到自己的应用，在这种情况下，是没有办法在它的类上添加`@Component`和`@Autowired`注解的，因此自动化装配的方案失效，这时候就需要采用显示装配。

JavaConfig代码和别的Java代码是有差别的，通常将它放在单独的包，使它和其他的应用程序逻辑分离，因为JavaConfig单纯只是配置代码。

这次对之前的`CDPlayerConfig`稍做修改，将`@ComponentScan`移除掉：
```
package soundsystem;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CDPlayerConfig { 

}

```
这时`CDPlayerConfig`就没有什么作用了，再运行`CDPlayerTest`会报错`org.springframework.beans.factory.BeanCreationException`。
```
package soundsystem;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=CDPlayerConfig.class)
public class CDPlayerTest {

  @Rule
  public final StandardOutputStreamLog log = new StandardOutputStreamLog();

  @Autowired
  private MediaPlayer player;
  
  @Autowired
  private CompactDisc cd;
  
  @Test
  public void cdShouldNotBeNull(){
	  assertNotNull(cd);
  }

  @Test
  public void play() {
    player.play();
    assertEquals(
        "Playing Sgt. Pepper's Lonely Hearts Club Band by The Beatles", 
        log.getLog());
  }

}
```
测试时期望被注入的那些bean根本没有创建，因为组件扫描不会发现它们。

有一种策略可以避免异常的出现，就是将`@Autowired`的`required`属性设置为false：
```
@Autowired(required=false)
```
这时候Console不会展示报错信息，但是测试仍是失败，将`@Autowired`的`required`属性设置为false，Spring会尝试执行自动装配，但是如果没有匹配的bean,Spring会将它处于未装配状态。但是，因为代码没有null检查，所以测试仍会报`java.lang.NullPointerException`。

接下来，在JavaConfig中声明Bean（划重点）：
```
package soundsystem;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CDPlayerConfig { 
//	
//	@Bean
//	public CompactDisc sgtPeppers() {
//		return new SgtPeppers();
//	}
	
	@Bean
	public CompactDisc randomCD() {
		int choice = (int) Math.floor(Math.random()*3);
		if (choice == 0) {
			return new SgtPeppers();
		}else if (choice == 1){
			return new AnotherLonelyNight();
		}else {
			return new Blue();
		}
	}
		
	@Bean
	public CDPlayer cdPlayer() {
		return new CDPlayer(new SgtPeppers());
	}
}
```
当中需要再创建`CompactDisc`的两个实现类`AnotherLonelyNight`和`Blue`，这时候运行`CDPlayerMain`：
```
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

```
可以实现随机播放CompactDisc。

......
