OTS PROJECT:
	I have implemented a Web Based Oil Transaction System which can serve 

three types users i.e. Client, Trader, and Manager. A client can buy/sell oil 

directly, depending on his current oil reserves, or contact a trader to perform 

these transactions for him. Based on each transaction, client pays commission to 

the company in form of either oil or cash.
	There are certain levels are available for clients based upon their 

purchase. It starts with SILVER and changes to GOLD, if they purchase 30 barrels 

in a single month. Commissions are applied as per their levels and their oil 

reserves or account balance are adjusted accordingly.

	OTS database keeps track of all the records of each transaction. Client 

can pay money to the company from time to time to settle their balances. A 

Trader can cancel certain payments and oil transactions, but these cancellations 

are logged for audit purposes. Payments and cancellations must go through 

traders only, not through anyone else. 
	
A manager(administrator) is also a trader and has all functionalities 

available to a trader. Additionally, a manager can also view reports which gives 

him a brief analysis of various trades of the OTS system. 


Assumptions
:
	Client cannot pay money directly and it has to be come through a trader 	

only - since the requirement states that trader information must be 	recorded 

for each payment.

	
Clients has to settle the balances time to time and are not allowed to 		

make partial payments.


	Total oil quantity-initial value starts from zero.


	Oil prices will be downloaded through an external downloader service 		

(Enhancement) that would insert the data into the database by logging 		

onto the system using user “oil_price_loader”
.

	Client is not allowed to cancel their order directly, they much contact 	

trader/administrator for cancelling their order.


	Payment cannot be completely cancelled - Only orders can be cancelled.
•		

	An administrator can add another administrator.


	An Administrator is a Trader with higher privileges.


	There is no cancellation fee.


Security Measures Taken:

	For Login, Post form has been provided, so that credentials are not 

visible in the webpage.

	A different user id has been created for the utility that downloads the 

oil price data into the system (userName- oil_price_loader). This user ID has 

not been given access to any tables apart from oil_prices.
All IDs are stored as 

UUID instead of sequence number in order to avoid predictability of the ID. 
	Some technical users tend to play with the Rest API calls and it is 

important that we make all Id column values unpredictable.

	Prepared statements have been used throughout the application in order 

to avoid SQL injection attacks.
Instead of defining the new page for accepting 

credit card information, a third party which performs the credit card 

transaction through https, called “Stripe” was used. 
	Stripe exposes an API that allows the integrator to charge users without 

storing the information on their end.


Application Authorization

Role, Feature, Role_has_features tables have been created in order to implement 

Access control on all the features that application has.


Roles Added 

ADMIN

CLIENT

TRADER

Features/Authorization added:


FEATURE_INSERT_USER(Admin)

FEATURE_EDIT_PROFILE (admin,Trader,Client)

FEATURE_CANCEL_ORDER(Trader,Admin)

FEATURE_ACCEPT_PAYMENT(Trader,Admin)

FEATURE_VIEW_REPORTS(Admin)

Database Authorization

For Access to Database, if user needs external access to database, separate db-

users have been created. 
They have been granted access to only those tables that he/she is supposed to 

access.


Admin - This database user has complete access to all functionalities on all 

tables.

oil_price loader - This database user has restricted access. 

GRANT ALL PRIVILEGES ON ots.oil_prices TO 'oil_price_loader'@'localhost';

trader - This database user has restricted access to only those functionalities 

that trader can perform.


GRANT SELECT ON ots.oil_prices TO 'trader'@'localhost';


GRANT select,update ON ots.users to 'trader@localhost' identified by 

'trader@123';


GRANT select ON ots.feature to 'trader@localhost' identified by 'trader@123';


GRANT select ON ots.role to 'trader@localhost' identified by 'trader@123';


GRANT select ON ots.role_has_features to 'trader@localhost' identified by 

'trader@123';


GRANT select, update, insert ON ots.payments to 'trader@localhost' identified by 

'trader@123';


GRANT select, INSERT, UPDATE ON ots.orders to 'trader@localhost' identified by 

'trader@123';


GRANT select, INSERT, UPDATE ON ots.places to 'trader@localhost' identified by 

'trader@123';


GRANT select,INSERT, UPDATE ON ots.cancels to 'trader@localhost' identified by 

'trader@123';


GRANT select, update ON ots.client to 'trader@localhost' identified by 

'trader@123';

GRANT select, update ON ots.trader to 'trader@localhost' identified by 

'trader@123';

client - This database user has restricted access to only those Tables that can 

be accessed by users of this role.

GRANT SELECT ON ots.oil_prices TO 'client'@'localhost' ;
GRANT select ON ots.role 

to 'client@localhost' identified by 'client@123';

GRANT select ON ots.feature to 'client@localhost' identified by 'client@123';

