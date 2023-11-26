# java-servlet-hello
/getCustomersd web application with Maven + Java + Servlets + Tomcat

## Compile app 
```
mvn clean install
```
The compiled file will been stored in `target` folder as `task3.war`

## Run app 
```
mvn tomcat7:run
```
The servlet can be executed in browser by path `http://localhost:8081/hello`

## Run in docker
After compile app just run next
```
docker run -i --rm --name task3-app -p 8080:8080 \
  -v ${PWD}/target/hello.war:/usr/local/tomcat/webapps/taks3.war \
  tomcat:9.0-jre8-alpine
```

And for opening the Servlet run in browser `http://localhost:8080/getCustomers'

To create or validate data in db run `http://localhost:8080/validateData'
