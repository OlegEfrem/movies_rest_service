package com.oef.movies.services

import com.oef.movies.models._

import scala.concurrent.Future

trait MovieService {
  def save(movieRegistration: MovieRegistration): Future[RegistrationResult.Value] = {
    Future.successful(
      movieRegistration match {
        case MovieRegistration("existingMovie", _, _) => RegistrationResult.AlreadyExists
        case _ => RegistrationResult.RegitrationSuccessful
      }
    )
  }

  def reserve(movieIdentification: MovieIdentification): Future[ReservationResult.Value] = {
    Future.successful(
      movieIdentification match {
        case MovieIdentification("tt0111161", "screen_123456") => ReservationResult.ReservationSuccessful
        case MovieIdentification("noSeatsLeft", _) => ReservationResult.NoSeatsLeft
        case _ => ReservationResult.NoSuchMovie
      }
    )
  }

  def read(movieIdentification: MovieIdentification): Future[Option[MovieInformation]] = {
    Future.successful(
      movieIdentification match {
        case MovieIdentification("tt0111161", "screen_123456") =>
          Some(MovieInformation(
            imdbId = "tt0111161",
            screenId = "screen_123456",
            movieTitle = "The Shawshank Redemption",
            availableSeats = 100,
            reservedSeats = 50
          ))
        case _ => None
      }
    )
  }
}
