package com.oef.movies.http.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ Directive0, StandardRoute }
import com.oef.movies.models.{ MovieIdentification, MovieRegistration }
import com.oef.movies.services.MovieService
import com.oef.movies.utils.Protocol
import spray.json._
import scala.concurrent.ExecutionContext

class MovieRoutes(movieService: MovieService)(implicit executionContext: ExecutionContext) extends Protocol {

  val movieRoutes = pathPrefix("movies" / "imdbId" / Segment / "screenId" / Segment) { (imdbId, screentId) =>
    val urlIdentifiers = MovieIdentification(imdbId, screentId)
    pathEndOrSingleSlash {
      put { //Register a movie
        entity(as[MovieRegistration]) { movieRegistration =>
          validateEquals(urlIdentifiers, MovieIdentification(movieRegistration.imdbId, movieRegistration.screenId)) {
            val saveResult = movieService.save(movieRegistration)
            import com.oef.movies.models.RegistrationResult._
            onSuccess(saveResult) {
              case RegitrationSuccessful => complete("movie registered")
              case AlreadyExists => complete(Conflict, "movie already exists")
            }
          }
        }
      } ~
        patch { //Reserve a seat at the movie
          entity(as[MovieIdentification]) { movieIdentification =>
            validateEquals(urlIdentifiers, movieIdentification) {
              val reservationResult = movieService.reserve(movieIdentification)
              import com.oef.movies.models.ReservationResult._
              onSuccess(reservationResult) {
                case ReservationSuccessful => complete("seat reserved")
                case NoSeatsLeft => complete(Conflict, "no seats left")
                case NoSuchMovie => notFound(movieIdentification)
              }
            }
          }
        } ~
        get { //Retrieve information about the movie
          val movieInfo = movieService.read(urlIdentifiers)
          onSuccess(movieInfo) {
            case Some(x) => complete(x.toJson)
            case None => notFound(urlIdentifiers)
          }
        }
    }
  }

  private def notFound(movieIdentification: MovieIdentification): StandardRoute = {
    complete(NotFound, s"Could not find th movie identified by: $movieIdentification")
  }

  private def validateEquals(urlIdentifiers: MovieIdentification, bodyIdentifiers: MovieIdentification): Directive0 = {
    validate(urlIdentifiers == bodyIdentifiers, s"resource identifiers from the path [$urlIdentifiers] and the body: [$bodyIdentifiers] do not match")
  }

}

object MovieRoutes {
  def apply()(implicit executionContext: ExecutionContext): MovieRoutes = new MovieRoutes(MovieService())
}
