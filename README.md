[![Build Status](https://travis-ci.org/OlegEfrem/movies_rest_service.svg?branch=master)](https://travis-ci.org/OlegEfrem/movies_rest_service)

# About
* This is a Movie Ticket reservation system implementation of the requirements described [here](https://github.com/OlegEfrem/movies_rest_service/blob/master/Assignment.md);
* It is based on template found [here](https://github.com/ArchDev/akka-http-rest);
* Live app is deployed on heroku [here](https://movie-reservation.herokuapp.com/v1/movies/imdbId/tt0111161/screenId/screen_123456/);
* Sample request/response and issue test calls to live app is on apiary [here](http://docs.movieserviceapi.apiary.io). 

# Highlights
## Libraries, Frameworks & Plugins
* Dependencies are defined [here](https://github.com/OlegEfrem/movies_rest_service/blob/master/build.sbt) and 
plugins [here](https://github.com/OlegEfrem/movies_rest_service/blob/master/project/plugins.sbt);
* Rest API based on [akka-http](http://doc.akka.io/docs/akka-http/10.0.5/scala/http/introduction.html);
* DB layer uses [PostgreSQL](https://www.postgresql.org/) for production, [H2 DB](http://www.h2database.com/html/main.html) for the 
[Integration Tests](https://github.com/OlegEfrem/movies_rest_service/blob/master/src/test/scala/com/oef/movies/IntegrationSpec.scala),  
[Slick](http://slick.lightbend.com/) with [HikariCP](https://github.com/brettwooldridge/HikariCP) connection pool as DB Access Layer and
[Flyway](https://flywaydb.org/) for DB schema management;
* For json (de)serialization two frameworks are used: [spray-json](https://github.com/spray/spray-json) and [play-json](https://www.playframework.com/documentation/2.5.x/ScalaJson);
* Testing layer uses: [scala test](http://www.scalatest.org/) for defining test cases, [scala mock](http://scalamock.org/) for mocking dependencies in unit tests and 
[akka-http-test-kit](http://doc.akka.io/docs/akka-http/10.0.5/scala/http/routing-dsl/testkit.html) for api tests;
* Plugins configured for the project are: [s-coverage](https://github.com/scoverage/sbt-scoverage) for code test coverage, [scala-style](http://www.scalastyle.org/) for code style checking and
[scala-iform](https://github.com/scala-ide/scalariform) for code formatting.

## Architecture/Design/Coding Principles
* Code follows the following programming concepts: 
  * OOP (Object Oriented Programming) principles of abstraction (all operations (meaning function, methods and procedures) and parameters are abstracted concepts of the business domain language (to be read as DDD Domain Driven Design) 
  found in the [task description](https://github.com/OlegEfrem/movies_rest_service/blob/master/Assignment.md), encapsulation (for example in the case classes, 
  [MoviesDao](https://github.com/OlegEfrem/movies_rest_service/blob/master/src/main/scala/com/oef/movies/services/dao/Movies.scala) encapsulates db management logic in private implementation exposed only via public interface).
  Inheritance and Polymorphism is exhibited in the relation [MovieId](https://github.com/OlegEfrem/movies_rest_service/blob/master/src/main/scala/com/oef/movies/models/Movie.scala#L3) with 
  concrete [Movie](https://github.com/OlegEfrem/movies_rest_service/blob/master/src/main/scala/com/oef/movies/models/Movie.scala#L8) implementations;
  * FP (Funtional Programming) principles of immutability (all data is immutable), higher order functions, pattern matching, monadic constructions, recursion;
  * Declarative style where operations describe what is done and not how;
  * TDD with [ScalaTest](http://www.scalatest.org/) the test were created before the implementation;
  * Mocking (with a tool like [ScalaMock](http://scalamock.org/)) - the db layer is mocked to only test the outer service layer in
  test [MovieServiceTest](https://github.com/OlegEfrem/movies_rest_service/blob/master/src/test/scala/com/oef/movies/services/MovieServiceTest.scala#L54) 
  * Iterative Development that can be traced through git commit messages;
  * Intention Revealing Operations where all parts of the method contribute to the clarity: operation name, parameter names, parameter types, and operation return type;
  * Separation of concerns: each class/object/trait has clearly defined responsibilities that are fully testable and tested;
  * Meaningful commit and messages - commit per implemented feature, and on important code changes that might be usefull to revisit;
  * Layer architecture: Each outer layer can only access the layers from the same level or a level immediate bellow it; 
* Given a different scale of the Application and longer development time several things would be done in different scenarios: 
  * BDD (with a tool like [Cucumber](https://cucumber.io/)) - having non technical audience as stackholders would make apropriate for a testing closer to the business level/language;
  * Performance Tests (with a tool like [Gatling](http://gatling.io/#/)) - having some SLA (Service Level Agreements) and thus non functional requirements would require to have this test layer in place;
  * CI/CD (Continuous Integration/Continuous Delivery with a tool like [Jenkins](https://jenkins.io/) or [TeamCity](https://www.jetbrains.com/teamcity/)) pipe line set up for easier delivery of the functionality;
  * Feature/Issue/Release/Time management and tracking with a tool like [Jira](https://www.atlassian.com/software/jira) or [YouTrack](https://www.jetbrains.com/youtrack/)
  * Cloud Infrastructure Provisioning and Configuration Management (on [AmazonCloudServices](https://aws.amazon.com/), [OpenStack](https://www.openstack.org/), 
  [Teraform](https://www.terraform.io/), [Puppet](https://puppet.com/), [Consul](https://www.consul.io/), etc)
  * given requirements for Elastic Systems (concept of the [Reactive Programming](http://www.reactivemanifesto.org/));
  * Architecture diagrams of the code and infrastructure.

# API Behaviour
It's behaviour is defined by the API Integration test found [here](https://github.com/OlegEfrem/movies_rest_service/blob/master/src/test/scala/com/oef/movies/http/HttpServiceTest.scala).
## The test output is: 
```aidl
[info] HttpServiceTest:
[info] service
[info] - should respond with HTTP-404 Not Found for a non existing path
[info] - should respond with HTTP-405 Method Not Allowed for a non supported HTTP method
[info] registration
[info] - should respond with HTTP-200 OK when registering a new movie
[info] - should respond with HTTP-409 Conflict when trying to register an existing movie
[info] - should respond with HTTP-403 Forbidden failing validation if path and body resource identifiers are different
[info] reservation
[info] - should respond with HTTP-200 OK when reserving an existing movie
[info] - should respond with HTTP-409 Conflict if no seats left
[info] - should respond with HTTP-404 Not Found for a non existing movie/screen combination
[info] - should respond with HTTP-403 Forbidden failing validation if path and body resource identifiers are different
[info] retrieval
[info] - should respond with HTTP-200 OK for an existing movie
[info] - should respond with HTTP-404 Not Found for a non existing movie/screen combination
```
## Movie title
* The register movie input date only provides the movie id, but no movie title, whereas the retrieve requires a movie title, thus on register flow a call to an external [movie information service provider](http://www.myapifilms.com/index.do)
is made [here](https://github.com/OlegEfrem/movies_rest_service/blob/master/src/main/scala/com/oef/movies/services/external/ImdbService.scala) to get movie title by movie id 
(as a side note the [javaGet](https://github.com/OlegEfrem/movies_rest_service/blob/master/src/main/scala/com/oef/movies/services/external/ImdbService.scala#L33) is a temporary solution while 
figuring out the [akkaGet](https://github.com/OlegEfrem/movies_rest_service/blob/master/src/main/scala/com/oef/movies/services/external/ImdbService.scala#L50) to work);
* This call will only be executed if movies is not already registered as can be seen in [this logic](https://github.com/OlegEfrem/movies_rest_service/blob/master/src/main/scala/com/oef/movies/services/MovieService.scala#L36);
* If the service responds with a non error json, movie title along with input registration data is saved to the db, other wise the error json is saved as movie title in the db containing the error code and message returned by the 
upstream server in the body as json response;

# Run Requirements
* This is a scala sbt project, and was developed and tested to run with: Java 1.8, Scala 2.12.1 and Sbt 0.13.8
* Production db is PostgreSQL;

## Configuration
* Create database in PostgresSQL 
* Set database settings on application config or set environment variables

### Changing application config
There are two config files. Application config `src/main/resources/application.conf` and test config `src/test/resources/application.conf`.

### Enviroment variables
- `PSQL_URL` || `PSQL_TEST_URL` - database url by scheme `jdbc:postgresql://host:port/database-name`
- `PSQL_USER` || `PSQL_TEST_USER` - database user
- `PSQL_PASSWORD` || `PSQL_TEST_PASSWORD` - database password

## Run application
To run application, call:
```
sbt run
```
If you wanna restart your application without reloading of sbt, use:
```
sbt re-start
```
