### Dependency Injection

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
