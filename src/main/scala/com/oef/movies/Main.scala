package com.oef.movies

import akka.actor.ActorSystem
import akka.event.{ Logging, LoggingAdapter }
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.oef.movies.http.HttpService
import com.oef.movies.http.routes.MovieRoutes
import com.oef.movies.services.{ ImdbService, MovieServiceImpl }
import com.oef.movies.services.dao.MoviesDao
import com.oef.movies.utils.{ Config, Migration }

import scala.concurrent.ExecutionContext

object Main extends App with Config with Migration {
  implicit val system = ActorSystem()
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val log: LoggingAdapter = Logging(system, getClass)
  val httpService = HttpService()

  migrate()

  Http().bindAndHandle(httpService.routes, httpInterface, httpPort)
}
