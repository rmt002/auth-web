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

---

## Docker config

1. Run the following command to build the docker image

                docker build -f DOCKERFILE  -t <desired-image-name> .  

2. Run the docker image

                docker run -p <machinePort>:<imagePort> <the-image-name-from-previous-command>

3. Stop the docker image

                docker ps
                docker stop <containerid-to-be-stopped>
