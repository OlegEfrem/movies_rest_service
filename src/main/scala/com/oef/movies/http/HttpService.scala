package com.oef.movies.http

import akka.http.scaladsl.server.Directives._
import com.oef.movies.http.routes.MovieRoutes
import com.oef.movies.services.MovieService

import scala.concurrent.ExecutionContext

class HttpService(movieRoutes: MovieRoutes)(implicit executionContext: ExecutionContext) extends RejectionHandling {
  val routes =
    handleRejections(myRejectionHandler) {
      pathPrefix("v1") {
        movieRoutes.movieRoutes
      }
    }

}

object HttpService {
  def apply()(implicit executionContext: ExecutionContext): HttpService =
    new HttpService(MovieRoutes())
}
