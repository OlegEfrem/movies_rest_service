package com.oef.movies.services

import com.oef.movies.models._

import scala.concurrent.Future

trait MovieService {
  def save(movieRegistration: MovieRegistration): Future[Unit] = Future.successful(())

  def reserve(movieIdentification: MovieIdentification): Future[ReservationResult.Value] = Future.successful(ReservationResult.ReservationSuccessful)

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
