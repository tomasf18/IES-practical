# 1
alterei versoes de dependencias por causa de vulnerabilidades mas acabei por retomar às antigas, saltando o #3.4
como a gestão do server era feito na propria app, apenas tive de copiar os programas dados no enunciado
curl http://127.0.0.1:8680/?msg=”Hard workers welcome!”

# 2
aqui ja passamos para algo mais "profissional": um server dockerizado (i.e., not embedded servers)
There are several production-ready application servers you can choose from
we will consider Apache Tomcat

The process for creating, deploying, and executing a Jakarta Servlet-based web application is different from that of Java classes which are packaged and executed as a Java application archive (JAR). It can be summarized as follows:


    Develop the web component code.

    Develop the deployment descriptor files, if necessary.

    Compile the web component code against the libraries of the servlet container and the helper libraries, if any.

    Package the compiled code along with helper libraries, assets and deployment descriptor files, if any, into a deployable unit, called a web application archive (WAR).

    Deploy the WAR into a servlet container.

    Run the web application by accessing a URL that references the web component.



supostamente vou ter de 
1- criar a web app
2- dockeriza la
3- corre la
4- fazer um curl do tipo: curl http://127.0.0.1:8080/my-jakarta-webapp/hello?msg="Mensagem a ser apresentada"

# 3

The **Spring Framework** is a comprehensive framework for building enterprise-level Java applications. It provides a wide range of functionalities to help developers handle common tasks in application development, such as dependency injection, data access, transaction management, and security.

Here’s a breakdown of what the **Spring Framework** offers:

### 1. **Dependency Injection (DI):**
   - Spring’s core feature is **Inversion of Control (IoC)** via Dependency Injection, where objects are given their dependencies instead of creating them manually. This makes the code more modular and easier to test.

### 2. **Aspect-Oriented Programming (AOP):**
   - Spring offers AOP to manage cross-cutting concerns (like logging, security, etc.) separately from your business logic.

### 3. **Transaction Management:**
   - Spring abstracts the complexity of transaction management and integrates with different data management technologies, making it easy to handle transactions declaratively.

### 4. **Data Access:**
   - Spring provides templates to work with databases (like JdbcTemplate, HibernateTemplate), and support for Object-Relational Mapping (ORM) frameworks such as Hibernate and JPA.

### 5. **Spring MVC:**
   - A part of the Spring Framework is **Spring MVC**, which is used for building web applications. It follows the Model-View-Controller (MVC) design pattern, simplifying the development of web interfaces.

### 6. **Security and Testing:**
   - **Spring Security** is a powerful framework for handling authentication and authorization. **Spring Test** simplifies writing unit and integration tests.

---

### **Spring Boot:**

**Spring Boot** is an extension of the Spring Framework that simplifies the process of building Spring applications. While the Spring Framework provides a lot of power and flexibility, it requires a lot of setup and configuration. Spring Boot removes much of this boilerplate by providing pre-configured setups.

Key features of **Spring Boot**:

### 1. **Auto-Configuration:**
   - Spring Boot can automatically configure your application based on the dependencies you have included. For instance, if you include a web dependency, Spring Boot will automatically set up a web server.

### 2. **Embedded Servers:**
   - Spring Boot applications can run with embedded web servers like **Tomcat** or **Jetty**, meaning you don’t need to deploy your application to an external server—it runs as a standalone application.

### 3. **Convention Over Configuration:**
   - While Spring requires explicit configuration for many things, Spring Boot follows a convention-over-configuration approach, meaning it provides sensible defaults and requires minimal configuration.

### 4. **Starter POMs:**
   - Spring Boot provides **starter dependencies**, which are pre-configured collections of dependencies. For example, if you want to build a web application, you can simply add `spring-boot-starter-web` to your project, and it will pull in everything you need.

### 5. **Spring Boot Actuator:**
   - A powerful feature that provides built-in monitoring and management capabilities for your application. It helps in exposing information like health checks, metrics, and environment details.

### 6. **Simplified Deployment:**
   - Spring Boot applications can be packaged as standalone **JAR** files, which include everything needed to run the application (server, libraries, etc.). This makes deployment easier as it eliminates the need for a separate application server.

---

### Summary:
- **Spring Framework** is the foundational framework for enterprise Java development, offering many tools for different kinds of applications (web, database, security, etc.).
- **Spring Boot** simplifies the development process by automating much of the configuration, providing embedded servers, and offering easy deployment options. It's designed to help you get started quickly with Spring applications.



