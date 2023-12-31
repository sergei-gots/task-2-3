# task 2.3 /getCustomers
Web application with Maven + Java + Servlets + Tomcat

## Specification

To implement a query to load clients from the database.
There is a customer database with their data of the following types:
The "customer" table is the central one:

```
customer (
    customerId VARCHAR(200) NOT NULL UNIQUE,
    col_1 SOME TYPE,
    ...
    col_N SOME TYPE
)
```
Several dozens O(1) (for example, 30) tables with a 1-to-1 relationship to customer data:

```
table_i (
    customerId VARCHAR(200) NOT NULL UNIQUE REFERENCES customer (customerId),
    col_1 SOME TYPE,
    ...
    col_N SOME TYPE
)
```
And one table with a 1-to-M relationship about the groups a customer belongs to:

```
table_many (
    customerId VARCHAR(200) NOT NULL REFERENCES customer (customerId),
    groupId INT NOT NULL
)
```

It is known that the number of groups a customer belongs to is not very large - O(1) (consider a maximum of several dozen).

In this task, it is assumed that there are no long columns like TEXT, arrays, etc., with a large amount of data in them.

The task is to implement a query to load clients with their data and groups (all data from the tables mentioned earlier, including all groups). It is necessary to create a /getCustomers router that accepts an offset parameter (in the JSON body) to load the next portion (5000) of clients.

In this task, performance must be taken into account since the number of clients can be from 300,000 to 1,000,000. What to choose as an offset? Implementing authentication is not required. It is required to implement the delivery of a block of 5,000 clients with data within 2-4 seconds on your PC. This means that the PC can be assumed to have only one core, not a powerful server.

## Compile app 
```
mvn clean install
```
The compiled file will be stored in `target` folder as `task3.war`

## Run app 
<br>
Before you run the app be sure you have appropriate database properties in the <a href="https://github.com/sergei-gots/task-2-3/blob/master/src/main/resources/db.properties">/resources/db.properties</a>
<br>To run the tomcat server and the /getCustomer application which will be deployed out there use:
```
mvn tomcat7:run
```

The servlet can be executed in browser by path <a href="http://localhost:8080/task-2-3/start">http://localhost:8080/task-2-3/start</a>
This is the start page. There are possibilities to validate data in the database and use /getCustomers service
end-point is available by path  `http://localhost:8080/task-2-3/getCustomer'


And for opening the Servlet run in browser `http://localhost:8080/task-2-3/getCustomers'

### Data validation

In order to create or validate data in db run `http://localhost:8080/task-2-3/validateData'.
Validation is implemented here in order to have possibiltiy to check and fix the next cases:  <br>
<li>if there are all the used tables created and creates them if it necessarty
<li>checks that customers count in the table customer is not less than it is required and creates the missing count of customers creating all the depending data in all the rest tables.
<br>One can run validation data in cases:<br>
<li> The first start on the server when there is no data in the db
<li> if the count of customers is less than required (it is defined as a constant: 

<b>org.task2.task23.service.DbConstants.CUSTOMER_COUNT_BY_DEFAULT = 500_000;</b>

<li> some tables are missing. Note that is not a customer table, there won't be any additional data entries created

