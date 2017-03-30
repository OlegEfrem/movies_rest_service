package com.oef.movies.http

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ HttpEntity, MediaTypes }
import com.oef.movies.IntegrationSpec
import spray.json._

class HttpServiceTest extends IntegrationSpec {

  import TestData._

  "registration" should {
    val requestEntity = HttpEntity(MediaTypes.`application/json`, registrationJson)

    "register a new movie" in {
      Put(generalUrl(), requestEntity) ~> movieRoutes ~> check {
        response.status shouldBe OK
        responseAs[String] shouldBe "movie registered"
      }
    }

    "reject registering an existing movie" in {
      pending
    }

    "fail validation if path and body resource identifiers differ" in {
      pending
    }

  }

  "reservation" should {

    "reserve an existing movie" in {
      val requestEntity = HttpEntity(MediaTypes.`application/json`, reservationJson)
      Patch(generalUrl(), requestEntity) ~> movieRoutes ~> check {
        response.status shouldBe OK
        responseAs[JsValue] shouldBe """{"reservationResult": "ReservationSuccessful"}""".parseJson
      }
    }

    "fail validation if path and body resource identifiers differ" in {
      pending
    }

    s"return HTTP-$NotFound for non existing movie/screen combination" in {
      pending
    }

  }

  "retrieval" should {

    "return an existing movie's info" in {
      Get(generalUrl()) ~> movieRoutes ~> check {
        response.status shouldBe OK
        responseAs[JsValue] shouldBe retrieveJson
      }
    }

    s"return HTTP-$NotFound for non existing movie/screen combination" in {
      Get(generalUrl(imdbId = "NonExistingImdbId")) ~> movieRoutes ~> check {
        status shouldBe NotFound
        responseAs[String] shouldBe "Could not find th movie identified by: imdbId=NonExistingImdbId, screenId=screen_123456"
      }
    }

  }

  object TestData {
    val registrationJson =
      """
        |{
        |"imdbId": "tt0111161",
        |"availableSeats": 100,
        |"screenId": "screen_123456"
        |}
        |""".stripMargin.parseJson.toString
    val reservationJson =
      """
        |{
        |"imdbId": "tt0111161",
        |"screenId": "screen_123456"
        |}
        |""".stripMargin.parseJson.toString
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

    def generalUrl(imdbId: String = "tt0111161", screenId: String = "screen_123456") = s"/movies/imdbId/$imdbId/screenId/$screenId/"

  }

}