# Exatamente como em Flask, só que para Java
In Spring’s approach to building web sites, HTTP requests are handled by a controller. You can easily identify the controller by the @Controller annotation. In the following example, GreetingController handles GET requests for /greeting by returning the name of a View (in this case, greeting). A View is responsible for rendering the HTML content. The following listing (from src/main/java/com/example/servingwebcontent/GreetingController.java) shows the controller:
package com.example.servingwebcontent;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

	@GetMapping("/greeting") // handle GET do endpoint /greeting (quando o user visita este endpoint, a página devolvida por este método apresenta se no ecrã)
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting"; // html page
	}

}

The @GetMapping annotation ensures that HTTP GET requests to /greeting are mapped to the greeting() method.

@RequestParam binds the value of the query string parameter name into the name parameter of the greeting() method. This query string parameter is not required. If it is absent in the request, the defaultValue of World is used. The value of the name parameter is added to a Model object, ultimately making it accessible to the view template.

### Thymeleaf == JANGO!!!!
The implementation of the method body relies on a view technology (in this case, Thymeleaf) to perform server-side rendering of the HTML. Thymeleaf parses the greeting.html template and evaluates the th:text expression to render the value of the ${name} parameter that was set in the controller.The following listing (from `src/main/resources/templates/greeting.html`) shows the greeting.html template:
```html
...
<body>
    <p th:text="|Hello, ${name}!|" />
</body>
</html>
```



A common feature of developing web applications is coding a change, restarting your application, and refreshing the browser to view the change. This entire process can eat up a lot of time. To speed up this refresh cycle, Spring Boot offers with a handy module known as `spring-boot-devtools`. Spring Boot Devtools:

    Enables hot swapping.

    Switches template engines to disable caching.

    Enables LiveReload to automatically refresh the browser.

    Other reasonable defaults based on development instead of production.


Run the Application

The Spring Initializr creates an application class for you. In this case, you need not further modify the class provided by the Spring Initializr. The following listing (`from src/main/java/com/example/servingwebcontent/ServingWebContentApplication.java`) shows the application class:



@SpringBootApplication is a convenience annotation that adds all of the following:

    @Configuration: Tags the class as a source of bean definitions for the application context.

    @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings. For example, if spring-webmvc is on the classpath, this annotation flags the application as a web application and activates key behaviors, such as setting up a DispatcherServlet.

    @ComponentScan: Tells Spring to look for other components, configurations, and services in the com/example package, letting it find the controllers.

The main() method uses Spring Boot’s SpringApplication.run() method to launch an application. Did you notice that there was not a single line of XML? There is no web.xml file, either. This web application is 100% pure Java and you did not have to deal with configuring any plumbing or infrastructure.


Build an executable JAR

You can run the application from the command line with Gradle or Maven. You can also build a single executable JAR file that contains all the necessary dependencies, classes, and resources and run that. Building an executable jar makes it easy to ship, version, and deploy the service as an application throughout the development lifecycle, across different environments, and so forth.




If you use Maven, you can run the application by using ./mvnw spring-boot:run. Alternatively, you can build the JAR file with `./mvnw clean package` and then run the JAR file, as follows:

java -jar target/gs-serving-web-content-0.1.0.jar


**Logging output is displayed.**

Now that the web site is running, visit http://localhost:8080/greeting, where you should see “Hello, World!”

Provide a name query string parameter by visiting http://localhost:8080/greeting?name=User. Notice how the message changes from “Hello, World!” to “Hello, User!”:

This change demonstrates that the @RequestParam arrangement in GreetingController is working as expected. The name parameter has been given a default value of World, but it can be explicitly overridden through the query string.


# Add a Home Page for the site root ('/')

Static resources, including HTML and JavaScript and CSS, can be served from your Spring Boot application by dropping them into the right place in the source code. By default, Spring Boot serves static content from resources in the classpath at /static (or /public). The index.html resource is special because, if it exists, it is used as a "welcome page", which means it is served up as the root resource (that is, at http://localhost:8080/). As a result, you need to create the following file (which you can find in src/main/resources/static/index.html):

When you restart the application, you will see the HTML at http://localhost:8080/.


### RESTFUL



This controller is concise and simple, but there is plenty going on under the hood. We break it down step by step.

The @GetMapping annotation ensures that HTTP GET requests to /greeting are mapped to the greeting() method.
There are companion annotations for other HTTP verbs (e.g. @PostMapping for POST). There is also a @RequestMapping annotation that they all derive from, and can serve as a synonym (e.g. @RequestMapping(method=GET)).

A key difference between a traditional MVC controller and the RESTful web service controller shown earlier is the way that the HTTP response body is created. Rather than relying on a view technology to perform server-side rendering of the greeting data to HTML, this RESTful web service controller populates and returns a Greeting object. The object data will be written directly to the HTTP response as JSON.

# METER OS LINKS EM VEZ DESTE TEXTO TODO

