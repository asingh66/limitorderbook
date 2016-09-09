# Limit Order Books

###Design an implement a REST API in Java that provides the functionality of a Limit Order Book.
###Use the existing Spring dependencies (like Web, JPA , Spring Boot) provided in the Maven pom.xml file included in the quickstart
###The following are the REST endpoints (Choose more appropriate endpoints if needed):

####/getOrders    - lists all the orders that have not been executed yet.
####/addOrder     - adds a new order to the Order Book (Pass Stock and Prive info as URL params)
####/cancelOrder  - deletes the order from the Order Book.
####/execute      - picks the top priority order from the order book and executes it 

To read more about Limit Order Books :
        http://www.investopedia.com/terms/l/limitorderbook.asp
        https://en.wikipedia.org/wiki/Central_limit_order_book




Please use an in-memory RDBMS like H2 or HSQL .

To run the API, you'll use the following command. More information about testing and running Spring Boot applications can be found in the Spring documentation online.

    mvn spring-boot:run   
    
You will have 48 hours to submit your solution.	

-------------------
#### Expectations

The basic expectation of this assignment is that you will use Spring Boot and Spring Web to build a RESTful API -- you may want to take a look at this guide if you haven't used the Spring Java Config annotations to build web services in the past: https://spring.io/guides/gs/rest-service/

1. The API should use an in memory relational database, such as H2 or HSQL 
2. It should take advantage of Spring Boot's embedded servlet container to run and test the resources.
3. It does NOT need to provide any security or authentication
4. It should use Maven for dependency and build management
5. It should follow common Java best practices in terms of documentation, style, and use of collections and design patterns
6. It should use JDK version 1.7 or 1.8.
7. Some basic exception handling should be provided, including use of standard HTTP status codes to indicate when a query parameter is unacceptable, for example
8. To test out the API you can use Postman App (www.getpostman.com).

--------------------
#### General Advice

If you feel that any information is missing or unclear, then please make a choice that makes sense for you and provide the reason of your choice, either in the code or in an attached document. 



##############Original ReadMe Ends Here##############
##############Amit Singhs Comments###################

Notes:
- Your server is running on localhost at port 8080, please change the urls in the notes below if the address and ports are different
- Git repository for the project is https://github.com/asingh66/limitorderbook.git
- URL of application is http://localhost:8080/capone/api/orders
- Order quantity and price cannot exceed 100000, the price is rounded off to 2 decimal places - 10.116 will be rounded to 10.12
- capone.order.matchoncreate flag is provided in application.properties file. When set to true, any order created and added to OrderBook
  gets matched automcatically. If set to false, the order gets created and added to OrderBook but matching is not done
- Only Limit order are added to OrderBook, Market orders are not added, if the Market order placed is not matched, it goes into      NOT_MATCHED status.
- If while matching, the quanitites of two orders are different, the order will go into PARTIAL_MATCH state but will be available for future matches. For example BID of price 100 and quantity 50 when matched to ASK of price 90 and quantity 40 will result in a partial match which means BID will go into PARTIAL_MATCH status with execution_quantity of 40. ASK will be EXECUTED completely. 10 remaining BID orders will be available for any future matches.
-OrderBook is kept in memory. 
    Every symbol has its own OrderBook. 
    There is a map of OrderBooks with symbol as key.
    Bids and Asks are kept as TreeMap in OrderBook. key is the price, value is a LinkList of TradeOrders ordered by time of creating.
    Bid treemap is sorted decending by price.
    Ask treemap is sorted ascending by price.

****************************************************************************************************************************************

----------------------------------------------------------------------------------------------------------------------------------------
REST APIS - the API docs can be accessed at http://localhost:8080/capone/swagger-ui.html 
----------------------------------------------------------------------------------------------------------------------------------------
URL capone/api/orders
Method: POST
Parameters: price (1 to 100000D), quantity (1 to 100000), symbol, orderType (BID or ASK), orderCategory(MARKET or LIMIT)
Function - 1. creates a new order in the system, order status is NEW
           2. If order is LIMIT - adds the order in OrderBook
           3. Executes the order in OrderBook
           4. If the order is fully executed, changes the order status is EXECUTED, removes it from OrderBook. If the order is partially                 executed, changes the order
              status to PARTIAL_MATCH
           5. If the order is MARKET and there is no match in step 4, moves the order to NOT_MATCHED status
           6. If order is executed, creates a OrderExecutionBatch and one or more OrderExecutionRecords for the BID and ASK pair
HTTP Codes: 201 - If the order is succesfully created, location of is sent in the header.            
----------------------------------------------------------------------------------------------------------------------------------------
URL capone/api/orders
Method: Get
Parameters: none
Function - returns all orders which are either in NEW or PARTIAL_MATCH state
----------------------------------------------------------------------------------------------------------------------------------------
URL capone/api/orders/{id}
Method: Get
Parameters: id of order
Function - returns the order details of input order id
HTTP Codes: 404 - if order number is not present in system
            200 - if order found, order details sent in the body

----------------------------------------------------------------------------------------------------------------------------------------
URL capone/api/orders/{id}/cancel
Method: Put
Parameter: order id
Function: Updates the order to Cancel state. Removes the order from OrderBook if its a Limit Order.
HTTP Codes: 404 - if order number is not present in system
            204 - if order was succesfully executed
----------------------------------------------------------------------------------------------------------------------------------------
URL capone/api/orders/execute
Method: Post
Parameter: NONE
Function: Match all the orders in OrderBook
HTTP Codes: 200 ok
----------------------------------------------------------------------------------------------------------------------------------------
URL capone/api/orders/execute/{symbol}
Method: Post
Parameter: Stock Symbol 
Function: Match all the orders of the provided stock symbol in OrderBook
HTTP Codes: 200 ok

****************************************************************************************************************************************
----------------------------------------------------------------------------------------------------------------------------------------
Database 
----------------------------------------------------------------------------------------------------------------------------------------
In memory H2 database is used. The database can be accessed from http://localhost:8082
JDBC_URL: jdbc:h2:mem:testdb
username: sa
password:(blank)

Tables
1. Trade_Order - table to store all bids and ask. Stores order status, quantity of original orders, executed_quantity, symbol, price etc
2. Order_Execution_Record - record of ask and bid matches. If there are more than one matches for an order, they are all tied together using execution_id which is a foreign key of Order_Excution_Batch
3. Order_Excution_Batch - used to keep track of order executions and grouping them together

****************************************************************************************************************************************
JUnit test cases
A few test cases have been provided only for testing rest calls, they are not compreheansive. 
No service layer test cases have been created due to time restriction.
Will need many more TestCases


