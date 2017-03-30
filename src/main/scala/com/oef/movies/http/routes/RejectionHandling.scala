package com.oef.movies.http.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

trait RejectionHandling {

  implicit def myRejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handleAll[MethodRejection] { methodRejections =>
        val names = methodRejections.map(_.supported.name)
        complete((MethodNotAllowed, s"Can't do that! Supported: ${names mkString " or "}!"))
      }
      .handleNotFound {
        extractUnmatchedPath { p =>
          complete((NotFound, s"The path you requested [$p] does not exist."))
        }
      }
      .result()
}
