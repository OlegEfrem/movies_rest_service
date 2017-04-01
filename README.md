Akka Slick REST service template
=========================

[![Build Status](https://travis-ci.org/ArchDev/akka-http-rest.svg?branch=master)](https://travis-ci.org/ArchDev/akka-http-rest)

Goal of example is to show how create reactive REST services on Typesafe stack with Akka and Slick.

Example contains complete REST service for entity interaction.

### Features:
* CRUD operations
* Entity partial updates
* CORS support
* Implemented authentication by token directive
* Test coverage with *ScalaTest*
* Migrations with *FlyWay*
* Ready for *Docker*

## Requirements
* JDK 7+ (e.g. [http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html));
* sbt ([http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html));
* PostgreSQL server

## Activator 

You can get this example through [Activator](https://typesafe.com/activator).
For more information please use following url that explains how to get this template on your computer 
step-by-step: [https://www.typesafe.com/activator/template/akka-http-rest](https://www.typesafe.com/activator/template/akka-http-rest)

## Configuration
* Create database in PostgresSQL 
* Set database settings on application config or set environment variables

### Changing application config
There are two config files. Application config `src/main/resources/application.conf` and test config `src/test/resources/application.conf`.

### Environment variables
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

### Run in Docker
For launching application in Docker, you must configure database docker instance and run docker image, generated by sbt.

Generating application docker image and publishing on localhost:
```
sbt docker:publishLocal
```

Example of running, generated docker image:
```
docker run --name restapi -e DB_USER=dbuser -e DB_PASSWORD=dbpass -e DB_NAME=dbname -d --link DATABASE_INSTANCE_NAME:database -p 9090:9000 APPLICATION_IMAGE
```

- `DATABASE_INSTANCE_NAME` - name of your Postgresql docker instance
- `APPLICATION_IMAGE` - id or name of application docker image

## Run tests
To run tests, call:
```
sbt test
```

## Live example
Application deployed on heroku and can be accessed by URL [http://akka-http-rest.herokuapp.com/](http://akka-http-rest.herokuapp.com/). First request can take some time, because heroku launch up project.

You can see documentation for this example on [Apiary](http://docs.akkahttprest.apiary.io).


## Copyright

Copyright (C) 2015 Arthur Kushka.

Distributed under the MIT License.
