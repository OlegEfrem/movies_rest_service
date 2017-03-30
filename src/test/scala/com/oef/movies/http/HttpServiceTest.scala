package com.oef.movies.http

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import com.oef.movies.IntegrationSpec
import spray.json._

class HttpServiceTest extends IntegrationSpec {

  import TestData._

  "Movie service" should {

    s"return HTTP-$NotFound for non existing path" in {
      Get("/non/existing/") ~> routes ~> check {
        status shouldBe NotFound
        responseAs[String] shouldBe "The path you requested [/non/existing/] does not exist."
      }
    }

    s"return HTTP-$MethodNotAllowed for non supported HTTP method" in {
      Head(generalUrl()) ~> routes ~> check {
        status shouldBe MethodNotAllowed
        responseAs[String] shouldBe "Not supported method! Supported methods are: PUT, PATCH, GET!"
      }
    }

  }

  "registration" should {
    val requestEntity = HttpEntity(MediaTypes.`application/json`, registrationJson())

    "register a new movie" in {
      Put(generalUrl(), requestEntity) ~> routes ~> check {
        response.status shouldBe OK
        responseAs[String] shouldBe "movie registered"
      }
    }

    "reject registering an existing movie" in {
      val imdbId = "existingMovie"
      val requestEntity = HttpEntity(MediaTypes.`application/json`, registrationJson(imdbId))
      Put(generalUrl(imdbId), requestEntity) ~> routes ~> check {
        response.status shouldBe Conflict
        responseAs[String] shouldBe "movie already exists"
      }
    }

    "fail validation if path and body resource identifiers differ" in {
      Put(generalUrl("differentImdb"), requestEntity) ~> routes ~> check {
        response.status shouldBe Forbidden
        responseAs[String] shouldBe
          "validation failed: resource identifiers from the path [imdbId=differentImdb, screenId=screen_123456] " +
            "and the body: [imdbId=tt0111161, screenId=screen_123456] do not match"
      }
    }

  }

  "reservation" should {
    val requestEntity = HttpEntity(MediaTypes.`application/json`, reservationJson())

    "reserve an existing movie" in {
      Patch(generalUrl(), requestEntity) ~> routes ~> check {
        response.status shouldBe OK
        responseAs[String] shouldBe "seat reserved"
      }
    }

    s"return HTTP-$Conflict if no seats left" in {
      val imdbId = "noSeatsLeft"
      val requestEntity = HttpEntity(MediaTypes.`application/json`, reservationJson(imdbId))
      Patch(generalUrl(imdbId), requestEntity) ~> routes ~> check {
        response.status shouldBe Conflict
        responseAs[String] shouldBe "no seats left"
      }
    }

    s"return HTTP-$NotFound for non existing movie/screen combination" in {
      val imdbId = "NonExistingImdbId"
      val requestEntity = HttpEntity(MediaTypes.`application/json`, reservationJson(imdbId))
      Patch(generalUrl(imdbId), requestEntity) ~> routes ~> check {
        response.status shouldBe NotFound
        responseAs[String] shouldBe s"Could not find th movie identified by: imdbId=$imdbId, screenId=screen_123456"
      }
    }

    "fail validation if path and body resource identifiers differ" in {
      Patch(generalUrl("differentImdb"), requestEntity) ~> routes ~> check {
        response.status shouldBe Forbidden
        responseAs[String] shouldBe
          "validation failed: resource identifiers from the path [imdbId=differentImdb, screenId=screen_123456] " +
            "and the body: [imdbId=tt0111161, screenId=screen_123456] do not match"
      }
    }

  }

  "retrieval" should {

    "return an existing movie's info" in {
      Get(generalUrl()) ~> routes ~> check {
        response.status shouldBe OK
        responseAs[JsValue] shouldBe retrieveJson
      }
    }

    s"return HTTP-$NotFound for non existing movie/screen combination" in {
      Get(generalUrl(imdbId = "NonExistingImdbId")) ~> routes ~> check {
        status shouldBe NotFound
        responseAs[String] shouldBe "Could not find th movie identified by: imdbId=NonExistingImdbId, screenId=screen_123456"
      }
    }

  }

  object TestData {
    val retrieveJson: JsValue =
      """
        |{
        |"imdbId": "tt0111161",
        |"screenId": "screen_123456",
        |"movieTitle": "The Shawshank Redemption",
        |"availableSeats": 100,
        |"reservedSeats": 50
        |}
        |""".stripMargin.parseJson

    def registrationJson(imdbId: String = "tt0111161"): String =
      s"""
         |{
         |"imdbId": "$imdbId",
         |"availableSeats": 100,
         |"screenId": "screen_123456"
         |}
         |""".stripMargin.parseJson.toString

    def reservationJson(imdbId: String = "tt0111161"): String =
      s"""
         |{
         |"imdbId": "$imdbId",
         |"screenId": "screen_123456"
         |}
         |""".stripMargin.parseJson.toString

    def generalUrl(imdbId: String = "tt0111161", screenId: String = "screen_123456"): String =
      s"/v1/movies/imdbId/$imdbId/screenId/$screenId/"

  }

}
