package com.oef.movies.services.dao

import java.sql.SQLException

import com.oef.movies.IntegrationSpec
import org.scalatest.BeforeAndAfterEach

class MoviesDaoTest extends IntegrationSpec with BeforeAndAfterEach {

  "create" should {

    "insert a new entry" in {
      val info = movieInfo()
      dao.read(info.movieIdentification).futureValue shouldBe None
      dao.create(info).futureValue shouldBe ((): Unit)
      dao.read(info.movieIdentification).futureValue.value shouldBe info
    }

    "throw an exception on existing entry insertion" in {
      val info = movieInfo()
      dao.create(info).futureValue
      whenReady(dao.create(info).failed) { e =>
        e shouldBe a[SQLException]
        //e.asInstanceOf[SQLException].getErrorCode shouldBe Identifiers.primaryKeyViolationCode works on H2, doesn't on Postgresql
      }
    }

  }

  "read" should {

    "return an existing entry" in {
      val info = movieInfo()
      dao.create(info).futureValue
      dao.read(info.movieIdentification).futureValue.value shouldBe info
    }

    "return none for non existing entry" in {
      val info = movieInfo()
      dao.read(info.movieIdentification).futureValue shouldBe None
    }

  }

  "update" should {

    "update an existing entry reserving a seat" in {
      val info = movieInfo()
      dao.create(info).futureValue

      def dbInfo = dao.read(info.movieIdentification).futureValue.value

      dbInfo.reservedSeats shouldBe 0
      dao.update(info.reserveOneSeat()).futureValue
      dbInfo.reservedSeats shouldBe 1
    }

    "do nothing for non existing entry" in {
      dao.update(movieInfo()).futureValue shouldBe ((): Unit)
    }

  }

}
