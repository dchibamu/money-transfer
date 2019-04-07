# Money Transfer RESTful API
Design and implement a rest api in Java for money transfers. The solution supports thread safety and an in-memory database implemented with a concurrentHashMap data structure. For the purposes of this illustration there is no cross currency or exchange rate transactions. 
All deposits, withdrawals and transfers are done in the currency the account was opened. 

In order to make transfers, deposits must be made first into each source account. Each deposit must be at least 10 units of currency.

## Following rest services are available for consumption

| HTTP Method | URI Path | Description | Example | 
| --- | --- | --- | --- |
| POST | /api/accounts | Open account | curl -d '{"nationalIdNumber":2019061629869,"firstName":"Karigamombe","lastName":"Tambazvinonetsa","currency":"EUR","amount":2365732}' -H 'Content-Type: application/json' http://127.0.0.1:8888/api/accounts  |
| GET  | /api/accounts/customer/{nationalIdNumber} | Get all accounts by national identity number | curl -H 'Accept:application/json' http://127.0.0.1:8888/api/accounts/customer/2019061629869 | 
| GET | /api/accounts/{accountNumber} | Retrieve account details | curl -H 'Accept:application/json' http://127.0.0.1:8888/api/accounts/1554554306697 |
| PUT | /account/{accountNumber}/deposit | Deposit money into bank account | curl -i -X PUT -H 'Content-Type:application/json' -d '{"accountNumber":1554554306697, "amount":15873.29}' http://localhost:8888/api/accounts/deposit |
| PUT | /account/{accountNumber}/withdraw | Withdraw money from bank account | curl -X PUT http://127.0.0.1:8888/api/account/withdraw -d '{"accountNumber":1554554306697, "amount":15873.29}' http://localhost:8888/api/accounts/withdrawal |
| PUT | /account/transfer | Transfer money from one account to another | curl -i -X PUT -H 'Content-Type:application/json' -d '{"sourceAccountNumber": 1554554306697,"targetAccountNumber":1554556764760,"amount":-250000}' http://localhost:8888/api/accounts/transfer | 
| GET | /account/balance/{accountNumber} | Query account balance | curl -H 'Accept:application/json' http://localhost:8888/api/accounts/balance/1554554306697 | 
| DELETE | /account/{accountNumber} | Close account - Precondition: Account balance must be ZERO | curl -X DELETE http://127.0.0.1:8888/api/account/619319321321 |
| GET | /transaction/{nationalIdNumber}/{fromDate}/{toDate} - Still needs to be implemented | Get a statement of transactions between the specified date range | curl -X http://127.0.0.1:8888/api/transaction/20190101/20190324 |
| GET | /transaction/{transactionType}/{nationalIdNumber}/{fromDate}/{toDate} - Still needs to be implemented | Get a statement of transactions of specific type between the specified date range | curl -X http://127.0.0.1:8888/transaction/deposit/20190101/20190324 |

## Employed technology stack
Linux/Windows with following technology
Java 1.8
Maven 3.x

## Posible Improvements
- Update the rest api to support XML
- Add Api documentation through swagger
- Include Persistence exception mapper to mapper all SQLExceptions to a generic exception
- Security
- Multi-currency accounts, e.g deposit USD amount in EUR account
- Migrate to relational database system that supports transactions and ACID properties
- For a real distributed system, there is need to separate frequent reads from writes - create and updates operations. 
    For example balance enquiries could be directed to their own server through a load balancer.

## Usage
Building application and running application - from command line
```
mvn clean package && java -jar target/money-transfer.jar
```