### Maven in action

#### create simple project

```
C:\Users\Administrator\maven\examples>mvn archetype:create -DgroupId=org.sonatyp
e.mavenbook.ch03 -DartifactId=simple -DpackageName=org.sonatype.mavenbook
[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 0.530 s
[INFO] Finished at: 2019-11-21T09:33:26+08:00
[INFO] Final Memory: 7M/17M
[INFO] ------------------------------------------------------------------------
[ERROR] Could not find goal 'create' in plugin org.apache.maven.plugins:maven-ar
chetype-plugin:3.0.1 among available goals crawl, create-from-project, generate,
 help, integration-test, jar, update-local-catalog -> [Help 1]
[ERROR]
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e swit
ch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR]
[ERROR] For more information about the errors and possible solutions, please rea
d the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoNotFoundEx
ception
```
> It's time to finally remove the support for the old create goal. For users still using it, it should fail the build with a message about using the generate goal instead.

[Maven: Could not find goal 'create' in plugin org.apache.maven.plugins:maven-archetype-plugin
](https://stackoverflow.com/questions/42314610/maven-could-not-find-goal-create-in-plugin-org-apache-maven-pluginsmaven-arc/42314656)

更正后：

```
C:\Users\Administrator\maven\examples>mvn archetype:generate -DgroupId=org.sonat
ype.mavenbook.ch03 -DartifactId=simple -DpackageName=org.sonatype.mavenbook
...
[INFO] Using following parameters for creating project from Old (1.x) Archetype:
 maven-archetype-quickstart:1.1
[INFO] -------------------------------------------------------------------------
---
[INFO] Parameter: basedir, Value: C:\Users\Administrator\maven\examples
[INFO] Parameter: package, Value: org.sonatype.mavenbook.ch03
[INFO] Parameter: groupId, Value: org.sonatype.mavenbook.ch03
[INFO] Parameter: artifactId, Value: simple
[INFO] Parameter: packageName, Value: org.sonatype.mavenbook.ch03
[INFO] Parameter: version, Value: 1.0-SNAPSHOT
[INFO] project created from Old (1.x) Archetype in dir: C:\Users\Administrator\m
aven\examples\simple
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 19.784 s
[INFO] Finished at: 2019-11-21T09:37:56+08:00
[INFO] Final Memory: 14M/33M
[INFO] ------------------------------------------------------------------------
```
创建后的目录为
```
simple/
simple/pom.xml
      /src/
      /src/main/
          /main/java
      /src/test
          /test/java
```
#### construct simple project：
```
C:\Users\Administrator\maven\examples>cd simple

C:\Users\Administrator\maven\examples\simple>mvn install
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building simple 1.0-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ simple ---

[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory C:\Users\Administrator\maven\examples
\simple\src\main\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ simple ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 1 source file to C:\Users\Administrator\maven\examples\simple\t
arget\classes
[INFO]
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ si
mple ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory C:\Users\Administrator\maven\examples
\simple\src\test\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.1:testCompile (default-testCompile) @ simple
---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 1 source file to C:\Users\Administrator\maven\examples\simple\t
arget\test-classes
[INFO]
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ simple ---
[INFO] Surefire report directory: C:\Users\Administrator\maven\examples\simple\t
arget\surefire-reports

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running org.sonatype.mavenbook.ch03.AppTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO]
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ simple ---
[INFO] Building jar: C:\Users\Administrator\maven\examples\simple\target\simple-
1.0-SNAPSHOT.jar
[INFO]
[INFO] --- maven-install-plugin:2.4:install (default-install) @ simple ---
[INFO] Installing C:\Users\Administrator\maven\examples\simple\target\simple-1.0
-SNAPSHOT.jar to D:\Downloads\maven\repository\org\sonatype\mavenbook\ch03\simpl
e\1.0-SNAPSHOT\simple-1.0-SNAPSHOT.jar
[INFO] Installing C:\Users\Administrator\maven\examples\simple\pom.xml to D:\Dow
nloads\maven\repository\org\sonatype\mavenbook\ch03\simple\1.0-SNAPSHOT\simple-1
.0-SNAPSHOT.pom
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 6.131 s
[INFO] Finished at: 2019-11-21T10:02:44+08:00
[INFO] Final Memory: 13M/31M
[INFO] ------------------------------------------------------------------------
```
```
C:\Users\Administrator\maven\examples\simple>java -cp target/simple-1.0-SNAPSHOT
.jar org.sonatype.mavenbook.ch03.App
Hello World!
```

