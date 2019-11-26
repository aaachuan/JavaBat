### Wiring Bean

#### Spring配置方案

创建应用对象之间协作关系的行为称为装配（wiring），这也是DI的本质。

Spring提供了三种主要的装配机制：
* 隐式的bean发现机制和自动装配
* 在Java中进行显示配置
* 在XML中进行显示配置

建议尽可能使用自动配置的机制，显式配置越少越好。但是，当源码不是由自己维护时，而自己需要为这些代码配置bean的时候，还是需要显示配置bean的，
这时居其次使用类型安全并比XML更强大的JavaConfig。

#### XMLConfig装配Bean