GRANT select ON ots.role_has_features to 'client@localhost' identified by 

'client@123';

GRANT select ON ots.payments to 'client@localhost' identified by 'client@123';

GRANT SELECT, INSERT ON ots.orders to 'client@localhost' identified by 

'client@123';

GRANT select, INSERT ON ots.places to 'client@localhost' identified by 

'client@123';

GRANT select, update ON ots.users to 'client@localhost' identified by 

'client@123';

GRANT select ON ots.client to 'client@localhost' identified by 'client@123';




Performance:

	Database is one of the most important tiers that is responsible for 

performance of the systems. OTS is not a very a compute heavy application and 

hence Database would definitely be the primary resource on which application’s 

performance will depend. 
Measures taken for Performance of the database are as described below.


Connection: 

	Database connection takes time which is why dbcp connection pooling 

Module was used in order to not have to create a new connection for every query. 

DBCP connection pool of following configuration has been defined

initialSize="3"

maxIdle="5"

maxActive="100"

This means that connection pool will start with a size of three active 

connections and will grow as more and more requests start coming in. 
The system is anticipated to provide approximately 100 concurrent users - 

assuming the total user size of around 5000.


	MySQL by default defines a BTREE index on all primary key, foreign key 

constraints defined for a table. 

	After analyzing all the queries written in application, it was found 

that Search User query will be used by every trader and administrator for 

selecting a user, hence it was important to add this query as well.


Indexes:

	BTREE index has been defined on all table’s primary Keys (MySQL’s 

default behavior)
Index for Select User query -Hash index because Search form 

does not support any search type except “Equal to” search.

	create index search_client USING HASH on users (id, last_name, apt_no, 

street,city, zip_code, phone_no, cell_no, email);
	
B tree index on id column of Orders table has been defined, since this 

index will be used every time we display the order summary page for a user.

	View reports functionality has queries on following columns, hence 

indexes corresponding to the same have been defined. 

	create index quantity_report USING HASH on orders (id, payment_id, 

quantity);

	create index amount_report USING HASH on orders (id, payment_id, 

total_amt);

	create index oil_commission_report USING HASH on orders (id, payment_id, 

oil_adjusted_quantity);

	create index fees_report USING HASH on orders (id, payment_id, 

commission_fees);


Application architecture overview:

Backend Technology Used: 

Spring MVC: 

	Spring Provides MVC Architecture support thus enabling automatic 

transaction of data into ModelAttribute beans. Also, it enables us to use 3 

layered architecture of separating requirement into three layers


Controller Layer:
 
	For Accepting data from Frontend and returning data back.

Service Layer:
 	
	For performing business logic
Data Access 
Object Layer:
 
	For Doing all Data access tasks.
Spring:
JDBC templates.
Spring JDBC 

automatically creates a connection to the Data source defined in ots-servlet.xml 

and executes prepared statement as called.


MySQL:

	MySQL being free as backend database


DBCP for connection pooling:

	Since this will be used by many customers, and traders,connection 

pooling was implemented in order to avoid connection overhead


Frontend Technology used:
 
	JSP, jQuery, HTML5, JSP EL

	These were used in order to render the dynamic data received from 

backend and for sending data to backend

 
CSS, Bootstrap:

	Used for Look and Feel of the Application
Google Charts API
Used for 

displaying several reports that managers can analyze.


Server: 

	Tomcat 8
Development 
Environment:  

	Eclipse, Maven, MySQL, Chrome Debug Tools


Source control management: 

		GIT


	As we can see in the above architecture diagram, JSP, jQuery and CSS are 

primarily used in order to render the frontend of the oil transaction system. 

The presentation layer communicates with the Spring Controller called 

“HomeController”. 
Internally Spring uses Front-Controller pattern in order to 

map these requests to individual mappings. 
	
Controller calls Service layer for executing the business logic 

required. 
	
Service Layer calls Dao Layer for executing Queries and fetching the 

result from database. 
	Request mapping jQuery Script for communicating with Backend

Functions.js

	This JavaScript file has all AJAX calls that are made to the backend for 

sending/returning view and or data to be displayed. The code looks like 

following, with Different URLs, data and request types. 

	Following call makes a POST backend request to “/placeOrder” Request 

mapping written in Home controller and displays the html received in the 

container.

function putOrder () 
	{
$. ajax ({
type: "POST",

	url: "placeOrder",
data: $("#placeOrderForm"). serialize(),
			

success: function(data){
$("#container").html(data);
			}});}

Modules from HomeController

@RequestMapping (value = "/home", method = RequestMethod.GET)

	This method is invoked eveytime when user launches the home page of the 

application. This method checks whether user is logged in and whether user is 

trader or not. It returns view of order summary for Clients where as it returns 

the search page for traders and managers. If user is not logged in, it simply 

presents user with the Login form.


