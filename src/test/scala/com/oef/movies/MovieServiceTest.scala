package com.oef.movies

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import org.scalatest.concurrent.ScalaFutures
import spray.json._

class MovieServiceTest extends BaseServiceTest with ScalaFutures {
  "Movie service" should {

    "register a movie" in {
      val registrationJson =
        """
          |{
          |"imdbId": "tt0111161",
          |"availableSeats": 100,
          |"screenId": "screen_123456"
          |}
          |""".stripMargin.parseJson.toString
      val requestEntity = HttpEntity(MediaTypes.`application/json`, registrationJson)
      Put(generalUrl, requestEntity) ~> movieRoutes ~> check {
        response.status shouldBe OK
        responseAs[String] shouldBe "movie registered"
      }
    }

    "reserve a seat at the movie" in {
      val reservationJson =
        """
          |{
          |"imdbId": "tt0111161",
          |"screenId": "screen_123456"
          |}
          |""".stripMargin.parseJson.toString
      val requestEntity = HttpEntity(MediaTypes.`application/json`, reservationJson)
      Patch(generalUrl, requestEntity) ~> movieRoutes ~> check {
        response.status shouldBe OK
        responseAs[JsValue] shouldBe """{"reservationResult": "ReservationSuccessful"}""".parseJson
      }
    }

    "retrieve information about the movie" in {
      val retrieveJson =
        """
          |{
          |"imdbId": "tt0111161",
          |"screenId": "screen_123456",
          |"movieTitle": "The Shawshank Redemption",
          |"availableSeats": 100,
          |"reservedSeats": 50
          |}
          |""".stripMargin.parseJson
      Get(generalUrl) ~> movieRoutes ~> check {
        response.status shouldBe OK
        responseAs[JsValue] shouldBe retrieveJson
      }
    }

  }

  private val generalUrl = "/movies/imdbId/tt0111161/screenId/screen_123456/"

}
