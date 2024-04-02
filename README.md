# Trade and Material Assets (TMA) Warehouse 

Run the application. 

To access the endpoints, you need to register and authorize. 

The application has three roles: 
  1. Employee (username, password) 
  2. Administrator (username, password)
  3. Coordinator (username, password)
 
 After authorization, you will get token. Provide it with all your future requests. 
 
 Endpoints: 
  1. api/v1/registering - Registration. 
  2. api/v1/auth - Authorization.
     
  4. api/v1/items - Get items (can apply filter (Can provide a RequestBody (field, value)) and sort).
  5. api/v1//items/{name}/edit - Edit item by name. Must provide RequestBody (itemName, itemGroup, unitOfMeasurement, quantity, priceWithoutVAT, storageLocation, contactPerson, photo). Not available for "Employee" role.
  7. api/v1/items/create - Create item. Must provide a RequestBody (itemName, itemGroup, unitOfMeasurement, quantity, priceWithoutVAT, storageLocation, contactPerson, photo). Not available for "Employee" role. 
  8. api/v1/items/{name}/remove - Remove item by name. Not available for "Employee" role.

  9. api/v1/requests/create - Create requests. Must provide List of RequestBody (itemName, quantity, priceWithoutVAT, comment). Not available for "Coordinator" role.
  10. api/v1/requests/{requestId}/status - Сhange request status by ID. Not available for "Employee" role.
  11. api/v1/requests - Get requests (can apply filter (Can provide a RequestBody (field, value)) and sort). "Coordinator" and "Administrator" receive all possible requests from the database, "Employee" receives only his own requests.
