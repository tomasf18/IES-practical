# 112981

# Lab 1: Objective of this lab

#### Note: This content is well explained in the TP slides "IES_01_GitMaven.pdf"  

Professional software engineering relies on team-oriented tools and practices to enhance development 
effectiveness. These tools should promote productivity, collective ownership, code/effort reusing, and 
deter from the mentality of “it works on my computer...”.  
This lab addresses the basic practices to `set up a development environment that facilitates cooperative development` 
for enterprise Java projects, specifically:  
    `MAVEN` - use a project build tool to configure the development project and automatically manage dependencies.  
    `GIT` - collaborate in code projects using git for source code management (SCM).  
    `DOCKER` - apply a container technology to speed up and reuse deployments.  



# Maven - Build Lifecycle Basics

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

Java Maven projects can be opened in the main IDEs, but we'll use de CLI (command line), because the entire `lifecycle`
can be managed from the command line.


## Creating a Project

1. Create a directory for the project and open the CLI.
2. Run the following command:   

```bash
    mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false
```

### What did I just do?

#### `archetype:generate` is named a `Maven goal`
I executed the Maven goal archetype:generate, and passed in various parameters to that goal (`“-D” switch is used to define/pass a property to Maven in CLI`).  
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
The POM is huge and can be daunting (assustador) in its complexity, but it is not necessary to understand all of the intricacies just 
yet to use it effectively. 


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

I form a `unique` package name by first having (or belonging to an organization that has) an Internet domain name, such as `ua.pt`.  
I then reverse this name, component by component, to obtain, in this example, `pt.ua`, and use this as a prefix for your package names, 
using a convention developed within your organization to further administer package names.  
The name of a package is not meant to imply where the package is stored within the Internet.  
I can create as many subgroups as I want. A good way to determine the granularity of the groupId is to use the `project structure`. 

e.g.: Desktop/IES/Lab1/lab1_1/ex1 -----> ex1.lab1.Lab1.IES.deti.ua.pt


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

These commands are used to build, manage dependencies, and run various phases of a Maven project lifecycle.

### **mvn archetype:generate**
   - **Purpose**: Create a project.
   - **Command**:
```bash
    mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false
```

## **mvn package**
   - **Purpose**: Packages the compiled code into a JAR, WAR, or any other format depending on the project's configuration.
   - **Command**:
```bash
mvn package
```

### **mvn exec:java**
   - **Purpose**: Executes a Java class from your project using the `exec-maven-plugin`.
   - **Command** (replace `MainClass` with your fully qualified class name):
```bash
mvn exec:java -Dexec.mainClass="ex2.lab1.ies.deti.ua.WeatherStarter" -Dexec.args="arg0 arg1 arg2" 
```

### **mvn clean**
   - **Purpose**: Cleans the project by removing the `target/` directory, which contains all compiled files and artifacts from previous builds.
   - **Command**:
```bash
mvn clean
```

### **mvn compile**
   - **Purpose**: Compiles the source code of the project.
   - **Command**:
 ```bash
 mvn compile
 ```

### **mvn versions:display-dependency-updates**
   - **Purpose**: Shows the latest versions of dependencies, highlighting any newer versions that are available.
   - **Command**:
```bash
mvn versions:display-dependency-updates
```

### **mvn test**
   - **Purpose**: Runs all the unit tests in the project.
   - **Command**:
```bash
mvn test
```

### **mvn install**
   - **Purpose**: Installs the package (e.g., JAR file) into the local Maven repository so it can be used as a dependency in other projects.
   - **Command**:
```bash
mvn install
```

### **mvn clean install**
   - **Purpose**: Combines the `clean` and `install` commands. It cleans the project and installs the compiled package into the local repository.
   - **Command**:
```bash
mvn clean install
```

### **mvn dependency:tree**
   - **Purpose**: Displays the dependency tree of the project. This is useful for identifying conflicts or checking the versions of dependencies being used.
   - **Command**:
```bash
mvn dependency:tree
```

### **mvn dependency:resolve**
   - **Purpose**: Resolves the dependencies and displays which ones are required by the project.
   - **Command**:
```bash
mvn dependency:resolve
```



# .gitignore

Specifies intentionally untracked files to ignore


## Pattern format

