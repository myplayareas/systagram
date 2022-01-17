# SysTagram
Aplicação para postar fotos e receber comentários e likes. 

![License](https://img.shields.io/badge/License-BSD%202--Clause-orange.svg) 

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

teste: http://IP-DA-INSTANCIA


Garantindo que a aplicação será iniciada como um serviço
---
Para executar o serviço ao iniciar o servidor (Ubuntu): 

1. Crie como root o arquivo rc.local em /etc
```
/etc/rc.local
```

2. Edit o arquivo rc.local via "sudo vim rc.local" com o seguinte conteudo
```
#!/bin/sh -e
cd /home/ubuntu/tsd/systagram/
sh /home/ubuntu/tsd/systagram/scripts/executa.sh || exit 1
exit 0
```

3. Altere o grupo e as permissões do arquivo rc.local para o grupo root e permissão de leitura
```
sudo chown root /etc/rc.local
sudo chmod 755 /etc/rc.local
```

4. Edite o serviço rc-local.service
```
sudo vi /etc/systemd/system/rc-local.service
```

5. Coloque o seguinte conteudo no rc-local.service
```
[Unit]
 Description=/etc/rc.local Compatibility
 ConditionPathExists=/etc/rc.local

[Service]
 Type=forking
 ExecStart=/etc/rc.local start
 TimeoutSec=0
 StandardOutput=tty
 RemainAfterExit=yes
 SysVStartPriority=99

[Install]
 WantedBy=multi-user.target
```

6. Habilite o serviço re-local via systemctl
```
sudo systemctl enable rc-local.service
```

7. Por fim, faça os testes para checar se o serviço inicia e veja seu status via: 
```
sudo systemctl start rc-local.service
sudo systemctl status rc-local.service
```

Observações: 
- Para tirar a prova dos 9 reinicie o servidor e veja se o serviço realmente iniciou. 
- Caso queira alterar o comportamento do script basta alterar os comandos do script /home/ubuntu/tsd/systagram/scripts/executa.sh


Criando um Esquema de Load Balancing e Auto Scaling na AWS
---

Para criar um esquema de Load Balancing e Auto Scaling os seguintes passos devem ser seguidos para criar um Balanceador iniciando com 2 instâncias do systagram e depois adicionando mais instâncias na medida que o consumo de processador superar 80%. Sendo para este exemplo o limite máximo de instância é de quatro instâncias. 

1. Crie uma imagem do Systagram de acordo com a seção "operações para execução da aplicação".

2. Crie o Load Balance via EC2 Dashboard -> Load Balancing -> Load Balancers 

2.1 Faça a criação padrão, mas configure o controle de sessão do balanceador
```
Port Configuration
80 (HTTP) forwarding to 80 (HTTP)
Stickiness: LBCookieStickinessPolicy, expirationPeriod='300'
```

3. Crie o Launch Configuration padrão 

4. Crie o Auto Scaling baseado na imagem criada no passo 1. Faça a linkagem com o balanceador de carga criado no passo 2. Nesse caso o auto scaling vai gerenciar de 2 a 4 instâncias baseado no consumo do processador. 

5. Faça o teste do DNS do Balanceador criado no passo 2.  

A versão de produção do Systagram foi inicialmente publicada em http://s3.amazonaws.com/systagram/index.html essa página aponta para o Balanceador de Carga publicado, e este redireciona as requisições para as instâncias ativas do Systagram. Caso queira fazer um teste no ambiente de produção basta criar um usuário comum na própria aplicação ou enviar um e-mail para armando@ufpi.edu.br

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
* Balanceamento de Carga via Load Balancing da AWS
* Elasticidade (variando de 2 a 4 instâncias) via Auto Scaling da AWS

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
