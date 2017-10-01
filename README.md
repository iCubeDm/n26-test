# N26 test task
This is the test task from N26

## Tools
 * Java8
 * SpringBoot (1.5.6.RELEASE)
 * maven
 * Docker
 * IDE used: JetBrains IntellijIDEA

## How to install
 * `git clone https://github.com/iCubeDm/n26-test`
 * `cd to/the/folder`
 * `mvn clean install`
 * `java -jar app/target/app-1.0.0.jar`
 
## Swagger
There is an auto-generated swagger documentation provided
 * Swagger-UI: `http://localhost:8080/swagger-ui.html`
 * Swagger.json: `http://localhost:8080/v2/api-docs`
 
## Others
Also there is a Dockerfile provided for future deployments =)
To make the docker image, first make `mvn clean install` to create an artifact,
and then docker during the build phase will use `app-1.0.0.jar` to make the image.
