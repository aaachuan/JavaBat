### Dependency Injection

#### DI功能的实现 

传统上，多个类相互间协作完成特定的业务逻辑时，每个对象会负责管理与自己相互协作的对象（所依赖的对象）的引用，将会导致高度耦合和难以测试。

```
package com.springinaction.knights;

public class BraveKnight implements Knight{

	private RescueDamselQuest quest;
	
	public BraveKnight() {
		this.quest = new RescueDamselQuest;
	}
	

	public void embarkOnQuest() {
		quest.embark();
		
	}
```
`BraveKnight`在它的构造函数中自行创建了`RescueDamselQuest`，这使得`BraveKnight`和`RescueDamselQuest`紧耦合，而`BraveKnight`也难以测试。

通过依赖注入，对象的依赖关系将由系统中负责协调各对象的第三方组件在创建的时候进行设定。对象无需自行创建或者管理它们的依赖关系，依赖关系将自动注入到需要它们的对象当中。

```
package com.springinaction.knights;

public class BraveKnight implements Knight{

	private Quest quest;
	
	public BraveKnight(Quest quest) {
		this.quest = quest;
	}
	

	public void embarkOnQuest() {
		quest.embark();
		
	}
     
}
```
这里，`BraveKnight`不需要自行创建`Quest`，而是在构造的时候将其作为构造器参数传入，这是依赖注入的方式之一，构造器注入（constructor injection）。
还有，`BraveKnight`没有与任何特定的`Quest`实现发生耦合。只要传入的参数类实现了`Quest`接口，那么具体实现类就无关紧要。如果一个对象只通过接口，而不是具体实现或初始化过程来表明依赖关系，那么这种依赖就能够在对象本身毫不知情的情况下，用不同的具体实现进行替换。

对依赖进行替换的一个最常用的方法就是在测试的时候使用mock实现。
```
package com.springinaction.knights;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class BraveKnightTest {
	
	@Test
	public void knightShouldEmbarkOnQuest() {
		Quest mockQuest = mock(Quest.class);
		BraveKnight knight = new BraveKnight(mockQuest);
		knight.embarkOnQuest();
		verify(mockQuest, times(1)).embark();
	}

}
```
上面使用`mock`框架`Mockito`创建一个`Quest`接口的`mock`实现，通过这个`mock`对象，就可以创建一个新的`BraveKnight`实例，并通过构造器注入这个`mock Quest`。当调用`embarkOnQuest()`方法时，可以验证`Quest`的`mock`实现的`embark()`方法仅仅被调用了一次。

#### 注入与装配

`BraveKnight`类现在可以接受`Quest`的任一实现，比如`SlayDragonQuest`：
```
package com.springinaction.knights;

import java.io.PrintStream;

public class SlayDragonQuest implements Quest{

	private PrintStream stream;
	
	public  SlayDragonQuest(PrintStream stream) {
		this.stream = stream;
	}
	
	public void embark() {
		stream.println("Embarking on quest to slay the dragon!");
	}
}
```

接下来，只有Spring通过它的配置，能够了解这些部分是如何装配起来的。创建应用组件之间的协作行为成为装配（wiring）。

> 基于XML配置

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:aop="http://www.springframework.org/schema/aop"
  xsi:schemaLocation="http://www.springframework.org/schema/aop 
      http://www.springframework.org/schema/aop/spring-aop.xsd
		http://www.springframework.org/schema/beans 
      http://www.springframework.org/schema/beans/spring-beans.xsd">
      
  <bean id="knight" class="com.springinaction.knights.BraveKnight">
    <constructor-arg ref="quest" />
  </bean>

  <bean id="quest" class="com.springinaction.knights.SlayDragonQuest">
    <constructor-arg value="#{T(System).out}" />
  </bean>
</beans>
```

Spring通过应用上下文（Application Context）装载bean的定义并把它们组装起来。Spring应用上下文全权负责对象的创建和组装。Spring自带了多种应用上下文的实现。它们之间主要的区别在于如何加载配置。

```
package com.springinaction.knights;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class KnightMain {

	public static void main(String[] args) throws Exception{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/knights.xml");
		Knight knight = context.getBean(Knight.class);
		knight.embarkOnQuest();
		 context.close();

	}

}
```
Output:
```
十一月 20, 2019 8:21:40 下午 org.springframework.context.support.AbstractApplicationContext prepareRefresh
信息: Refreshing org.springframework.context.support.ClassPathXmlApplicationContext@1221be2: startup date [Wed Nov 20 20:21:40 CST 2019]; root of context hierarchy
十一月 20, 2019 8:21:40 下午 org.springframework.beans.factory.xml.XmlBeanDefinitionReader loadBeanDefinitions
信息: Loading XML bean definitions from class path resource [META-INF/spring/knights.xml]
十一月 20, 2019 8:21:41 下午 org.springframework.beans.factory.support.DefaultListableBeanFactory preInstantiateSingletons
信息: Pre-instantiating singletons in org.springframework.beans.factory.support.DefaultListableBeanFactory@6e6d5e: defining beans [knight,quest]; root of factory hierarchy
Embarking on quest to slay the dragon!
十一月 20, 2019 8:21:41 下午 org.springframework.context.support.AbstractApplicationContext doClose
信息: Closing org.springframework.context.support.ClassPathXmlApplicationContext@1221be2: startup date [Wed Nov 20 20:21:40 CST 2019]; root of context hierarchy
十一月 20, 2019 8:21:41 下午 org.springframework.beans.factory.support.DefaultSingletonBeanRegistry destroySingletons
信息: Destroying singletons in org.springframework.beans.factory.support.DefaultListableBeanFactory@6e6d5e: defining beans [knight,quest]; root of factory hierarchy
```

> 基于Java配置

```
package com.springinaction.knights;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KnightConfig {

  @Bean
  public Knight knight() {
    return new BraveKnight(quest());
  }
  
  @Bean
  public Quest quest() {
    return new SlayDragonQuest(System.out);
  }

}

```

```
package com.springinaction.knights;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class KnightMain {

	public static void main(String[] args) throws Exception{
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/knights.xml");
	    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(com.springinaction.knights.KnightConfig.class); 
		Knight knight = context.getBean(Knight.class);
		knight.embarkOnQuest();
		 context.close();

	}

}
```
Output是一样的。

更多Denpendency Injection内容：
[Dhanji R. Prasanna's Dependency Injection](https://www.manning.com/books/dependency-injection#toc)
