package com.oef.movies.http

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

trait RejectionHandling {

  def myRejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handleAll[MethodRejection] { methodRejections =>
        val names = methodRejections.map(_.supported.name)
        complete((MethodNotAllowed, s"Not supported method! Supported ones are: ${names mkString " or "}!"))
      }
      .handleNotFound {
        extractUnmatchedPath { p =>
          complete((NotFound, s"The path you requested [$p] does not exist."))
        }
      }
      .result()
}
