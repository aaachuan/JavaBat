### Aspect-Oriented Programming(AOP)

#### 应用切面

面向切面编程往往被定义为促使软件系统实现关注点分离的一项技术。

```
package com.springinaction.knights;

import java.io.PrintStream;

public class Minstrel {

	  private PrintStream stream;
	  
	  public Minstrel(PrintStream stream) {
	    this.stream = stream;
	  }

	  public void singBeforeQuest() {
	    stream.println("Fa la la, the knight is so brave!");
	  }

	  public void singAfterQuest() {
	    stream.println("Tee hee hee, the brave knight " +
	    		"did embark on a quest!");
	  }

	}
```

不好的做法：
```
package com.springinaction.knights;

public class BraveKnight implements Knight {

  private Quest quest;
  private Minstrel minstrel;

  public BraveKnight(Quest quest, Minstrel minstrel) {
    this.quest = quest;
    this.minstrel = minstrel;
  }

  public void embarkOnQuest() throws QuestException {
    minstrel.singBeforeQuest();	
    quest.embark();
    minstrel.singAfterQuest();
  }

}
```
现在，简单的`BraveKnight`类开始变得复杂，而利用AOP，`BraveKnight`本身不用直接访问`Minstrel`的方法。

要将`Minstrel`抽象为一个切面，所需要做的就是在Spring配置文件声明它，将`Minstrel`定义为一个切面。

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
 
  <bean id="minstrel" class="com.springinaction.knights.Minstrel">
    <constructor-arg value="#{T(System).out}" />
  </bean>
  
  <aop:config>
    <aop:aspect ref="minstrel">
      <aop:pointcut id="embark"
          expression="execution(* *.embarkOnQuest(..))"/>
        
      <aop:before pointcut-ref="embark" 
          method="singBeforeQuest"/>

      <aop:after pointcut-ref="embark" 
          method="singAfterQuest"/>
    </aop:aspect>
  </aop:config>

</beans>
```
首先，需要把`Minstrel`声明为一个bean，然后在`<aop:aspect>`元素中引用。`<aop:before>`在方法执行前调用，称为前置通知（before advice）。`<aop:after>`在方法执行后调用，称为后置通知（after advice）。两种方式中，`pointcut-ref`都引用`embark`的切入点。`expression`的语法为AspectJ的切点表达式语言。

```
十一月 21, 2019 11:23:24 上午 org.springframework.context.support.AbstractApplicationContext prepareRefresh
信息: Refreshing org.springframework.context.support.ClassPathXmlApplicationContext@1221be2: startup date [Thu Nov 21 11:23:24 CST 2019]; root of context hierarchy
十一月 21, 2019 11:23:24 上午 org.springframework.beans.factory.xml.XmlBeanDefinitionReader loadBeanDefinitions
信息: Loading XML bean definitions from class path resource [META-INF/spring/knights.xml]
十一月 21, 2019 11:23:24 上午 org.springframework.beans.factory.support.DefaultListableBeanFactory preInstantiateSingletons
信息: Pre-instantiating singletons in org.springframework.beans.factory.support.DefaultListableBeanFactory@d2e7d9: defining beans [knight,quest,minstrel,org.springframework.aop.config.internalAutoProxyCreator,org.springframework.aop.aspectj.AspectJPointcutAdvisor#0,org.springframework.aop.aspectj.AspectJPointcutAdvisor#1,embark]; root of factory hierarchy
Fa la la, the knight is so brave!
十一月 21, 2019 11:23:25 上午 org.springframework.context.support.AbstractApplicationContext doClose
信息: Closing org.springframework.context.support.ClassPathXmlApplicationContext@1221be2: startup date [Thu Nov 21 11:23:24 CST 2019]; root of context hierarchy
Embarking on quest to slay the dragon!
Tee hee hee, the brave knight did embark on a quest!
十一月 21, 2019 11:23:25 上午 org.springframework.beans.factory.support.DefaultSingletonBeanRegistry destroySingletons
信息: Destroying singletons in org.springframework.beans.factory.support.DefaultListableBeanFactory@d2e7d9: defining beans [knight,quest,minstrel,org.springframework.aop.config.internalAutoProxyCreator,org.springframework.aop.aspectj.AspectJPointcutAdvisor#0,org.springframework.aop.aspectj.AspectJPointcutAdvisor#1,embark]; root of factory hierarchy

```
首先，`Minstrel`仍然是POJO，没有任何代码表明它要被作为一个使用。其次，`Minstrel`可以被运用到`BraveKnight`中，而`BraveKnight`不需要显示调用它，`BraveKnight`完全不知道`Minstrel`的存在。

关于AspectJ依赖的问题：

[SpringFramework: instantiation exception](https://stackoverflow.com/questions/18335887/springframework-instantiation-exception)
