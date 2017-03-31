package com.oef.movies.models

sealed abstract class MovieId(imdbId: String, screenId: String) {
  lazy val movieIdentification: MovieIdentification =
    MovieIdentification(imdbId, screenId)
}

case class MovieRegistration(imdbId: String, availableSeats: Int, screenId: String) extends MovieId(imdbId, screenId)

object RegistrationResult extends Enumeration {
  val RegitrationSuccessful, AlreadyExists = Value
}

case class MovieIdentification(imdbId: String, screenId: String) {
  override def toString: String = s"imdbId=$imdbId, screenId=$screenId"
}

object ReservationResult extends Enumeration {
  val ReservationSuccessful, NoSeatsLeft, NoSuchMovie = Value
}

case class MovieInformation(imdbId: String, screenId: String, movieTitle: String, availableSeats: Int, reservedSeats: Int) extends MovieId(imdbId, screenId) {
  def reserveOneSeat(): MovieInformation =
    MovieInformation(imdbId, screenId, movieTitle, availableSeats, reservedSeats = reservedSeats + 1)
}
