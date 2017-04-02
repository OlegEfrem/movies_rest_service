package com.oef.movies.http

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ HttpEntity, MediaTypes }
import com.oef.movies.ApiSpec
import com.oef.movies.models.{ MovieIdentification, MovieInformation, MovieRegistration }
import com.oef.movies.utils.Protocol
import spray.json._

class HttpServiceTest extends ApiSpec with Protocol {
  val httpService = HttpService()

  import TestData._

  "service" should {

    s"respond with HTTP-$NotFound for a non existing path" in {
      Get("/non/existing/") ~> httpService.routes ~> check {
        status shouldBe NotFound
        responseAs[String] shouldBe "The path you requested [/non/existing/] does not exist."
      }
    }

    s"respond with HTTP-$MethodNotAllowed for a non supported HTTP method" in {
      Head(generalUrl()) ~> httpService.routes ~> check {
        status shouldBe MethodNotAllowed
        responseAs[String] shouldBe "Not supported method! Supported methods are: PUT, PATCH, GET!"
      }
    }

  }

  "registration" should {
    s"respond with HTTP-$OK when registering a new movie" in {
      val movieRegistration = movieInfo().movieRegistration
      val requestEntity = HttpEntity(MediaTypes.`application/json`, registrationJson(movieRegistration).toString)
      Put(generalUrl(movieRegistration.movieIdentification), requestEntity) ~> httpService.routes ~> check {
        response.status shouldBe OK
        responseAs[String] shouldBe "movie registered"
      }
    }

    s"respond with HTTP-$Conflict when trying to register an existing movie" in {
      val existingMovie = movieInfo()
      dao.create(existingMovie)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, registrationJson(existingMovie.movieRegistration))
      Put(generalUrl(existingMovie.movieIdentification), requestEntity) ~> httpService.routes ~> check {
        response.status shouldBe Conflict
        responseAs[String] shouldBe "movie already exists"
      }
    }

    s"respond with HTTP-$Forbidden failing validation if path and body resource identifiers are different" in {
      val pathIdentifiers = movieInfo().movieIdentification
      val bodyRegistration = movieInfo().movieRegistration
      val requestEntity = HttpEntity(MediaTypes.`application/json`, registrationJson(bodyRegistration))
      Put(generalUrl(pathIdentifiers), requestEntity) ~> httpService.routes ~> check {
        response.status shouldBe Forbidden
        responseAs[String] shouldBe
          s"validation failed: resource identifiers from the path [$pathIdentifiers] " +
          s"and the body: [${bodyRegistration.movieIdentification}] do not match"
      }
    }

  }

  "reservation" should {

    s"respond with HTTP-$OK when reserving an existing movie" in {
      val existingMovie = movieInfo()
      dao.create(existingMovie)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, reservationJson(existingMovie.movieIdentification))
      Patch(generalUrl(existingMovie.movieIdentification), requestEntity) ~> httpService.routes ~> check {
        response.status shouldBe OK
        responseAs[String] shouldBe "seat reserved"
      }
    }

    s"respond with HTTP-$Conflict if no seats left" in {
      val existingMovie = movieInfo(availableSeats = 50, reserverdSeats = 50)
      dao.create(existingMovie)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, reservationJson(existingMovie.movieIdentification))
      Patch(generalUrl(existingMovie.movieIdentification), requestEntity) ~> httpService.routes ~> check {
        response.status shouldBe Conflict
        responseAs[String] shouldBe "no seats left"
      }
    }

    s"respond with HTTP-$NotFound for a non existing movie/screen combination" in {
      val notExistingId = movieInfo().movieIdentification
      val requestEntity = HttpEntity(MediaTypes.`application/json`, reservationJson(notExistingId))
      Patch(generalUrl(notExistingId), requestEntity) ~> httpService.routes ~> check {
        response.status shouldBe NotFound
        responseAs[String] shouldBe s"Could not find th movie identified by: $notExistingId"
      }
    }

    s"respond with HTTP-$Forbidden failing validation if path and body resource identifiers are different" in {
      val pathIdentifiers = movieInfo().movieIdentification
      val bodyIdentifiers = movieInfo().movieIdentification
      val requestEntity = HttpEntity(MediaTypes.`application/json`, reservationJson(bodyIdentifiers))
      Patch(generalUrl(pathIdentifiers), requestEntity) ~> httpService.routes ~> check {
        response.status shouldBe Forbidden
        responseAs[String] shouldBe
          s"validation failed: resource identifiers from the path [$pathIdentifiers] " +
          s"and the body: [$bodyIdentifiers] do not match"
      }
    }

  }

  "retrieval" should {

    s"respond with HTTP-$OK for an existing movie" in {
      val existingMovie = movieInfo()
      dao.create(existingMovie)
      Get(generalUrl(existingMovie.movieIdentification)) ~> httpService.routes ~> check {
        response.status shouldBe OK
        responseAs[JsValue] shouldBe retrieveJson(existingMovie)
      }
    }

    s"respond with HTTP-$NotFound for a non existing movie/screen combination" in {
      val notExistingId = movieInfo().movieIdentification
      Get(generalUrl(notExistingId)) ~> httpService.routes ~> check {
        status shouldBe NotFound
        responseAs[String] shouldBe s"Could not find th movie identified by: $notExistingId"
      }
    }

  }

  object TestData {

    def retrieveJson(movieInformation: MovieInformation = movieInfo()): JsValue =
      s"""
         |{
         |"imdbId": "${movieInformation.imdbId}",
         |"screenId": "${movieInformation.screenId}",
         |"movieTitle": "${movieInformation.movieTitle}",
         |"availableSeats": ${movieInformation.availableSeats},
         |"reservedSeats": ${movieInformation.reservedSeats}
         |}
         |""".stripMargin.parseJson

    def registrationJson(movieRegistration: MovieRegistration = movieInfo().movieRegistration): String =
      s"""
         |{
         |"imdbId": "${movieRegistration.imdbId}",
         |"availableSeats": ${movieRegistration.availableSeats},
         |"screenId": "${movieRegistration.screenId}"
         |}
         |""".stripMargin.parseJson.toString

    def reservationJson(movieIdentification: MovieIdentification = movieInfo().movieIdentification): String =
      s"""
         |{
         |"imdbId": "${movieIdentification.imdbId}",
         |"screenId": "${movieIdentification.screenId}"
         |}
         |""".stripMargin.parseJson.toString

    def generalUrl(movieIdentification: MovieIdentification = movieInfo().movieIdentification): String =
      s"/v1/movies/imdbId/${movieIdentification.imdbId}/screenId/${movieIdentification.screenId}/"

  }

}
