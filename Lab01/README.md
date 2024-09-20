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