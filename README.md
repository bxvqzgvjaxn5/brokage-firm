# brokage-firm-challange

developed with:
- java 19.0.1
- maven 3.9.9

how to run the project:
- clone the repository
- run `mvn clean install`
- run `java -jar target/brokage-firm-challange-1.0-SNAPSHOT.jar`

or you can run the project with docker:
- clone the repository
- run `docker build -t challenge .`
- run `docker run -p 8080:8080 challenge`

run tests:
- run `mvn test`

useful links:
- http://localhost:8080/swagger-ui.html
- http://localhost:8080/h2-console

default users:
- admin@example.com:password
- investor1@example.com:password
- investor2@example.com:password

endpoints:
```
POST /public-offer
POST /order-sell
POST /order-buy
GET /customers
POST /customers
POST /customers/{id}/deposit
GET /assets
POST /assets
GET /orders
GET /customers/{id}
GET /customers/{id}/stocks
GET /customers/{id}/stocks/{assetId}
DELETE /order-cancel
DELETE /assets/{id}
```

take a look at [openapi documentation](./openapi.json) for more details
