# Auth Web WS
Simple Spring Boot App that uses JWT to implement Authorization.

## Endpoints

- **GET** /health    

        Health check on port 8090

-  **POST** /authenticate

        Takes in username and password and returns the JWT

        Request

        {
            "username": "".
            "password": ""
        }

        Response
        
        {
            "jwt" : <TOKEN>
        }

- **POST** /authLive

        Pass in the token in the Authorization Header to check if the user is still authenticated or not