@RequestMapping (value = "/login", method = RequestMethod.POST)

	This method accepts user Id and password and validates the credentials. 

It internally calls UserDaoImpl.getUserDetails and verifies email/Password 

combination by using aes_decrypt method for decrypting the password stored in 

the database.


@RequestMapping (value = "/searchUser", method = RequestMethod.POST)

	This method accepts search form From User and executes an OR search on 

all the fields. Please note that it executes Exact search and returns all 

records present.


@RequestMapping (value = "/selectUser", method = RequestMethod.GET)

	This method is called when Trader/Manager needs to perform certain 

functions like view order summary, balance, accept payment, Cancel order, etc. 

This method sets the user in the session and uses the same for performing all 

user actions
.

@RequestMapping (value = "/insertOrUpdateUser", method = RequestMethod.POST)

	This method is called when user clicks on submit button on create user 

screen. This method validates where both the passwords entered are same or not. 

If complete data is valid, it creates a user by calling UserDaoImpl.INSERT_USER 

method through service. Also, for trader, it creates an entry in trader table, 

and for client, it creates an entry in client table.


@RequestMapping (value = "/cancelOrder", method = RequestMethod.GET)

	This method is called when Trader/Manager cancels specific orders for a 

client. This method performs following logic
Creates an entry in cancels table. 

This entry helps track the trader who performed the cancellation
Updates Account 

information of the user - especially Total Oil quantity and Credit balance. For 

cancelled “Buy Oil” and sell oil transactions.


@RequestMapping (value = "/payment", method = RequestMethod.GET)

	This method is called when user select orders to pay for. The total 

amount is calculated based on orders selected and user is presented a Credit 

card payment screen. Here, user’s existing balance is taken into consideration 

while accepting payment. Stripe - third party payment gateway service has been 

used for accepting user’s credit card information. Once provided, the form gets 

redirected to Stripe form that is setup in Test mode. Accepted credit card 

number to be used is 4242 4242 4242 4242
 

@RequestMapping (value = "/paymentAccepted", method = RequestMethod.POST)

	This method is called when used provides the credit card information. 

This information is then used and an entry is made to the payments table. Also, 

for corresponding orderIds, payment_id column is populated in the database. once 

done, it updates the user’s balance to zero.


@RequestMapping (value = "/topMenu", method = RequestMethod.GET)

	This method is called in order to determine and display the different 

features that user has access to. It is responsible for displaying “View 

Reports” Menu in the top right of the panel, once manager has logged on to the 

system.

@RequestMapping (value = "/loadOrders", method = RequestMethod.GET)

	This method fetches orders from the database by invoking 

OrderDaoImpl.getOrders() method. The Orders table is joined with Cancels table 

and the same is used for displaying the orders in the UI. 


@RequestMapping (value = "/placeOrder", method = RequestMethod.POST)

	This method is used for creating order. It creates an entry in the order 

table by invoking OrderDaoImpl.createOrder method. It then creates an entry in 

the places table in order to store information related to the user who created 

the order. For Buy transactions, total oil owned by the user is increased, 

whereas for Sell transactions, the oil quantity is reduced.


@RequestMapping (value = "/logout", method = RequestMethod.GET)

	This method invalidates the session and logs user out.


@RequestMapping (value = "/viewReports", method = RequestMethod.GET)

	This method is responsible for displaying several results that are 

displayed to Managers. Reports are mainly displayed at aggregated level.  

Queries used for generating these reports are as listed below. 


select sum(o.quantity) as sums,(isnull(o.payment_id)!=true) as payment_avl, 

(isnull(c.client_id)!=true) as is_cancelled from orders o left join cancels c on 

o.id=c.order_id group by payment_avl,is_cancelled order by sums asc


select sum(o.total_amt) as sums,(isnull(o.payment_id)!=true) as payment_avl, 

(isnull(c.client_id)!=true) as is_cancelled from orders o left join cancels c on 

o.id=c.order_id group by payment_avl,is_cancelled order by sums asc


select sum(o.oil_adjusted_quantity) as sums, (isnull(o.payment_id)!=true) as 

payment_avl, (isnull(c.client_id)!=true) as is_cancelled from orders o left join 

cancels c on o.id=c.order_id group by payment_avl,is_cancelled order by sums asc


select sum(o.commission_fees) as sums,(isnull(o.payment_id)!=true) as 

payment_avl, (isnull(c.client_id)!=true) as is_cancelled from orders o left join 

cancels c on o.id=c.order_id group by payment_avl,is_cancelled order by sums asc


	These results are then aggregated and Google Json based charts API is 

used for displaying several charts to the manager. 
	These reports give manager insight into how much money/oil is pending or 

cancelled or is paid for by the users. 
	
This application takes care of all the requirements that have been 

specified by the friend for Oil transaction management system.

