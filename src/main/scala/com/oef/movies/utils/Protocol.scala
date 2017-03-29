package com.oef.movies.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.oef.movies.models._
import spray.json.DefaultJsonProtocol

trait Protocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val movieRegistrationFormat = jsonFormat3(MovieRegistration)
  implicit val movieReservationFormat = jsonFormat2(MovieIdentification)
  implicit val movieInformationFormat = jsonFormat5(MovieInformation)

}
