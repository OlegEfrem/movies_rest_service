package com.oef.movies.helpers

import com.oef.movies.models.MovieInformation

import scala.util.Random

trait TestData {

  def movieInfo(
    imdbId: String = randomString(),
    screenId: String = randomString(),
    availableSeats: Int = 100,
    reserverdSeats: Int = 0,
    movieTitle: String = "Some Movie Title"
  ): MovieInformation =
    MovieInformation(
      imdbId = imdbId,
      screenId = screenId,
      movieTitle = movieTitle,
      availableSeats = availableSeats,
      reservedSeats = reserverdSeats
    )

  def randomString(): String = Random.alphanumeric.take(10).mkString("")
}
