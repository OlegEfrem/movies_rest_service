package com.oef.movies.http

import akka.http.scaladsl.server.Directives._
import com.oef.movies.http.routes.MovieRoutes

class HttpService(movieRoutes: MovieRoutes) extends RejectionHandling {
  val routes =
    handleRejections(customRejectionHandler) {
      pathPrefix("v1") {
        movieRoutes.movieRoutes
      }
    }

}

object HttpService {
  def apply(): HttpService =
    new HttpService(MovieRoutes())
}
