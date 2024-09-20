112981

# Objective of this lab

Professional software engineering relies on team-oriented tools and practices to enhance development 
effectiveness. These tools should promote productivity, collective ownership, code/effort reusing, and 
deter from the mentality of “it works on my computer…”.  
This lab addresses the basic practices to set up a development environment that facilitates cooperative 
development for enterprise Java projects, specifically:  
    MAVEN - use a project build tool to configure the development project and automatically manage dependencies.  
    GIT - collaborate in code projects using git for source code management (SCM).  
    DOCKER - apply a container technology to speed up and reuse deployments.  



# Build Lifecycle Basics

The regular “build” of a (large) project takes several steps (i.e., is made up of phases):
    - obtaining dependencies  
    - compiling source code  
    - packaging artifacts  
    - updating documentation  
    - installing on the server  
    - etc.  

The defult lifecycle comprises of (="é composto por") the following phases:  
    1. `validate` - validate the project is correct and all necessary information is available  
    2. `initialize` - initialize build state, e.g. set properties or create directories.  
    3. `generate-sources` - generate any source code for inclusion in compilation.  
    4. `process-sources` - process the source code, for example to filter any values.  
    5. `generate-resources` - generate resources for inclusion in the package.  
    6. `process-resources` - copy and process the resources into the destination directory, ready for packaging.  
    7. `compile` - compile the source code of the project  
    8. `process-classes` - post-process the generated files from compilation, for example to do bytecode enhancement on Java classes.  
    9. `generate-test-sources` - generate any test source code for inclusion in compilation.  
    10. `process-test-sources` - process the test source code, for example to filter any values.  
    11. `generate-test-resources` - create resources for testing.  
    12. `process-test-resources` - copy and process the resources into the test destination directory.  
    13. `test-compile` - compile the test source code into the test destination directory  
    14. `process-test-classes` - post-process the generated files from test compilation, for example to do bytecode enhancement on Java classes.  
    15. `test` - test the compiled source code using a suitable unit testing framework. These tests should not require the code be packaged or deployed  
    16. `prepare-package` - perform any operations necessary to prepare a package before the actual packaging. This often results in an unpacked, processed version of the package.  
    17. `package` - take the compiled code and package it in its distributable format, such as a JAR.   
    18. `pre-integration-test` - perform actions required before integration tests are executed. This may involve things such as setting up the required environment.  
    19. `integration-test` - process and deploy the package if necessary into an environment where integration tests can be run.  
    20. `post-integration-test` - perform actions required after integration tests have been executed. This may including cleaning up the environment.  
    21. `verify` - run any checks on results of integration tests to ensure quality criteria are met  
    22. `install` - install the package into the local repository, for use as a dependency in other projects locally  
    23. `deploy` - done in the build environment, copies the final package to the remote repository for sharing with other developers and projects.  

In medium to large projects, these tasks are coordinated by a `build tool`. The most common one for Java project is `Maven`.



# Getting started with Maven

Java Maven projects can be opened in the main IDEs, but first we'll use de CLI (command line), because the entire `lifecycle`
can be managed from the command line.


## Creating a Project

1. Create a directory for the project and open the CLI.
2. Run the following command:   

```bash
    mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false
```

### What did I just do?

#### `archetype:generate` is named a `Maven goal`
I executed the Maven goal archetype:generate, and passed in various parameters to that goal (` “-D” switch is used to define/pass a property to Maven in CLI`).  
The prefix `archetype` is the `plugin` that provides the goal.  
This `archetype:generate` goal created a simple project based upon a maven-archetype-quickstart archetype.  
Suffice (Basta) it to say for now that `a plugin is a collection of goals with a general common purpose`.  


3. Notice that the command crated a directory with the same name given as the `artifactId` (this is the `project name`).
4. Notice the following standard project structure:

``` txt
    my-app
    |-- pom.xml
    `-- src
        |-- main
        |   `-- java
        |       `-- com
        |           `-- mycompany
        |               `-- app
        |                   `-- App.java
        `-- test
            `-- java
                `-- com
                    `-- mycompany
                        `-- app
                            `-- AppTest.java
```

#### The `src/main/java` directory contains the project source code  
#### The `src/test/java` directory contains the test source  
#### The `pom.xml` file is the project's Project Object Model, or POM 


## The POM

