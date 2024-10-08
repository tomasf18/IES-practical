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