package com.oef.movies.helpers

import com.oef.movies.models.MovieInformation

import scala.util.Random

trait TestData {

  def movieInfo(imdbId: String = "someImdbId", screenId: String = "someScreenId", availableSeats: Int = 100, reserverdSeats: Int = 0): MovieInformation =
    MovieInformation(
      imdbId = imdbId,
      screenId = screenId,
      movieTitle = "Some Movie Title",
      availableSeats = availableSeats,
      reservedSeats = reserverdSeats
    )

  def randomMovieInfo() = movieInfo(Random.nextString(5))
}