See [here](https://git-scm.com/docs/gitignore).  
See [here](https://github.com/github/gitignore) gitignore templates.  
Use this [.gitignore file](https://gist.github.com/bastiao/6b07c238e8d1ed4c7ff3e197660b6c5e).



# Standard Directory Layout

Having a common directory layout allows users familiar with one Maven project to immediately feel at home in another Maven project.  
If I'm used to work in Maven projects, this standard layout will help me immediately locate the files I wish if I were indicated to work 
on a unkown Maven project. It's not a rule, it is a standard for project organization.  

I can see the section that documents the directory layout `expected` by Maven and the directory layout `created` by Maven [here](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html).  

One of the standards is `src/main/resources` for Application/Library resources. And it was needed in the exercise 1.3.
Here, I was able to store the configuration file for logging support, `log4j2.xml`.



# File/Console logging integration

To add this new feature, I used the recommended logging library `Log4j2`.  
I had to add its dependencies to the `pom file` (the links provided have all the info), and also to create the logging configuration file `log4j2.xml`.  
Guess where this last file had to be located? Yup, `src/main/resources`!



# Docker - ChatGPT


## Explanation

Imagine you’re developing an application on your laptop. Everything works fine, but when you try to run the same app on another computer or server, it crashes because that system has different software, settings, or dependencies. This problem happens a lot in software development when moving applications from one environment to another.

Docker solves this issue by packaging your application and all its dependencies into containers. A container is like a lightweight, standalone package that includes everything your app needs to run, such as libraries, configuration files, and system tools. This way, your app will work consistently across different environments - whether it’s your local machine, a test server, or a production environment.

Containers are isolated from each other and the host machine, which means they won’t interfere with each other’s settings or dependencies. Docker is popular because it’s efficient (`containers are lightweight compared to traditional virtual machines`) and makes it easier to develop, test, and deploy applications in any environment.


### **Step 1: Install Docker**

### **Step 2: Create a Dockerfile**

The **Dockerfile** is a key part of Docker. It’s a simple text file that contains a set of instructions to tell Docker how to build your application’s environment.

Let’s break down a basic example for a Node.js application:

#### Example Dockerfile:

```dockerfile
# Step 1: Specify the base image
FROM node:14 

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the package.json and install dependencies
COPY package.json ./
RUN npm install

# Step 4: Copy the rest of the application code into the container
COPY . .

# Step 5: Expose a port to allow external access
EXPOSE 3000

# Step 6: Define the command to start the application
CMD ["npm", "start"]
```

**Explanation:**
1. `FROM node:14`: This tells Docker to start with a base image of Node.js version 14. The base image is a pre-configured environment with specific software, in this case, Node.js. Docker Hub has lots of official base images for different technologies (Node, Python, Java, etc.).

2. `WORKDIR /app`: This sets the working directory inside the container. Every command that follows will happen inside `/app` in the container, just like a specific folder on your computer. Think of it as the "home" folder for your app in the container.

3. `COPY package.json ./`: This copies the `package.json` file from your local project folder into the container. Docker needs this to install the project dependencies.

4. `RUN npm install`: This runs the command `npm install` inside the container to install all the Node.js dependencies from the `package.json` file.

5. `COPY . .`: This copies all the other files from your local project folder into the container (like the app's code files).

6. `EXPOSE 3000`: This tells Docker that your application will be listening on port 3000 inside the container. It’s how Docker manages external access to the container.

7. `CMD ["npm", "start"]`: This is the command that Docker will run to start your application once the container is up. In this case, it tells Docker to run `npm start`, which will start your Node.js app.

---

### **Step 3: Build a Docker Image**

Once you’ve created your Dockerfile, the next step is to build a **Docker image** from it. A Docker image is a snapshot of your application and its environment, ready to be used for running containers.

#### Command to build an image:
```bash
docker build -t my-node-app .
```

**Explanation:**
- `docker build`: This is the command to build a Docker image.
- `-t my-node-app`: This flag tags (names) your image as `my-node-app`. You can give it any name you want.
- `.`: This tells Docker to look in the current directory (`where your Dockerfile is`) `to find the Dockerfile and other necessary files`.

After this command runs, Docker will go through your Dockerfile, follow each instruction, and create an image. You can see the list of your built images by running:
```bash
docker images
```
---

### **Step 4: Run a Docker Container**

Now that you’ve built the image, you can run it as a **Docker container**. A container is a running instance of an image, isolated from your host system but with everything your app needs.

#### Command to run a container:
```bash
docker run -p 3000:3000 my-node-app
```

**Explanation:**
- `docker run`: This starts a new container from an image.
- `-p 3000:3000`: This maps port 3000 on your machine to port 3000 inside the container. This is crucial because your app inside the container is running on port 3000, and you want to access it through the same port on your local machine. You can open a browser and go to `http://localhost:3000` to see your app.
- `my-node-app`: This is the name of the image you just built. Docker will use this image to run the container.

Once the container is running, it isolates your app in its own environment, meaning it won’t interfere with other programs or dependencies on your machine.

---

### **Step 5: Check Running Containers**

While your app is running in a container, you can use Docker commands to check on it.

#### Command to list running containers:
```bash
docker ps
```

**Explanation:**
- `docker ps`: This lists all currently running containers. It shows useful information like the container’s ID, the image it's based on, and which ports are being used.

You might see something like:
```
CONTAINER ID  IMAGE          COMMAND           PORTS                    NAMES
a1b2c3d4e5    my-node-app    "npm start"       0.0.0.0:3000->3000/tcp   peaceful_einstein
```

#### Stopping a container:
When you're done with the app, you can stop the container with:
```bash
docker stop <container_id>
```

Replace `<container_id>` with the actual ID from the `docker ps` output (for example, `a1b2c3d4e5`).

---

### **Step 6: Push Docker Images to Docker Hub**

If you want to share your Docker image with others, you can push it to **Docker Hub**, a cloud-based registry for storing Docker images. This allows anyone to pull and run your image.

#### Steps:
1. First, log in to Docker Hub (create an account if you don’t have one):
    ```bash
    docker login
    ```

2. Next, tag your image with your Docker Hub username so it can be pushed:
    ```bash
    docker tag my-node-app yourusername/my-node-app
    ```

    Replace `yourusername` with your actual Docker Hub username.

3. Finally, push the image to Docker Hub:
    ```bash
    docker push yourusername/my-node-app
    ```

Now, your image is available online, and anyone can pull it using:
```bash
    docker pull yourusername/my-node-app
```

---

### **Step 7: Useful Docker Commands**

Here are a few more essential Docker commands to help you manage your images and containers:

- **List all images**: 
    ```bash
    docker images
    ```
    This shows all images on your machine.

- **Remove an image**:
    ```bash
    docker rmi <image_id>
    ```
    This removes an image from your system. Replace `<image_id>` with the actual image ID from `docker images`.

- **Remove a container**:
    ```bash
    docker rm <container_id>
    ```
    This removes a container, but first, you’ll need to stop it using `docker stop`. If you want to force remove it (stop and remove at the same time), use the `-f` flag:
    ```bash
    docker rm <container_id> -f
    ```

---

### Summary:
1. **Dockerfile**: Defines the environment and instructions for building your app.
2. **Build an image**: `docker build` creates an image from the Dockerfile.
3. **Run a container**: `docker run` starts your app in an isolated container.
4. **Check containers**: `docker ps` lists running containers.
5. **Push to Docker Hub**: Share your image using `docker push`.
6. **Manage Docker**: Use commands like `docker stop`, `docker rm`, and `docker rmi` to manage containers and images.

Each of these steps helps ensure your app runs consistently across different systems, and Docker makes it easy to deploy, share, and manage your applications!



# Docker start guide


## Start the project

1. To get started, clone the project to your local machine (git clone).  

2. Once you have the project, start the development environment using Docker Compose.  

```bash
    docker compose watch # So the changes can be updated automatically
```

3. Start making changes on the project.   

4. What happened? I was able to:  

- Start a complete development project with zero installation effort. The containerized environment provided the development environment, ensuring you have everything you need. You didn't have to install Node, MySQL, or any of the other dependencies directly on your machine. All you needed was Docker and a code editor.  
- Make changes and see them immediately. This was made possible because 1) the processes running in each container are watching and responding to file changes and 2) the files are shared with the containerized environment.


## Container images

If you’re new to container images, think of them as a standardized package that contains everything needed to run an application, including its files, configuration, and dependencies. These packages can then be distributed and shared with others.  


## Docker Hub

To share your Docker images, you need a place to store them. This is where registries come in. While there are many registries, Docker Hub is the default and go-to registry for images. Docker Hub provides both a place for you to store your own images and to find images from others to either run or use as the bases for your own images.  


## Create an image repository

Now that you have an account, you can create an image repository. `Just as a Git repository holds source code, an image repository stores container images.`

1. Go to Docker Hub.

2. Select Create repository.

3. On the Create repository page, enter the following information:

    Repository name - `getting-started-todo-app`
    Short description - feel free to enter a description if you'd like
    Visibility - select Public to allow others to pull your customized to-do app

4. Select Create to create the repository.


## Build and push the image

Now that you have a repository, you are ready to build and push your image. 

  ### What is an image/Dockerfile?

    Without going too deep yet, think of a container image as a single package that contains everything needed to run a process.  

    Any machine that runs a container using the image, will then be able to run the application as it was built without needing anything else pre-installed on the machine.  

    A Dockerfile is a text-based script that provides the instruction set on how to build the image. For this quick start, the repository already contains the Dockerfile.  

    Check that the Dockerfile has no file extension like .txt. `Some editors may append this file extension automatically which results in an error when you run the application`.


1. To get started, clone the project to your local machine.  

```bash
    git clone https://github.com/docker/getting-started-todo-app
```

And after the project is cloned, navigate into the new directory created by the clone:

```bash
    cd getting-started-todo-app
```

2. Build the project by running the following command, swapping out `DOCKER_USERNAME` with your username.

```bash
    docker build -t DOCKER_USERNAME/getting-started-todo-app .
```

For example, if your Docker username was mobydock, you would run the following:

```bash
    docker build -t mobydock/getting-started-todo-app .
```

3. To verify the image exists locally, you can use the docker image ls command:

```bash
    docker image ls
``` 

You will see output similar to the following:

```txt
tomas@tomas-ROG-Strix-G531GT-G531GT:~/Desktop/Docker /getting-started-todo-app$ docker image ls
REPOSITORY                         TAG       IMAGE ID       CREATED          SIZE
tomassf/getting-started-todo-app   latest    69f4e70c886b   11 seconds ago   1.12GB
getting-started-todo-app-client    latest    1e5d54eb05c0   27 minutes ago   1.19GB
getting-started-todo-app-backend   latest    7d445a494e73   28 minutes ago   1.17GB
traefik                            v2.11     1741c0b1ff49   2 days ago       168MB
phpmyadmin                         latest    2c40d71042e9   2 weeks ago      562MB
mysql                              8.0       f5da8fc4b539   2 months ago     573MB

...
```

4. To push the image, use the docker push command. Be sure to replace `DOCKER_USERNAME` with your username:

```bash
    docker push DOCKER_USERNAME/getting-started-todo-app
```

Depending on your upload speeds, this may take a moment to push.

5. What happenned?

I built a container image that packages my application and push it to Docker Hub.



# Docker commands

Here’s a list of the most useful Docker commands, covering everything from creating a Dockerized app to building, 
running, and managing your containers and images:

---

### **1. Dockerfile Creation**
You need a `Dockerfile` to define how to set up the environment for your app. You’ll write a `Dockerfile` with specific instructions (see above for an example).

---

### **2. Build a Docker Image**
Once your Dockerfile is ready, build the Docker image using this command:

```bash
docker build -t <image_name> .
```

- `-t <image_name>`: Tags the image with a name (e.g., `my-app`).
- `.`: The dot tells Docker to look in the current directory for the `Dockerfile`.

Example:
```bash
docker build -t my-app .
```

---

### **3. Run a Docker Container**
After the image is built, you can run a container from it:

```bash
docker run -d -p <host_port>:<container_port> <image_name>
```

- `-d`: Runs the container in detached mode (in the background).
- `-p`: Maps the host machine's port to the container's port (e.g., `8080:80`).
- `<image_name>`: The name of the image to run.

Example:
```bash
docker run -d -p 8080:3000 my-app
```

This maps port 3000 in the container to port 8080 on your machine, so you can access the app via `localhost:8080`.

---

### **4. Check Running Containers**
To see a list of all running containers, use:

```bash
docker ps
```

This shows information like the container ID, the image it’s using, and the ports it’s mapped to.

---

### **5. Stop a Running Container**
To stop a container, use:

```bash
docker stop <container_id>
```

You can get the container ID from the `docker ps` output.

---

### **6. Remove a Container**
To remove a stopped container:

```bash
docker rm <container_id>
```

If the container is running, you can stop and remove it in one step using:

```bash
docker rm <container_id> -f
```

---

### **7. List Docker Images**
To list all Docker images on your machine:

```bash
docker images
```

This shows the image name, tag, and ID.

---

### **8. Remove a Docker Image**
To remove an image that you no longer need:

```bash
docker rmi <image_id>
```

Get the image ID from `docker images`.

---

### **9. View Container Logs**
To see the logs from a running container:

```bash
docker logs <container_id>
```

This helps debug any issues by showing the output your app is generating.

---

### **10. Access a Running Container (Interactive Mode)**
To open a shell inside a running container for debugging or exploration:

```bash
docker exec -it <container_id> /bin/bash
```

This gives you a shell in the container, where you can run commands and inspect the environment.

---

### **11. Push an Image to Docker Hub**
Once you have your image ready, you can push it to Docker Hub for others to use.

1. First, log in to Docker Hub:
    ```bash
    docker login
    ```

2. Tag the image:
    ```bash
    docker tag <image_name> <username>/<repository_name>:<tag>
    ```

3. Push the image to your Docker Hub repository:
    ```bash
    docker push <username>/<repository_name>:<tag>
    ```

Example:
```bash
docker tag my-app myusername/my-app:v1
docker push myusername/my-app:v1
```

---

### **12. Pull an Image from Docker Hub**
To download an image from Docker Hub:

```bash
docker pull <username>/<repository_name>:<tag>
```

Example:
```bash
docker pull myusername/my-app:v1
```

---

### **13. Stop All Running Containers**
If you want to stop all running containers at once:

```bash
docker stop $(docker ps -q)
```

---

### **14. Remove All Stopped Containers**
To clean up all containers that are no longer running:

```bash
docker rm $(docker ps -a -q)
```

---

### **15. Remove All Docker Images**
To remove all images from your system:

```bash
docker rmi $(docker images -q)
```

---

### **16. Docker Compose (for multi-container apps)**
If your app has multiple services (like a web server and a database), Docker Compose helps you manage them easily by defining everything in a `compose.yaml` file.

To start all services defined in the `compose.yaml` file:
```bash
docker compose up -d
```

To stop them:
```bash
docker compose down
```

---

# Portainer

Portainer is a lightweight, open-source management tool for Docker and Kubernetes environments. It provides a simple and intuitive web-based user interface (UI) that allows you to manage your containerized applications and infrastructure without needing to use the command line.

### Key Features of Portainer:
1. **Web-Based UI**: Portainer gives you an easy-to-use web interface to manage Docker containers, images, networks, and volumes.
2. **Multi-Environment Support**: It supports Docker standalone, Docker Swarm, Kubernetes, and Edge environments.
3. **Container Management**: You can create, start, stop, delete, and inspect containers.
4. **Volume and Network Management**: Easily create and manage Docker volumes and networks.
5. **Image Management**: Manage Docker images, including pulling images from Docker Hub or private registries, and deploying them.
6. **Stacks and Templates**: Portainer supports Docker Compose and allows you to deploy multi-container applications (stacks). You can also create and manage application templates for easy reuse.
7. **User and Team Management**: Create users and teams with role-based access control (RBAC) to manage who can interact with resources in the Docker or Kubernetes environment.
8. **Logs and Console Access**: Direct access to container logs and the ability to open a console into a running container for debugging.
9. **App Templates**: A library of pre-configured applications for quick deployment.
10. **Edge Compute Management**: Manage distributed edge computing nodes via agents that communicate with the central Portainer instance.

### What You Can Do with Portainer:

1. **Deploy and Manage Containers**
2. **Manage Docker Images**
3. **Monitor Containers and Logs**
4. **Volume and Network Management**
5. **Simplify Multi-Environment Orchestration**
6. **User Management and Security**
7. **Templates for Easy Deployment**
8. **Edge Compute**

## How to login 

I can log into my Portainer Server instance by starting the container:
```bash
docker start portrait
```
then opening a web browser and going to:
```txt
    https://localhost:9443
```



# Docker Compose Quickstart -> All the source code for this example is in: ~/Desktop/IES/composetest


## Prerequisites

Make sure you have:

- Installed the latest version of Docker Compose
- A basic understanding of Docker concepts and how Docker works


## Step 1: Set up

1. Create a directory for the project
2. Create a file called app.py in your project directory (this means: "create your project source code")
3. Create another file called requirements.txt in your project directory
4. Create a Dockerfile


## Step 2: Define services in a Compose file

Compose simplifies the control of your entire application stack, making it easy to manage services, networks, and 
volumes in a single, comprehensible YAML configuration file.

Create a file called compose.yaml in your project directory.


## Step 3: Build and run your app with Compose

With a single command, you create and start all the services from your configuration file.

1. From your project directory, start up your application by running `docker compose up`.

`Compose` pulls a Redis image, builds an image for your code, and starts the services you defined. 
In this case, the code is statically copied into the image at build time.

2. Switch to another terminal window, and type `docker image ls` to list local images.
You can inspect images with `docker inspect <tag or id>`.

3. Stop the application, either by running `docker compose down` from within your project directory in 
the second terminal, or by hitting CTRL+C in the original terminal where you started the app.


## Step 4: Edit the Compose file to use Compose Watch

1. Edit the `compose.yaml` file in your project directory to use `watch` so you can preview your running 
Compose services which are automatically updated as you edit and save your code.

Whenever a file is changed, Compose syncs the file to the corresponding location under /code inside the container. 
Once copied, the bundler updates the running application without a restart.


## Step 5: Re-build and run the app with Compose

From your project directory, type docker `compose watch` or `docker compose up --watch` to build and launch the app and start the file watch mode.


## Step 6: Update the application

To see Compose Watch in action:

1. Change the greeting in `app.py` and save it.
2. Refresh the app in your browser. The greeting should be updated, `and the counter should still be incrementing`.


## Step 7: Split up your services

Using multiple Compose files lets you customize a Compose application for different environments or workflows. 
This is useful for large applications that may use dozens of containers, with ownership distributed across multiple teams.

1. In your project folder, create a new Compose file called `infra.yaml`. 

2. Cut the Redis service from your `compose.yaml` file and paste it into your new `infra.yaml` file. 
Make sure you add the services top-level attribute at the top of your file.

3. In your `compose.yaml` file, add the `include` top-level attribute along with the path to the `infra.yaml` file.

4. Run `docker compose up` to build the app with the updated Compose files, and run it. You should see the Hello world message in your browser.

This is a simplified example, but it demonstrates the basic principle of include and how it can make it easier to modularize complex applications into sub-Compose files.


## Step 8: Experiment with some other commands

- If you want to run your services in the background, you can pass the `-d` flag (for "detached" mode) 
to `docker compose up` and use `docker compose ps` to see what is currently running.

- Run `docker compose --help` to see other available commands.

- If you started Compose with `docker compose up -d`, stop your services once you've finished with them: `docker compose stop`

- You can bring everything down, removing the containers entirely, with the `docker compose down` command.



# How to use methods of classes of one maven project in another?

To call functions from Project 2 in Project 1, you need to:

1. **Add Project 2 as a dependency in Project 1's `pom.xml`:**
   You'll need to include Project 2's JAR as a dependency in Project 1.

   - If Project 2 is built as a Maven project, install it to your local Maven repository:
     ```bash
     mvn install
     ```
     This generates a JAR and installs it in your local Maven repository.
     In other words, I download a .jar of my project locally. Reminding what a .jar file is:

    ---
      A **`.jar` file** (Java ARchive) is a compressed file format used to package Java classes, associated metadata, and resources (like images, text files, etc.) into a single file. Essentially, it's a **ZIP file** with a `.jar` extension, optimized for use with Java applications.

      Here's what a `.jar` file typically contains:
      1. **Java class files**: These are the compiled `.class` files that your Java source code gets turned into.
      2. **Metadata**: Includes files like `META-INF/MANIFEST.MF`, which provide important information such as the main class to run (for executable `.jar` files).
      3. **Libraries**: `.jar` files can also include other `.jar` files or external libraries your application depends on.
      4. **Resources**: Any resources like images, text files, or configuration files that your Java application might need during runtime.

      ### Uses of `.jar` files:
      - **Executable JAR**: A `.jar` file can be packaged as an executable, which allows you to run the Java application by simply double-clicking it or using the command: `java -jar yourfile.jar`.
      - **Library JAR**: A `.jar` file can be used as a library that other Java applications can include and use.

      ### Benefits of using `.jar` files:
      - **Portability**: Bundles everything needed to run the program, making it easier to distribute and share across different platforms.
      - **Organization**: Packages all files (classes, libraries, resources) neatly into one file.
      - **Compression**: Since it's a ZIP format, the contents are compressed, reducing the file size.
      
      In your project, for example, you can bundle your Maven project into a `.jar` file to run the entire application or distribute it easily.
     
     ---

   - Then, add Project 2's dependency in Project 1’s `pom.xml`:
     ```xml
     <dependency>
       <groupId>ex5_api.lab1.ies.deti.ua</groupId>  <!-- Change to your groupId -->
       <artifactId>ipma-api-client</artifactId>     <!-- Artifact name of Project 2 -->
       <version>1.0-SNAPSHOT</version>             <!-- The version of Project 2 -->
     </dependency>
     ```

2. **Import Project 2 classes in Project 1:**
   In your `Main.java` file from Project 1, you can import the `CityForecast` class from Project 2:
   ```java
   import ex5_api.lab1.ies.deti.ua.CityForecast;
   ```

3. **Call Project 2 methods in Project 1:**
   After importing, you can create an instance of `CityForecast` and call its methods. For example:
   ```java
   public class Main {
       public static void main(String[] args) {
           CityForecast cityForecast = new CityForecast();
           // Call methods from CityForecast
           String forecast = cityForecast.getForecast("Lisbon");
           System.out.println("Forecast for Lisbon: " + forecast);
       }
   }
   ```

This setup allows Project 1 to use the functionality provided by Project 2. 



# How to `dockerize` a maven project?

To **dockerize** your Maven project (specifically, the **AnyCityForecast** application), you will need to:

1. **Build the "fat JAR" (executable JAR)** that includes all dependencies using Maven.
2. **Create a Dockerfile** that defines how to package and run this JAR inside a Docker container.
3. **Build a Docker image** for your project.
4. **Run the Docker container** that executes your Java application.
5. **Observe logs** from the Docker container to ensure the application runs correctly.

Let's break this process down:

### 1. Build an Executable (Fat) JAR
To create an executable JAR that includes all dependencies, you can use the **Maven Shade Plugin**. This ensures that all external libraries and dependencies are included in the JAR file.

First, in your Maven project's `pom.xml` for **AnyCityForecast** (Project 1), add the following plugin configuration:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.2.4</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <transformers>
                            <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                <mainClass>ex5.lab1.ies.deti.ua.Main</mainClass>
                            </transformer>
                        </transformers>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

Now, when you run `mvn package`, it will build a JAR file that includes all necessary dependencies. The `mainClass` in the configuration points to your main class.

You can test that the JAR is executable by running:

```bash
java -jar target/any-city-forecast-1.0-SNAPSHOT.jar
```

### 2. Create the Dockerfile

A **Dockerfile** is needed to define the Docker image that will run your application. In the root directory of your **AnyCityForecast** project (next to the `pom.xml`), create a file named `Dockerfile` with the following content:

```Dockerfile
# Use an official OpenJDK image as the base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from your Maven project into the container
COPY target/any-city-forecast-1.0-SNAPSHOT.jar /app/any-city-forecast.jar

# Define the command to run the JAR file
CMD ["java", "-jar", "any-city-forecast.jar"]
```

This Dockerfile does the following:
- **Base Image**: Uses `openjdk:17-jdk-alpine` as the base image, which is a lightweight image with OpenJDK 17.
- **WORKDIR**: Sets `/app` as the working directory.
- **COPY**: Copies the JAR file built by Maven into the container.
- **CMD**: Specifies the command to run the JAR file when the container starts.

### 3. Build the Docker Image

Next, you need to build the Docker image for your application. From the root of your Maven project (where the Dockerfile is located), run the following command:

```bash
docker build -t any-city-forecast .
```

This command builds the Docker image and tags it as `any-city-forecast`.

### 4. Run the Docker Container

Now that you have the Docker image, you can run it in a container. Use the following command to start the container:

```bash
docker run -d any-city-forecast
```

This will start the container in detached mode (`-d`), running the Java application in the background.

### 5. Inspect the Logs

To verify that the application is running correctly and producing the expected periodic output, you can inspect the container logs:

```bash
docker logs <container_id>
```

To get the `container_id`, you can list running containers with:

```bash
docker ps
```

### Example Workflow Summary:

1. **Add the Maven Shade Plugin** to the `pom.xml` of **AnyCityForecast**.
2. Run `mvn clean package` to create the "fat JAR".
3. **Write the Dockerfile**.
4. Run `docker build -t any-city-forecast .` to build the Docker image.
5. Run `docker run -d any-city-forecast` to start the container.
6. Use `docker logs` to verify the application's output.

---