package com.oef.movies

import akka.event.{ LoggingAdapter, NoLogging }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.oef.movies.http.HttpService

trait ApiSpec extends IntegrationSpec with ScalatestRouteTest with HttpService {
  protected val log: LoggingAdapter = NoLogging
}
