package com.oef.movies.models

case class MovieRegistration(imdbId: String, availableSeats: Int, screenId: String)

case class MovieIdentification(imdbId: String, screenId: String)

object ReservationResult extends Enumeration {
  val ReservationSuccessful, NoSeatsLeft, NoSuchMovie = Value
}

case class MovieInformation(imdbId: String, screenId: String, movieTitle: String, availableSeats: Int, reservedSeats: Int)
