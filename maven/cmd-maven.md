#### maven?

Apache Maven is a software project management and comprehension tool. Based on the concept of a project object model (POM), Maven can manage a project's build, reporting and documentation from a central piece of information.

Maven’s primary goal is to allow a developer to comprehend the complete state of a development effort in the shortest period of time. In order to attain this goal, there are several areas of concern that Maven attempts to deal with:

- Making the build process easy
- Providing a uniform build system
- Providing quality project information
- Providing guidelines for best practices development
- Allowing transparent migration to new features

#### 下载

[Maven官网](http://maven.apache.org/download.cgi)

Path配置环境变量:

```
D:\Downloads\apache-maven-3.5.0\bin
```

```
Microsoft Windows [版本 6.1.7601]
版权所有 (c) 2009 Microsoft Corporation。保留所有权利。

C:\Users\Administrator>mvn -v
Apache Maven 3.5.0 (ff8f5e7444045639af65f6095c62210b5713f426; 2017-04-04T03:39:0
6+08:00)
Maven home: D:\Downloads\apache-maven-3.5.0\bin\..
Java version: 1.8.0_201, vendor: Oracle Corporation
Java home: D:\jdk1.8.0_201\jre
Default locale: zh_CN, platform encoding: GBK
OS name: "windows 7", version: "6.1", arch: "x86", family: "windows"

C:\Users\Administrator>
```
#### maven的仓库

maven仓库用于存放项目所需要的jar包，maven的仓库和项目是一对多的关系，多个项目共享一个仓库的相同jar包。
仓库默认位置在`${user.home}/.m2/repository`，因为在`D:\Downloads\apache-maven-3.5.0\conf`下的`settings.xml`有如下几行：
```
  <!-- localRepository
   | The path to the local repository maven will use to store artifacts.
   |
   | Default: ${user.home}/.m2/repository
  <localRepository>/path/to/local/repo</localRepository>
  -->
```
一看，没有.m2文件夹，有以下方法：
- cmd使用`mvn help:system`命令，在执行该命令的过程中，会生成~/.m2文件夹（~ 代表操作系统的当前用户目录），也就是本地仓库，并且会从Maven官网下载必要的依赖包到本地仓库。

- 命令行下执行`mvn test`使maven在用户目录下自动创建.m2目录。（命令本身会报错，但不影响）。

- 手动新建.m2文件夹，输入`.m2.`即可。

值得注意的是之前需要配置`JAVA_HOME`环境变量，不然maven会有报错问题。
```
C:\Users\Administrator\.m2\repository
```
为了便于快速下载相关jar包，可以使用国内maven 阿里云的下载地址：
在`D:\Downloads\apache-maven-3.5.0\conf`下的`settings.xml`下修改`<mirrors>`下新增
```
  <mirror>
            <id>alimaven</id>
            <mirrorOf>central</mirrorOf>
            <name>aliyun maven</name>
            <url>http://maven.aliyun.com/nexus/content/repositories/central/</url>
        </mirror>
```
如果repository是空的，配置完镜像再使用`mvn help:system`命令，这时候就是从aliyun上下载了。
因为仓库默认位置在`${user.home}/.m2/repository`，可以自定义修改仓库的位置。
```
<localRepository>D:/Downloads/maven/repository</localRepository>
```
经测试，路径正反斜杠均可。

#### 命令行创建maven style的java project

确保目录下没有要新建的项目文件夹，cmd运行`mvn archetype:generate -DgroupId=com.how2java -DartifactId=mavenj2se -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false`

- archetype:generate 表示创建个项目
- DgroupId 项目包名: com.how2java
- DartifactId 项目名称: mavenj2se
- DarchetypeArtifactId 项目类型: maven-archetype-quickstart
- DinteractiveMode:false 表示前面参数都给了，就不用一个一个地输入了

```
Microsoft Windows [版本 6.1.7601]
版权所有 (c) 2009 Microsoft Corporation。保留所有权利。

C:\Users\Administrator>mvn archetype:generate -DgroupId=com.how2java -DartifactI
d=mavenj2se -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=f
alse
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building Maven Stub Project (No POM) 1
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] >>> maven-archetype-plugin:3.0.1:generate (default-cli) > generate-source
s @ standalone-pom >>>
[INFO]
[INFO] <<< maven-archetype-plugin:3.0.1:generate (default-cli) < generate-source
s @ standalone-pom <<<
[INFO]
[INFO]
[INFO] --- maven-archetype-plugin:3.0.1:generate (default-cli) @ standalone-pom
---
[INFO] Generating project in Batch mode
[WARNING] No archetype found in remote catalog. Defaulting to internal catalog
[INFO] -------------------------------------------------------------------------
---
[INFO] Using following parameters for creating project from Old (1.x) Archetype:
 maven-archetype-quickstart:1.0
[INFO] -------------------------------------------------------------------------
---
[INFO] Parameter: basedir, Value: C:\Users\Administrator
[INFO] Parameter: package, Value: com.how2java
[INFO] Parameter: groupId, Value: com.how2java
[INFO] Parameter: artifactId, Value: mavenj2se
[INFO] Parameter: packageName, Value: com.how2java
[INFO] Parameter: version, Value: 1.0-SNAPSHOT
[INFO] project created from Old (1.x) Archetype in dir: C:\Users\Administrator\m
avenj2se
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 6.708 s
[INFO] Finished at: 2019-10-28T19:45:14+08:00
[INFO] Final Memory: 13M/32M
[INFO] ------------------------------------------------------------------------

C:\Users\Administrator>
```

运行完后会创建一个标准结构的maven项目，`C:\Users\Administrator\mavenj2se\pom.xml`：

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.how2java</groupId>
  <artifactId>mavenj2se</artifactId>
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>mavenj2se</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```

`C:\Users\Administrator\mavenj2se\src\main\java\com\how2java\App.java`：

```
package com.how2java;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
```

`C:\Users\Administrator\mavenj2se\src\test\java\com\how2java\AppTest.java`：

```
package com.how2java;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
```

再把目录切到项目根目录下，运行`mvn package`命令，package做了很多事情，编译，测试，打包，最后生成了一个j2se-1.0-SNAPSHOT.jar包，里面放了App这个类。
```
[INFO]
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ mavenj2se ---
[INFO] Building jar: C:\Users\Administrator\mavenj2se\target\mavenj2se-1.0-SNAPS
HOT.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 9.235 s
[INFO] Finished at: 2019-10-28T20:26:40+08:00
[INFO] Final Memory: 13M/31M
[INFO] ------------------------------------------------------------------------
```

再执行jar。

```
C:\Users\Administrator\mavenj2se>java -cp target/mavenj2se-1.0-SNAPSHOT.jar com.
how2java.App
Hello World!

```
