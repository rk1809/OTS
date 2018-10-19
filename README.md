OTS PROJECT:
	I have implemented a Web Based Oil Transaction System which can serve three types users i.e. Client, Trader, and Manager. A client can buy/sell oil directly, depending on his current oil reserves, or contact a trader to perform 
these transactions for him. Based on each transaction, client pays commission to the company in form of either oil or cash.
	There are certain levels are available for clients based upon their purchase. It starts with SILVER and changes to GOLD, if they purchase 30 barrels in a single month. Commissions are applied as per their levels and their oil reserves or account balance are adjusted accordingly.
	OTS database keeps track of all the records of each transaction. Client can pay money to the company from time to time to settle their balances. A Trader can cancel certain payments and oil transactions, but these cancellations are logged for audit purposes. Payments and cancellations must go through traders only, not through anyone else. 
	A manager(administrator) is also a trader and has all functionalities available to a trader. Additionally, a manager can also view reports which gives him a brief analysis of various trades of the OTS system.
	
Application Architecture Overview:

Spring MVC: 
It is a three-layered architecture that helps in separating the requirements into 3 layers. 

Controller Layer:
Is used to accept the data from front end and vice versa.

Service Layer:
 	Is used to perform business logic
	
Data Access Object Layer:
	Is used to perform data access job from Database.

Spring JDBC templates.
	Spring JDBC automatically creates a connection to the Data source defined in ots-servlet.xml and executes prepared statement as called.It creates the automatic connection to the data source as per the ots-servlet.xml and executes the prepared statements accordingly.

MySQL:
	MySQL is used as a backend DB.
Frontend:
 	JSP, HTML5, jQuery, JSP EL
	These technologies were used in order to render the dynamic data received from backend and also for sending back.
Bootstrap CSS:
	Is used mainly for the look and feel of the website.
Server: 
	Tomcat 8
Development Environment:  
	MySQL, Eclipse, Maven.
Source control management: 
	GIT


