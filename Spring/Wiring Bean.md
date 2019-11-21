### Wiring Bean

#### Spring配置方案

创建应用对象之间协作关系的行为称为装配（wiring），这也是DI的本质。

Spring提供了三种主要的装配机制：
* 隐式的bean发现机制和自动装配
* 在Java中进行显示配置
* 在XML中进行显示配置

建议尽可能使用自动配置的机制，显式配置越少越好。但是，当源码不是由自己维护时，而自己需要为这些代码配置bean的时候，还是需要显示配置bean的，
这时居其次使用类型安全并比XML更强大的JavaConfig。

#### 自动化装配Bean

Spring从两个角度来实现自动化装配：
- 组件扫描（component scanning）：Spring会自动发现应用上下文中所创建的bean。
- 自动装配（autowiring）：Spring自动满足bean之间的依赖。

`CompactDisc`接口：
```
package soundsystem;

public interface CompactDisc {
  void play();
}
```

`CompactDisc`的一个实现类`SgtPeppers`，并带`@Component`注解：
```
package soundsystem;
import org.springframework.stereotype.Component;

@Component
public class SgtPeppers implements CompactDisc {

  private String title = "Sgt. Pepper's Lonely Hearts Club Band";  
  private String artist = "The Beatles";
  
  public void play() {
    System.out.println("Playing " + title + " by " + artist);
  }
  
}
```
`@Component`注解表明该类会作为组件类，并告知Spring要为这个类创建bean。

但是，组件扫描默认是不启用的，还需要显示配置一下Spring，从而命令它寻找带有`@Component`注解的类，并为其创建bean：
```
package soundsystem;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class CDPlayerConfig { 
}
```
这里并没有显示地声明任何bean。没有其他配置的话，`@ComponentScan`默认扫描与配置类相同的包。

当然，也可以用XML配置实现启动组件扫描：
`<context:component-scan base-package="soundsystem" />`

现在就可以创建一个简单的JUnit测试，判断`CompactDisc`是否真的创建出来：
```
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=CDPlayerConfig.class)
public class CDPlayerTest {
	
	@Autowired
	private CompactDisc cd;
	
	@Test
	public void cdShouldNotBeNull() {
		assertNotNull(cd);
	}
	
}
```
`SpringJUnit4ClassRunner`便于在测试开始的时候自动创建Spring的应用上下文，注解`@ContextConfiguration`会告诉他需要在`CDPlayerConfig`加载配置。

当然，可以在`CDPlayerMain`实际运行输出：
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
Output:
```
十一月 21, 2019 7:13:22 下午 org.springframework.context.support.AbstractApplicationContext prepareRefresh
信息: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@4437c4: startup date [Thu Nov 21 19:13:22 CST 2019]; root of context hierarchy
十一月 21, 2019 7:13:23 下午 org.springframework.beans.factory.support.DefaultListableBeanFactory preInstantiateSingletons
信息: Pre-instantiating singletons in org.springframework.beans.factory.support.DefaultListableBeanFactory@1417077: defining beans [org.springframework.context.annotation.internalConfigurationAnnotationProcessor,org.springframework.context.annotation.internalAutowiredAnnotationProcessor,org.springframework.context.annotation.internalRequiredAnnotationProcessor,org.springframework.context.annotation.internalCommonAnnotationProcessor,CDPlayerConfig,org.springframework.context.annotation.ConfigurationClassPostProcessor.importAwareProcessor,anotherLonelyNight,CDPlayer,sgtPeppers]; root of factory hierarchy
Playing Another lonely night by Adam Lambert
```
这里是另一个`CompactDisc`的实现类`AnotherLonelyNight`：
```
package soundsystem;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Component
@Primary
public class AnotherLonelyNight implements CompactDisc {

	private String title = "Another lonely night";  
	
	private String artist = "Adam Lambert";
	  
	public void play() {
	    System.out.println("Playing " + title + " by " + artist);
	  }

}

```
因为`AnotherLonelyNight`和`SgtPeppers`同时实现了`CompactDisc`接口，如果没有进行处理的话会抛出`NoUniqueBeanDefinitionException`异常：
```
Exception in thread "main" org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type [soundsystem.CompactDisc] is defined: expected single matching bean but found 2: anotherLonelyNight,sgtPeppers
```
一种解决方案是上面的`@Primary`注解，将这个bean设置为首选。












