# GBCE Super Simple Stock Market Service
A Service representing operations in the Trading System of Global Beverage Corporation Exchange.

## Project Description

### Requirements

	1.	For a given stock:
	    
		i.    Given any price as input, calculate the dividend yield
		ii.   Given any price as input, calculate the P/E Ratio
		iii.  Record a trade, with timestamp, quantity, buy or sell indicator and price
		iv.   Calculate Volume Weighted Stock Price based on trades in past 5 minutes
	
	2.	Calculate the GBCE All Share Index using the geometric mean of prices for all stocks

### Constraints & Notes

	1. Written in the language - Java
	2. The source code runs as a service and can be executed via unit tests. 
	3. Maven is used as Build tool and TestNG for unit tests. 
	3. No database, GUI or I/O is required, all data need only be held in memory.
	4. All formulas for calculations of financial numbers in the requirements are provided.
	5. The code provides only the functionality requested. It implements the solution as a Service. 
	6. The solution is designed to be extensible and can be exposed as Web Service / Web application / REST APIs  

### Sample data from the Global Beverage Corporation Exchange

	Stock Symbol  | Type 		| Last Dividend | Fixed Dividend 	| Par Value
	------------- | ---- 		| ------------: | :------------: 	| --------: 
	TEA           | Common    	| 0  			|    				| 100
	POP           | Common    	| 8  			|    				| 100
	ALE           | Common    	| 23 			|    				| 60
	GIN           | Preferred 	| 8  			| 2% 				| 100
	JOE           | Common    	| 13 			|    				| 250


## How to use

	This is a maven project. This can be run using the Unit tests included. 
	* mvn test -> to execute the unit tests (and the application) 
	
	This can be packaged as a JAR and can be used as a Library/Service:
	* mvn package -> to generate the JAR

## Solution Design

#### 3-Tier Architecture
	
The application tries to follow the standard 3 Tier JEE architecture. As per one of the constraints, the UI (Presentation Layer) is not included. But other layers are created i.e. Service layer and the DAO layer. All data is held in memory. No database is used. A simple data store is created using Maps. There are no explicit integration requirements provided. So, the simplest decision was to create a java library, which could be integrated in all other JAVA technologies and architectures. 

The design is flexible and extensible. It can be integrated with an existing web application, or can be published as web service or REST APIs to be consumed by external systems.

#### Class Diagram

The class diagram for the solution is available at : <project root>/resources/diagram/GBCEStockMarket_ClassDiagram.png

## Implementation 

#### Project Management / Build Framework

The project was planned to be implemented as a JAR/Library. I chose Maven for its easy project management and build capabilities. I started off with creating a maven project integrated in Eclipse IDE with archetype as - "maven-archetype-quickstart".

#### Application Framework

The application must follow OO Principles. To implement a cohesive and loosely coupled structure (Trying to follow SOLID principles) I chose to use Spring Framework for the Application context and Dependency Injection. Added Spring 4 dependencies in Maven POM.xml 

#### Unit Test Framework

Trying to follow TDD approach I chose to use TestNG framework as it provides many features to make testing more powerful and easier to do. Added TestNG dependencies in POM.xml. Created a testng.xml file to create a test suite to include all my test classes. Configured path to testng.xml (src/test/java/resources) in POM file using maven-surefire-plugin.

#### Logging Framework

As a better option to Log4J in terms of configuration and faster implementation, I chose to use Logback SLF4j for logging purpose. By default, Spring uses the Jakarta Commons Logging API (JCL). So to integrate Logback with spring I made following changes in POM.xml:
	1. Excluded commons-logging from spring-core 
	2. Bridge the Spring’s logging from JCL to SLF4j, via jcl-over-slf4j
	3. Included logback as dependency
	4. Created a logback.xml in the src/main/resources folder

#### Coding

I started with creating a Test class - StockServiceTest to write test cases for the Stock Service application. To utilize Spring's Dependency Injection in TestNG unit test i have to extend StockServiceTest class from AbstractTestNGSpringContextTests. Also using Spring 4 feature of Java based context configuration, I created StockMarketTestSpringConfig class to hold bean definitions. I created a bean to hold a Map with Stock data already provided in the requirements.

To implement the Stock Service requirements, I started with test case to record a trade into the trading system. Added an interface reference StockService as class member in my test class. Configured with @Autowired to let Spring inject the actual object at runtime with its Auto Discovery mechanism. In the testRecordTrade() method, used StockService to call stockService.recordTrade(trade) API. Got compilation errors and to fix that I used Eclipse quick fix feature to add missing method declaration in StockService Interface. Also created an implementation class - StockServiceImpl for this interface. Following the compilation errors I created classes for Trade and Stock to represent Trade-Has-A-Stock relationship. With the initial blank implementation of the required classes in this test method, I ran the Test class - StockServiceTest, using Eclipse-TestNG plug-in. Followed this with adding method definition for recordTrade(Trade trade) into StockServiceImpl class. Switching back and forth between test method and the implementation method, concluded with a partially successful feature. The Service method was ready but, DAO layer with an in-memory data store was remaining.

With the same approach as above, I created Test classes - TradeDaoTest and StockDaoTest to create the DAO layer via test cases. The interface and implementation of StockDao and TradeDao followed. Adding ConcurrentHashMap instances inside the the DAO classes I created an in-memory data store to hold Trade and Stock data.

Similarly created other test methods for each feature in the requirements and running each test class to fill the missing logic implementation of the features. Added all Test classes in testng.xml as test Suite. With maven test goal i could see the entire project being compiled and tested with the TestNG test suite. 

I followed the syntax errors shown by eclipse and test errors in maven test & build process to complete all features of the application.

The final artifact for this project is generated as "GBCEStockmarket-1.0.jar" by running maven goal : mvn package


