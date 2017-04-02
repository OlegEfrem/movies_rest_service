package com.oef.movies

import akka.event.{ Logging, LoggingAdapter }
import akka.http.scaladsl.Http
import com.oef.movies.http.HttpService
import com.oef.movies.utils.{ ActorContext, Config, Migration }

object Main extends App with Config with Migration with ActorContext {
  val log: LoggingAdapter = Logging(system, getClass)
  val httpService = HttpService()

  migrate()

  Http().bindAndHandle(httpService.routes, httpInterface, httpPort)
}
