package com.oef.movies.http

import akka.http.scaladsl.server.Directives._
import com.oef.movies.http.routes._

trait HttpService extends MovieServiceRoute {

  val routes =
    pathPrefix("v1") {
      movieRoutes
    }

}
