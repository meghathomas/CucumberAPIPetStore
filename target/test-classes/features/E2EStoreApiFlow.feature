Feature:  Validate E2E flow of Store API

	Scenario: Validate the POST request to add a new record
		Given Generate Payload for "storeAPI" POST Request
		When Generate request for API
		And Call the "POST" API with resource "order"
		Then Validate Status code is 200
		And Validate response JSON schema using "storeSchema.json"
		
	Scenario: Validate the GET request to get the newly added record
		Given Generate request for API
		When Call the "GET" API with resource "order" by "id"
		Then Validate Status code is 200
		And Validate "id" in response
		And Validate response JSON schema using "storeSchema.json"
		
	Scenario: Validate the DELETE request to remove the added record
		Given Generate request for API
		When Call the "DELETE" API with resource "order" by "id"
		Then Validate Status code is 200
		
	Scenario: Validate the GET request to validate deleted record is removed
		Given Generate request for API
		When Call the "GET" API with resource "order" by "id"
		Then Validate Status code is 404
		And Validate "message" in response
		
	Scenario: Validate the GET request to validate inventory
		Given Generate request for API
		When Call the "GET" API with resource "inventory" by ""
		Then Validate Status code is 200
		
	Scenario: Validate the DELETE request with already deleted record
		Given Generate request for API
		When Call the "DELETE" API with resource "order" by "id"
		Then Validate Status code is 404
		And Validate "message" in response