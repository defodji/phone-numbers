# SpringBoot application to manage phone numbers for a telecom company

### Start the application
./gradlew bootRun

### OpenAPI specifications for the API 
The specs are in the file src/main/api/customer-phones-1.0.0.json

### API Operations
* To get the list of all phone numbers
`GET http://localhost:8080/managePhoneNumbers/v1/phonenumbers`

* To get the list of phone numbers for a given customer
`GET http://localhost:8080/managePhoneNumbers/v1/phonenumbers&customerId=02f84c05-e48b-4986-8275-e8674f956cd6`

* To activate a phone number
`POST http://localhost:8080/managePhoneNumbers/v1/phonenumbers/`
with a sample request body
`{
"number": "0444555222",
"customerId": "02f84c05-e48b-4986-8275-e8674f956cd6"
}`
