# Money Transfer RESTful API

### Following rest services are available for consumption

| HTTP Method | URI Path | Description | Example | 
| --- | --- | --- | --- |
| POST | /account | Open account | curl -X POST http://127.0.0.1:8888/api/account -F nationalIdNumber=9010025674289 -F firstName=Ngonidzashe -F lastName=Chibamu -F amount=1000 -F currency=ZAR |
| GET | /account/{accountNumber} | Retrieve specific account information | curl -X GET http://127.0.0.1:8888/api/867834567 |
| GET | /account/customer/{nationalIdNumber} | Retrieve accounts for specific customer | curl -X GET http://127.0.0.1:8888/api/account/customer/8812297682082 |                           
| PUT | /account/deposit | Deposit money into account | curl -X PUT http://127.0.0.1:8888/api/account/deposit -F accountNumber=87879213821 -F amount=660 |
| PUT | /account/withdraw | Withdraw money out of account | curl -X PUT http://127.0.0.1:8888/api/account/withdraw -F accountNumber=7818721381 -F amount=432 |
| PUT | /account/transfer | Transfer money from one account to another | curl -X PUT http://127.0.0.1:8888/api/transfer -F fromAccountNumber=7487139123 -F toAccountNumber=841783791213 -F amount=55 | 
| DELETE | /account | Close account | curl -X DELETE http://127.0.0.1:8888/api/account -F accountNumber=619319321321 |
| GET | /transaction/{nationalIdNumber}/{fromDate}/{toDate} | Get a statement of transactions between the specified date range | curl -X http://127.0.0.1:8888/transaction/20190101/20190324 |
| GET | /transaction/{transactionType}/{nationalIdNumber}/{fromDate}/{toDate} | Get a statement of transactions of specific type between the specified date range | curl -X http://127.0.0.1:8888/transaction/deposit/20190101/20190324 |
| GET | /account/balance/{accountNumber} | Query account balance | curl -X GET http://127.0.0.1:8888/api/account/balance/784287821321 | 