The `pom.xml` file is the core of a project's configuration in Maven.  
It is a single `configuration file` that `contains the` majority of `information required to build a project` in just the way you want.  
The POM is huge and can be daunting (assustador) in its complexity, but it is not necessary to understand all of the intricacies just yet to use it effectively. 


## Build the Project

```bash
    mvn package
```

The command line will print out various actions, and end with the following:

```txt

    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time:  2.953 s
    [INFO] Finished at: 2019-11-24T13:05:10+01:00
    [INFO] ------------------------------------------------------------------------

```

Unlike the first command executed (archetype:generate), the second is simply a single word - `package`.  
Rather than a goal, this is a `phase`.  
`A phase is a step in the build lifecycle`, which is an ordered sequence of phases.  
When a phase is given, Maven executes every phase in the sequence up to (até) and including the one defined.  
For example, if I execute the `compile` phase, the phases that actually get executed are:
1. validate
2. generate-sources
3. process-sources
4. generate-resources
5. process-resources
6. compile

I may test the newly compiled and packaged JAR with the following command:

```bash
    java -cp target/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App
```

Which will print the quintessential:

```txt
    Hello World!
```



# Naming Conventions


## groupId

```txt
    -DgroupId=com.mycompany.app
```

`groupId` `uniquely` identifies your project across all projects.  
A group ID should follow Java's package name rules. This means `it starts with a reversed domain name` you control.  

For example:
```txt
    org.apache.maven, org.apache.commons, ex1.Lab01.ies.deti.ua (ua.deti.ies.Lab01.ex1 -> domain.sub-domain.folder.file)
```

Developers should take steps to `avoid` the possibility of `two published packages having the same name` by choosing unique 
package names for packages that are widely distributed.  
This allows packages to be easily and automatically installed and catalogued. 

I form a unique package name by first having (or belonging to an organization that has) an Internet domain name, such as `ua.pt`.  
I then reverse this name, component by component, to obtain, in this example, `pt.ua`, and use this as a prefix for your package names, 
using a convention developed within your organization to further administer package names.  
The name of a package is not meant to imply where the package is stored within the Internet.  
I can create as many subgroups as I want. A good way to determine the granularity of the groupId is to use the `project structure`.


## artifactId

`artifactId` is the name of the jar without version. If you created it, then you can choose whatever name you want `with lowercase letters and no strange symbols`.  
eg. `maven`, `commons-math`, `my-weather-app`


## version

`version` if you distribute it, then you can choose any typical version with numbers and dots (1.0, 1.1, 1.0.1, ...).  
Don't use dates as they are usually associated with SNAPSHOT. If it's a third party artifact, you have to use their version number whatever it is, and as strange as it can look. For example, `2.0`, `2.0.1`, `1.3.1`.



# POM main elements

```xml
  <modelVersion>4.0.0</modelVersion>

  <groupId>ex2.lab1.ies.deti.ua</groupId>
  <artifactId>my-weather-radar</artifactId>
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>my-weather-radar</name>
  <url>http://maven.apache.org</url>

  <properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <developers>
    <developer>
      <id>team-leader</id>
      <name>Tomás Santos</name>
      <email>t.santos.f@ua.pt</email>
      <roles>
        <role>Team Leader</role>
      </roles>
    </developer>
    <developer>
      <id>...</id>
      <name>...</name>
      <email>...</email>
      <roles>
        <role>...</role>
      </roles>
    </developer>
    ...
  </developers>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId> <!--  Remember! 'Artifact' is only a human-made thing, in this case: libraries, etc. -->
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
```

## Declaring project dependencies

Build tools allow to state the project dependencies on external artifacts.  
Maven will be able to retrieve well-known dependencies from the `Maven central repository automatically`.  
In this project, we will need to open a HTTP connection, create a well-formatted GET request, get the 
JSON response, process the response content.  
Instead of programming all these (demanding) steps by hand, `we could use a good component/library`, or, in Maven terms, 
`declare dependecies for artifacts`.

```xml
    <dependency>
      <groupId>com.squareup.retrofit2</groupId>
      <artifactId>retrofit</artifactId>
      <version>2.3.0</version>
    </dependency> 
```

Note the artifact coordinates below; we can easily locate this artifact by `searching the Maven central`.  
 <`groupId`>com.squareup.retrofit2</`groupId`>  
 <`artifactId`>retrofit</`artifactId`>  

`Note:` In POM, we declare `direct dependencies`; these artifacts will usually require other dependencies, forming a graph of project dependencies. 



