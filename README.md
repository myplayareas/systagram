# SysTagram
Aplicação para postar fotos e receber comentários e likes. 

Features
---

1. Controle de autenticação (Login/Logout)
2. Dashboard de usuário de acordo com sua permissão
3. Controle de permissões (Admin/User)
4. Controle de uploads de arquivos
5. Gerenciar Fotos
6. Gerenciar Posts 
7. Gerenciar Comentários nos posts
8. Gerenciar Likes nos posts

Sobre as operações para execução da aplicação
---

O branch principal contem a aplicação apontando para os serviços da AWS. 

O branch likes contem a aplicação rodando em um ambiente local autocontido para testar as funcionalidades da aplicação em um ambiente local offline. 

Caso você queira publicar na sua infraestrutura ASW basta alterar o protótipo com os seguintes passos: 

1. Crie uma instância do Ubuntu 18 no EC2 com as portas 80, 22, 8080 liberadas para qualquer IP.

1.1 Lembre-se de instalar o jdk e o maven para poder compilar o código da aplicação.

1.2 Faça o clone do repositório do systagram. 

2. Crie uma instância, preferencialmente na mesma VPN do EC2 do passo1, do RDS com o MariaDB. Lembre-se de liberar as portas 3306 e 22 para qualquer IP ter acesso. 

2.1 Crie um banco de dados chamado dbsystagram. 

3. Entre na instância do EC2 do item 1 e conecte na instância do RDS do item 2. 
Exemplo: 
```
$mysql -u root -p -h mydbsystagram.x.y.rds.amazonaws.com
```
Restaure o banco rodando o script restaura-dbsystagram.sql no RDS
```
>use dbsystagram;
>source scripts/sql/restaura-dbsystagram.sql
```
3.1. Na instância do EC2 altere o arquivo application.properties para apontar para o novo banco restaurado do RDS

Obs: a configuração do root dever ser a mesma do application.properties disponível em systagram/src/main/resources/application.properties

4. Crie um bucket no S3 com a seguinte estrutura: 
nome-do-bucket
|-users
|-uploads
|--pictures

Obs: Essas pastas do bucket devem ter acesso de leitura para todos os usuários.

5. Copie o conteúdo dos diretórios users, uploads/pictures do repositório do systagram para os referidos diretórios do bucket criado no item 4. Com isso, os usuários e as figuras já salvas no protótipo vão aparecer quando a aplicação for iniciada on-line.

6. Antes de compilar a aplicação é preciso atualizar a classe Constantes.java com o id, chave de acesso e também com o nome do bucket criado no item 4. 

Obs: atualize os seguintes dados na classe Contantes (systagram/src/main/java/br/ufc/great/sysadmin/util/Constantes.java): 

public static String access_key_id = "?";
public static String secret_key_id = "?";
public static String s3awsurl = "https://s3.amazonaws.com/systagram-uploads/";
public static String bucketPrincipal = "systagram-uploads"; 

7. Compile as classes com o maven a partir do diretório raiz do repositorio systagram: 
```
$ mvn clean
$ mvn test
```
8. Execute a aplicação principal do Spring boot: 
```
$mvn spring-boot:run
```
Recomendo fazer os testes do protótipo em uma janela privada do browser, pois estou enfrentando alguns problemas para gerenciar as sessões dos usuários. 

teste: http://IP-DA-INSTANCIA:8080

Characteristics
---

* Spring Boot;
* Spring Security for basic login with permissions;
* Thymeleaf para view;
* Mysql Database or others;
* Basic entity crud;
* AWS (Amazon Web Service)
* EC2 (Elastic Compute Cloud)
* RDS (Relational Database Service)
* S3 (Simple Storage Service)
* DynamoDB (NoSQL database service)

TODO
---

* Search in the listing;
* Model of Dialog;
* Template for sending e-mail with template;

About Spring-boot packaging
---

1. Adding Classpath Dependencies
```
$mvn dependency:tree
```

2. Running the Example
Since you used the spring-boot-starter-parent POM, you have a useful run goal that you can use to start the application
```
$mvn spring-boot:run
```
3. How to test, execute and package the application?
You have to put Classpath Dependencies
```
$mvn dependency:tree
```
3.2. If you want to run the example directly from main path source code
```
$mvn spring-boot:run
```
3.3. If you want to create .jar package application. 
The packaget application .jar are archives containing your compiled classes along with all of the jar dependencies that your code needs to run.
```
$mvn clean package
```
3.4 To run that application, use the java -jar command, as follows:
```
$java -jar target/artefactId-version.jar
```
For further details click the link below to read full article about spring-boot packaging: 
https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started-first-application.html#getting-started-first-application-pom

Special Configurations
---
For database security, datasource, jpa, thymeleaf and session configuration you have to change values in src/main/resources/sql/security.sql and src/main/resources/application.properties

References
---

[1] Spring MVC 4. Java Framework for MVC Web Applications. Available at https://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html

[2] Spring Boot 1. It is a Java Framework (based on the Spring Platform) for web applications that use inversion control container for the Java platform. Available at https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-security

[3] Thymeleaf. It is a Java / XHTML / HTML5 Java model engine that can work in both web (servlet-based) and non-web environments. It is best suited to serve XHTML / HTML5 in the MVC-based web application preview layer. Available at https://www.thymeleaf.org

[4] Bootstrap. Vision layer framework for responsive web applications. Available at https://v4-alpha.getbootstrap.com/getting-started/introduction

[5] JQuery. JavaScript Function Library. Available at https://jquery.com

[6] ORM JPA. Abstartion of data access. Available at https://docs.spring.io/spring-data/jpa/docs/current/reference/html

[7] Spring Security. It is a Java framework that provides an access control framework for Java / Java EE applications that provides authentication, authorization, and other security features for enterprise applications. Available at https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle

[8] Maven. Management of Builds and Dependencies. Available at https://maven.apache.org

[9] Mysql 5. Database Management System. Available at https://dev.mysql.com/downloads/mysql

[10] AdminLTE. Control panel template for web applications. Available at https://adminlte.io/themes/AdminLTE/index.html

[11]. AWS. Amazon Web Service. Available at https://aws.amazon.com/

Questions, suggestions or any kind of criticism contact us by email armando@ufpi.edu.br
