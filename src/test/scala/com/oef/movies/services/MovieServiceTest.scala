package com.oef.movies.services

import com.oef.movies.UnitSpec
import com.oef.movies.models.{ RegistrationResult, ReservationResult }
import com.oef.movies.services.dao.MoviesDao
import com.oef.movies.services.external.ImdbService
import scala.concurrent.Future

class MovieServiceTest extends UnitSpec {
  private val existingMovie = movieInfo("existingMovie")
  private val nonExistingMovie = movieInfo("nonExistingMovie")
  private val noSeatsLeftMovie = movieInfo("noSeatsLeftMovie", reserverdSeats = 100)

  "read" should {

    "read an existing entry" in new TestFixture {
      service.read(existingMovie.movieIdentification).futureValue.value shouldBe existingMovie
    }

    "return NONE for a non existing entry" in new TestFixture {
      service.read(nonExistingMovie.movieIdentification).futureValue shouldBe None
    }

  }

  "save" should {
    import RegistrationResult._
    s"return $RegitrationSuccessful for a new entry" in new TestFixture {
      service.save(nonExistingMovie.movieRegistration).futureValue shouldBe RegitrationSuccessful
    }

    s"return $AlreadyExists for an existing entry" in new TestFixture {
      service.save(existingMovie.movieRegistration).futureValue shouldBe AlreadyExists
    }

  }

  "reserve" should {
    import ReservationResult._
    s"return $ReservationSuccessful for an existing entry with available seats" in new TestFixture {
      service.reserve(existingMovie.movieIdentification).futureValue shouldBe ReservationSuccessful
    }

    s"return $NoSeatsLeft for an existing entry with no available seats" in new TestFixture {
      service.reserve(noSeatsLeftMovie.movieIdentification).futureValue shouldBe NoSeatsLeft
    }

    s"return ${ReservationResult.NoSuchMovie} for a non existing entry" in new TestFixture {
      service.reserve(nonExistingMovie.movieIdentification).futureValue shouldBe NoSuchMovie
    }

  }

  trait TestFixture {
    val daoStub = stub[MoviesDao]
    val imdbStub = stub[ImdbService]
    val service = new MovieServiceImpl(daoStub, imdbStub)

    imdbStub.movieTitleById _ when * returns Future.successful("Some Movie Title")

    List(existingMovie, noSeatsLeftMovie) foreach (movie => daoStub.read _ when movie.movieIdentification returns Future.successful(Some(movie)))
    daoStub.read _ when nonExistingMovie.movieIdentification returns Future.successful(None)
    daoStub.create _ when nonExistingMovie returns Future.successful(())
    daoStub.update _ when * returns Future.successful(())
  }

}
