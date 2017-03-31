package com.oef.movies.services

import com.oef.movies.models._
import com.oef.movies.services.dao.MoviesDao
import org.postgresql.util.PSQLException

import scala.concurrent.{ ExecutionContext, Future }

trait MovieService {

  def save(movieRegistration: MovieRegistration): Future[RegistrationResult.Value]

  def reserve(movieIdentification: MovieIdentification): Future[ReservationResult.Value]

  def read(movieIdentification: MovieIdentification): Future[Option[MovieInformation]]

}

object MovieService {
  def apply()(implicit executionContext: ExecutionContext): MovieService = new MovieServiceImpl(MoviesDao(), ImdbService())
}

class MovieServiceImpl(dao: MoviesDao, imdbService: ImdbService)(implicit executionContext: ExecutionContext) extends MovieService {

  override def save(movieRegistration: MovieRegistration): Future[RegistrationResult.Value] = {
    val saveResult =
      for {
        movieTitle <- imdbService.movieTitleById(movieRegistration.imdbId)
        movieInfo = new MovieInformation(movieRegistration, movieTitle)
        result <- dao.create(movieInfo)
      } yield result
    saveResult.map(_ => RegistrationResult.RegitrationSuccessful) recover {
      case e: PSQLException if e.getMessage.contains(Identifiers.primaryKeyViolation) =>
        RegistrationResult.AlreadyExists
    }
  }

  override def reserve(movieIdentification: MovieIdentification): Future[ReservationResult.Value] = {
    val existingMovie = read(movieIdentification)
    existingMovie flatMap {
      case Some(x) =>
        if (x.reservedSeats < x.availableSeats)
          dao.update(x.reserveOneSeat()).map(_ => ReservationResult.ReservationSuccessful)
        else
          Future.successful(ReservationResult.NoSeatsLeft)
      case None =>
        Future.successful(ReservationResult.NoSuchMovie)
    }
  }

  override def read(movieIdentification: MovieIdentification): Future[Option[MovieInformation]] = {
    dao.read(movieIdentification)
  }

}
