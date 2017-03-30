package com.oef.movies.http.routes.helpers

import akka.event.LoggingAdapter
import akka.stream.ActorMaterializer
import com.oef.movies.utils.{ Config, Protocol }

import scala.concurrent.ExecutionContext

trait RouteTemplate extends Protocol with RejectionHandling {
  protected implicit def executor: ExecutionContext

  protected implicit def materializer: ActorMaterializer

  protected def log: LoggingAdapter

}
