# 112981

# Lab 1: Objective of this lab

   -  Deploy a java web application into an application container (servlets). 
   -  Start a simple web application with Spring Boot and Spring Initializr.

## Table of Contents
1. [Embedded Jetty Server](#embedded-jetty-server)
2. [Server-side programming and application servers (Tomcat) - Jakarta EE](#server-side-programming-and-application-servers-tomcat---jakarta-ee)
3. [Spring Boot - Web development with a full-featured framework](#spring-boot---web-development-with-a-full-featured-framework)
4. [RESTful web service - quotes](#restful-web-service---quotes)

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

In simpler terms, *Jakarta EE* is powerful and comprehensive, but for many cases (like creating *RESTful APIs*), we don’t need the full feature set of an application server. 
So, for *RESTful web services, we can use **Spring Boot* to create a simple application that returns quotes depending on the request. 
The Controller is structured as follows:
java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
public class QuotesController 
{
	public static Map<Integer,Set<String>> quotesDict = Map.of(
		1, Set.of(
			"Just when I thought I was out, they pull me back in. - The Godfather III (1990)",
			"Life moves pretty fast. If you don't stop and look around once in a while, you could miss it. - Ferris Bueller's Day Off (1986)"
		),
		2, Set.of(
			"We're goin' streaking! - Old School (2003)"
		),
		3, Set.of(
			"I'm sorry father, for you there is only death. But our destiny is life! - The Fountain (2006)",
			"Sometimes it's people closest to us who lie to us best - Arrow (2015)",
			"Hope is a good thing, maybe the best of things, and no good thing ever dies. - Shawshank Redemption (1994)"
		),
		... // TODO: complete this list
	);

    public record QuotesRecord(Set<String> contents) { }
	public record ShowsRecord(Set<Integer> showsId) { }

	@GetMapping("/api/shows")
	public ShowsRecord api_shows() 
	{
		return new ShowsRecord(QuotesController.quotesDict.keySet());
	}

	@GetMapping("/api/quote")
	public QuotesRecord api_quote() 
	{
		int randomIdx = (int) (Math.random() * quotesDict.size()) + 1;
		return new QuotesRecord(QuotesController.quotesDict.get(randomIdx));
	}

	@GetMapping("/api/quotes")
	public QuotesRecord api_quote(@RequestParam(value="show", required=false, defaultValue="-1") int showID) 
	{
		if (!QuotesController.quotesDict.containsKey(showID)) {
			return new QuotesRecord(Set.of("Show not found. Usage: /api/quotes?show=<showID>"));
		}

		return new QuotesRecord(QuotesController.quotesDict.get(showID));
	}
}


Hear, we have three endpoints:
 - /api/shows: returns the list of shows available in the quotesDict.
 - /api/quote: returns a random quote from the quotesDict.
 - /api/quotes?show=<show_id>: returns all quotes from a specific show. If the show is not found, it returns an error message.

You can give a go to the application by running the following command:
bash
mvn clean package
java -jar target/spring-boot-web-0.0.1-SNAPSHOT.jar


And try the endpoints:
bash
curl -X GET http://localhost:8080/api/shows | jq
curl -X GET http://localhost:8080/api/quote | jq
curl -X GET http://localhost:8080/api/quotes?show=3 | jq


## References

- [Jetty Documentation](https://www.eclipse.org/jetty/documentation/)
- [Jakarta EE Documentation](https://jakarta.ee/specifications/platform/8/)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/)
- [Tomcat Docker Image](https://hub.docker.com/_/tomcat)




### RESTFUL



This controller is concise and simple, but there is plenty going on under the hood. We break it down step by step.

The @GetMapping annotation ensures that HTTP GET requests to /greeting are mapped to the greeting() method.
There are companion annotations for other HTTP verbs (e.g. @PostMapping for POST). There is also a @RequestMapping annotation that they all derive from, and can serve as a synonym (e.g. @RequestMapping(method=GET)).

A key difference between a traditional MVC controller and the RESTful web service controller shown earlier is the way that the HTTP response body is created. Rather than relying on a view technology to perform server-side rendering of the greeting data to HTML, this RESTful web service controller populates and returns a Greeting object. The object data will be written directly to the HTTP response as JSON.

# METER OS LINKS EM VEZ DESTE TEXTO TODO

