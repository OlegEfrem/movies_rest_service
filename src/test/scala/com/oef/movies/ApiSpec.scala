package com.oef.movies

import akka.event.{ LoggingAdapter, NoLogging }
import akka.http.scaladsl.testkit.ScalatestRouteTest

trait ApiSpec extends IntegrationSpec with ScalatestRouteTest {
  protected val log: LoggingAdapter = NoLogging
}
