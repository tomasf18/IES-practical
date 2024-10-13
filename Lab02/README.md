# 112981

# Lab 2: Objective of this lab

   -  Deploy a java web application into an application container (servlets). 
   -  Start a simple web application with Spring Boot and Spring Initializr.

## Table of Contents
   1. [Embedded Jetty Server](#embedded-jetty-server)
   2. [Server-side programming and application servers (Tomcat) - Jakarta EE](#server-side-programming-and-application-servers-tomcat---jakarta-ee)
   3. [Spring Boot - Web development with a full-featured framework](#spring-boot---web-development-with-a-full-featured-framework)
   4. [RESTful web service - quotes](#restful-web-service---quotes)
   5. [References](#references)

## Embedded Jetty Server

1. Create a Maven project
2. Modify pom.xml:

```xml
    <dependency>
        <groupId>org.eclipse.jetty</groupId>
        <artifactId>jetty-server</artifactId>
        <version>9.2.15.v20160210</version>
    </dependency>

    <dependency>
        <groupId>org.eclipse.jetty</groupId>
        <artifactId>jetty-servlet</artifactId>
        <version>9.2.15.v20160210</version>
    </dependency>
```

3. Create a java file `EmbeddedJettyExample.java`:

```java
   import org.eclipse.jetty.server.Server;
   
   public class EmbeddedJettyExample {
      public static void main(String[] args) throws Exception {
         Server server = new Server(8680);
         try {
               server.start();
               server.dumpStdErr(); // dump the server's standard error output to the console (for debug)
               server.join();
         } catch (Exception e) {           
               e.printStackTrace();
         }  
      }
   }
```

- You can run this project in eclipse by executing `JettyEmbeddedExample`.
- This runs an HTTP server on port 8680.
- A simple server, won’t do anything useful work as there are no handlers. 
- This will return 404 error for every request.
- join() method is used to wait for the server to stop.

Once the server is running at http://127.0.0.1:8680, check connectivity with:
```bash
   curl -X GET http://127.0.0.1:8680
```


5. To upgrade the server to handle HTTP requests, create a new class, HelloServlet.java:
- Below example configures a single HelloServlet.

```java
   public class EmbeddedJettyExample {
   
      public static void main(String[] args) throws Exception {
         
         Server server = new Server(8680);       
         
         ServletHandler servletHandler = new ServletHandler();
         server.setHandler(servletHandler);
                  
         servletHandler.addServletWithMapping(HelloServlet.class, "/");
         
         server.start();
         server.join();

      }
      
      public static class HelloServlet extends HttpServlet 
      {
         protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
         {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>New Hello Simple Servlet</h1>"); 
               } 
         }
   }
```


Also, change the EmbeddedJettyExample.java to include the HelloServlet before starting the server, in `server.start()`.

With this change, the server is now handling requests to the root path / with the HelloServlet servlet. Mapping the servlet to the root path means that the servlet will be invoked when the server receives a request to the root path.

6. You can run the server using Maven.

```bash
   mvn exec:java -Dexec.mainClass="ex1.lab2.ies.deti.ua.EmbeddedJettyExample" -q
   # Test it:
   curl -X GET http://0.0.0.0:8680
```

**Note**: *This method doesn't sanitize the input, be careful with XSS attacks.*

7. Try to run the server and make a request with a parameter

If you wanna accept request parameters to the http GET request, you can use the `request.getParameter()` method. For example, to get the msg parameter from the request:

```java
   String message = request.getParameter("msg");
```

```bash
   mvn exec:java -Dexec.mainClass="ex1.lab2.ies.deti.ua.EmbeddedJettyExample" -q
   curl -X GET http://127.0.0.1:8680/?msg=”Hard workers welcome!”
```

## Server-side programming and application servers (Tomcat) - Jakarta EE

**Goal:** Use `Jakarta EE` to create a simple web application and deploy it in a `Tomcat` server using `Docker`.

1. Start by creating a new Jakarta EE application, based on the Web profile.
   - Download the zip and adapt it to implement a similar function to the previous exercise (print a custom message using parameters passed to the Servlet in the URL)

2. Deploy it in Tomcat.
   - To do that, you need to create a `compose.yml` file in the root directory of the project with the `tomcat` service:

```yml
   version: '3.8'
   services:
   tomcat:
      image: tomcat:10.0-jdk17
      ports:
         - "8888:8888"
         - "5005:5005"
      volumes:
         - ./target:/usr/local/tomcat/webapps
      environment:
         - JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
      command: catalina.sh run
```

**Note:** *Notice that the local `./target` folder (in which the war will be prepared) is being mapped to the default `webapps` location of Tomcat (upon start, `Tomcat will deploy the wars found in this folder`).*

3. Build the project
```bash
   mvn clean package
```

4. Run the Docker container
```bash
   docker compose up
```

Now, you can access the application via:
- [http://127.0.0.1:8888/JakartaWebStarter-1.0-SNAPSHOT/hello-servlet?msg=”Hardworkerswelcome!”](http://127.0.0.1:8888/JakartaWebStarter-1.0-SNAPSHOT/hello-servlet?msg=”Hardworkerswelcome!”).

**Note:**  
```txt
   The process for creating, deploying, and executing a Jakarta Servlet-based web application is different from that of Java classes which are packaged and executed as a Java application archive (JAR). It can be summarized as follows:

      1. Develop the web component code.

      2. Develop the deployment descriptor files, if necessary.

      3. Compile the web component code against the libraries of the servlet container and the helper libraries, if any.

      4. Package the compiled code along with helper libraries, assets and deployment descriptor files, if any, into a deployable unit, called a web application archive (WAR).

      5. Deploy the WAR into a servlet container.

      6. Run the web application by accessing a URL that references the web component.
```

## Spring Boot - Web development with a full-featured framework

**Note:** *I used the web UI of Spring Initializr, but VSCode can be used too*  

1. Create a new Spring Boot project using the [https://start.spring.io/](Spring Initializr)   
  
**OR**

1. Install the **Spring Boot Extension Pack* on VSCode. 

2. Create a new project using the *Spring Initializr* in command palette (`Ctrl+Shift+P`) and search for `Spring Initializr: Generate a Maven Project`.  
Choose the following options:

- `Project:`      Maven
- `Language:`     Java
- `Spring Boot:`  3.3.4
- `GroupID:`      ex3.lab2.ies.deti.ua
- `ArtifactID:`   web-app
- `*Packaging*:`  Jar
- `Java:`         21
- `Dependencies:` Spring Web | Thymeleaf | Spring Boot DevTools


### Spring MVC Controller

3. Create a `Web Controller` to `handle the requests`. 
   - You can easily identify the controller by the `@Controller` and `@GetMapping` **annotations**.  
   - In the following example, `GreetingController` handles GET requests for `/greeting` by returning the name of a **Template View** (in this case, greeting). A `View` is responsible for rendering the HTML content.  

```java
   import org.springframework.stereotype.Controller;
   import org.springframework.ui.Model;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.RequestParam;

   @Controller
   public class GreetingController {

      @GetMapping("/greeting")
      public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
         model.addAttribute("name", name);
         return "greeting";
      }

   }
```


This controller is concise and simple, but there is plenty going on. We break it down step by step.
   - The `@GetMapping` annotation ensures that HTTP GET requests to `/greeting` are mapped to the `greeting()` method.
   - The `@RequestParam` binds the value of the query string parameter name into the name parameter of the `greeting()` method. This query string parameter is not required. If it is absent in the request, the defaultValue of World is used. The value for the name parameter is added to a Model object, ultimately making it accessible to the view template.

4. To use a `server-side` rendering technology called `Thymeleaf` to render the HTML content, create a new file (**src/main/resources/templates/greeting.html**), like the following, where the `th:text` attribute `${name}` is used to evaluate the expression and display the result:

```html
   <!DOCTYPE HTML>
   <html xmlns:th="http://www.thymeleaf.org">
   <head> 
      <title>Getting Started: Serving Web Content</title> 
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
   </head>
   <body>
      <p th:text="|Hello, ${name}!|" />
   </body>
   </html>
```

   -  Notice that, when you change something, you need to restart the server. 
   - You can solve this problem by using the `Spring Boot DevTools`. 
   - To use it, add the following dependency either when creating a new project or directly on the `pom.xml` file:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
</dependency>
```

5. Create the main class to run the application:
   - `src/main/java/ies/lab02/SpringBootWebApplication.java`

```java
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;

   @SpringBootApplication
   public class ServingWebContentApplication {

      public static void main(String[] args) {
         SpringApplication.run(ServingWebContentApplication.class, args);
      }

   }
```

`@SpringBootApplication` is a convenience **annotation** that adds all of the following annotations:

   - `@Configuration`: Tags the class as a source of bean definitions for the application context.

   - `@EnableAutoConfiguration`: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings. For example, if s`pring-webmvc` is on the classpath, this annotation flags the application as a web application and activates key behaviors, such as setting up a `DispatcherServlet`.

   - `@ComponentScan`: Tells Spring to look for other components, configurations, and services in the `ex3.lab2.ies.deti.web_app` package, letting it find the controllers.

   - The `main()` method uses Spring Boot’s `SpringApplication.run()` method to launch an application. Did you notice that there was not a single line of XML? There is no `web.xml` file, either. This web application is 100% pure Java and you did not have to deal with configuring any plumbing or infrastructure. 
   - To clean, compile and test the application, use the following Maven commands, that end with an executable jar:

```bash
   ./mvnw clean package
   java -jar target/spring-boot-web-0.0.1-SNAPSHOT.jar
```

   - Now, you can access the application via [http://127.0.0.1:8888/greeting?name=Tomas](http://127.0.0.1:8888/greeting?name=Tomas).
   - You can also, add a home page (`root`, "/") in `resources/static/index.html`:

```html
   <!DOCTYPE HTML>
   <html>
   <head> 
      <title>Getting Started: Serving Web Content</title> 
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
   </head>
   <body>
      <p>Get your greeting <a href="/greeting">here</a></p>
   </body>
   </html>
```

   - Access the root page via [http://127.0.0.1:8888](http://127.0.0.1:8888).

**Note:** This is pretty much similar with `Flask`, but for Java. Just like `Thymeleaf` is similar with `Jango`, but also for Java. See [here](https://spring.io/guides/gs/serving-web-content).


## RESTful web service - quotes

   - *Jakarta EE* is powerful and comprehensive, but in many cases (like creating *RESTful APIs*), we don’t need the full-feature (and more complex) Application Server. 
   - So, for *RESTful web services, we can use **Spring Boot* to create a simple API.
   - Here is an example (for the Quotes exercise):

1. Firstly, we need to create a class to represent each data object (`record`):

```java
   public record Quote(int id, String show_name, String text) { }
   public record Show(int id, String name) { }
```

2. Secondly, we need a `Service` to use the data and manage it according to the request:

```java
   @Service
   public class QuoteService { 
      // hardcoded data
      private static final List<Show> shows = List.of(
         new Show(1, "Star Wars"),
         new Show(2, "Titanic"),
         new Show(3, "Avengers: Endgame"),
         new Show(4, "Scarface")
      );

      private static final List<Quote> quotes = List.of(
         new Quote(1, "Star Wars", "May the Force be with you."),
         new Quote(2, "Titanic", "I'm the king of the world!"),
         new Quote(3, "Avengers: Endgame", "I am Iron Man."),
         new Quote(4, "Scarface", "Say hello to my little friend!"),
         new Quote(5, "Star Wars", "I find your lack of faith disturbing."),
         new Quote(6, "Titanic", "I'll never let go, Jack. I promise.")
      );
      
      public List<Show> getShows() {
         return shows;
      }

      public Quote getRandomQuote() {
         return quotes.get((int) (Math.random() * quotes.size()));
      }

      public List<Quote> getQuotesByShowId(int showId) {
         List<Show> shows = this.getShows();
         Show choice = shows.stream().filter(show -> show.id() == showId).findFirst().orElse(null);
         if (choice == null) {
               return null;
         }

         return quotes.stream().filter(quote -> quote.show_name().equals(choice.name())).collect(Collectors.toList());
      }
   }
```

3. Then, we need a `Controller` to ensure that each of the possible HTTP GET requests has a method to handle it (this controller will use the `Service` defined above):

```java
   @RestController
   @RequestMapping("/api")
   public class QuoteController {

      @Autowired
      private QuoteService service;

      @GetMapping("/quote")
      public Quote randomQuote() {
         return service.getRandomQuote();
      }

      @GetMapping("/shows")
      public List<Show> shows() {
         return service.getShows();
      }

      @GetMapping("/quotes")
      public List<Quote> quotes(@RequestParam("show_id") String show_id) {
         return service.getQuotesByShowId(Integer.parseInt(show_id));
      }

   }
```

4. Finally, we need to create an `application class` to run our RESTful API (this class is automatically created with Spring Initializr):

```java
   @SpringBootApplication
   public class QuotesApiApplication {

      public static void main(String[] args) {
         SpringApplication.run(QuotesApiApplication.class, args);
      }

   }
```

   - In the end, we have three `endpoints`:
      - `/api/shows`: returns the list of shows available in the quotesDict.
      - `/api/quote`: returns a random quote from the quotesDict.
      - `/api/quotes?show=<show_id>`: returns all quotes from a specific show. If the show is not found, it returns an error message.

   - Run the app with the following commands:

```bash
   ./mvnw clean package
   java -jar target/spring-boot-web-0.0.1-SNAPSHOT.jar
```

   - And try the endpoints:

```bash
curl -X GET http://localhost:8080/api/shows | jq
curl -X GET http://localhost:8080/api/quote | jq
curl -X GET http://localhost:8080/api/quotes?show=2 | jq
```

## References

   - [Jetty Documentation](https://jetty.org/docs/index.html)
   - [Jakarta EE Documentation](https://jakarta.ee/specifications/platform/8/)
   - [Spring Boot Documentation](https://docs.spring.io/spring-boot/documentation.html)
   - [Thymeleaf Documentation](https://www.thymeleaf.org/)
   - [Tomcat Docker Image](https://hub.docker.com/_/tomcat)
   - [Spring Boot RESTful API documentation](https://spring.io/guides/gs/rest-service)