# Maven commands

`Note:` `mvn exec:java` can receive command line arguments  
`eg.:` 
```xml 
    mvn exec:java -Dexec.mainClass="ex2.lab1.ies.deti.ua.WeatherStarter" -Dexec.args="arg0 arg1 arg2" 
```

Here’s a list of the most useful and frequently used Maven commands along with their purposes.  
These commands are used to build, manage dependencies, and run various phases of a Maven project lifecycle.

### 1. **mvn clean**
   - **Purpose**: Cleans the project by removing the `target/` directory, which contains all compiled files and artifacts from previous builds.
   - **Command**:
     ```bash
     mvn clean
     ```

### 2. **mvn compile**
   - **Purpose**: Compiles the source code of the project.
   - **Command**:
     ```bash
     mvn compile
     ```

### 3. **mvn package**
   - **Purpose**: Packages the compiled code into a JAR, WAR, or any other format depending on the project's configuration.
   - **Command**:
     ```bash
     mvn package
     ```

### 4. **mvn install**
   - **Purpose**: Installs the package (e.g., JAR file) into the local Maven repository so it can be used as a dependency in other projects.
   - **Command**:
     ```bash
     mvn install
     ```

### 5. **mvn test**
   - **Purpose**: Runs all the unit tests in the project.
   - **Command**:
     ```bash
     mvn test
     ```

### 6. **mvn exec:java**
   - **Purpose**: Executes a Java class from your project using the `exec-maven-plugin`.
   - **Command** (replace `MainClass` with your fully qualified class name):
     ```bash
     mvn exec:java -Dexec.mainClass="com.example.MainClass"
     ```

### 7. **mvn clean install**
   - **Purpose**: Combines the `clean` and `install` commands. It cleans the project and installs the compiled package into the local repository.
   - **Command**:
     ```bash
     mvn clean install
     ```

### 8. **mvn dependency:tree**
   - **Purpose**: Displays the dependency tree of the project. This is useful for identifying conflicts or checking the versions of dependencies being used.
   - **Command**:
     ```bash
     mvn dependency:tree
     ```

### 9. **mvn dependency:resolve**
   - **Purpose**: Resolves the dependencies and displays which ones are required by the project.
   - **Command**:
     ```bash
     mvn dependency:resolve
     ```

### 10. **mvn validate**
   - **Purpose**: Validates the project to ensure all required information is available.
   - **Command**:
     ```bash
     mvn validate
     ```

### 11. **mvn verify**
   - **Purpose**: Verifies that the project is correctly built, typically by running integration tests (if any).
   - **Command**:
     ```bash
     mvn verify
     ```

### 12. **mvn exec:exec**
   - **Purpose**: Executes arbitrary programs, including external scripts, using the `exec-maven-plugin`.
   - **Command**:
     ```bash
     mvn exec:exec -Dexec.executable="your_program"
     ```

### 13. **mvn versions:display-dependency-updates**
   - **Purpose**: Shows the latest versions of dependencies, highlighting any newer versions that are available.
   - **Command**:
     ```bash
     mvn versions:display-dependency-updates
     ```

### 14. **mvn site**
   - **Purpose**: Generates project reports, including Javadocs, test reports, and more.
   - **Command**:
     ```bash
     mvn site
     ```

### 15. **mvn help:describe**
   - **Purpose**: Provides information about a specific goal or phase in Maven. Useful if you want more details on a particular command.
   - **Command** (replace `goal` with the Maven goal you're interested in):
     ```bash
     mvn help:describe -Dgoal=compile
     ```

### 16. **mvn jetty:run**
   - **Purpose**: Runs a web application using Jetty (an embedded web server).
   - **Command**:
     ```bash
     mvn jetty:run
     ```

These commands will help you manage your Maven projects efficiently, from compiling to deploying, and checking dependencies.

# Notes

Compile and run the project, either from the IDE or the CLI:
```bash
mvn package # get dependencies, compiles the project and creates the jar
mvn exec:java -Dexec.mainClass="ex2.lab1.ies.deti.ua.WeatherStarter" # adapt to match your own package structure and class name 
```  

`Note:` `mvn exec:java` can receive command line arguments  
`eg.:` 
```xml 
    mvn exec:java -Dexec.mainClass="ex2.lab1.ies.deti.ua.WeatherStarter" -Dexec.args="arg0 arg1 arg2" 
```