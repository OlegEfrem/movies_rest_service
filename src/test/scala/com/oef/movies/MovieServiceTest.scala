package com.oef.movies

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ HttpEntity, MediaTypes }
import org.scalatest.concurrent.ScalaFutures
import spray.json._

class MovieServiceTest extends BaseServiceTest with ScalaFutures {
  import TestData._

  "Movie service" should {

    s"return HTTP-$BadRequest for malformed path" in {
      Get("/movies/") ~> movieRoutes ~> check {
        status shouldBe NotFound
      }
    }

    s"return HTTP-$MethodNotAllowed for non supported HTTP method" in {
      Head(generalUrl()) ~> movieRoutes ~> check {
        status shouldBe MethodNotAllowed
        responseAs[String] shouldBe ""
      }
    }

    s"return HTTP-$NotFound for not existing imdbId/screenId combination" in {
      Get(generalUrl(imdbId = "NonExistingImdbId")) ~> movieRoutes ~> check {
        status shouldBe NotFound
        responseAs[String] shouldBe "Could not find th movie identified by: imdbId=NonExistingImdbId, screenId=screen_123456"
      }
    }

  }

  "register a movie" should {

    "register a new movie successfully" in {

      val requestEntity = HttpEntity(MediaTypes.`application/json`, registrationJson)
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

  "reserve a seat at the movie" should {

    "reserve a moovie for an existing movie" in {
      val requestEntity = HttpEntity(MediaTypes.`application/json`, reservationJson)
      Patch(generalUrl(), requestEntity) ~> movieRoutes ~> check {
        response.status shouldBe OK
        responseAs[JsValue] shouldBe """{"reservationResult": "ReservationSuccessful"}""".parseJson
      }
    }

    s"return HTTP-$NotFound for non existing movie" in {

    }

    "fail validation if path and body resource identifiers differ" in {

    }

  }

  "retrieve information about the movie" in {

    Get(generalUrl()) ~> movieRoutes ~> check {
      response.status shouldBe OK
      responseAs[JsValue] shouldBe retrieveJson
    }
  }

  object TestData {
    def generalUrl(imdbId: String = "tt0111161", screenId: String = "screen_123456") = s"/movies/imdbId/$imdbId/screenId/$screenId/"

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

  }

}
