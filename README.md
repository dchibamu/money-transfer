# Money Transfer RESTful API

### Following rest services are available for consumption

| HTTP Method | URI Path | Description | Example | 
| --- | --- | --- | --- |
| POST | /account | Open account | ``` curl -X POST http://127.0.0.1:8080/api/account \ -F nationalId=9010025674289 \ -F firstName=Ngonidzashe \
    -F lastName=Chibamu \
    -F amount=1000 \
    -F currency=ZAR ``` |
                                   
