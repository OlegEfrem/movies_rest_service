package com.oef.movies.http

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

trait RejectionHandling {

  def myRejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handle{ case ValidationRejection(msg, _) =>
          complete((Forbidden, s"validation failed: $msg"))
      }
      .handleAll[MethodRejection] { methodRejections =>
        val names = methodRejections.map(_.supported.name)
        complete((MethodNotAllowed, s"Not supported method! Supported methods are: ${names mkString ", "}!"))
      }
      .handleNotFound {
        extractUnmatchedPath { p =>
          complete((NotFound, s"The path you requested [$p] does not exist."))
        }
      }
      .result()
}
