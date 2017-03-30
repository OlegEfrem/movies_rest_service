package com.oef.movies

import akka.event.{ LoggingAdapter, NoLogging }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.oef.movies.http.HttpService
import com.oef.movies.utils.Migration
import org.scalatest.{ Matchers, WordSpec }

trait IntegrationSpec extends BaseSpec with ScalatestRouteTest with HttpService with Migration {
  protected val log: LoggingAdapter = NoLogging
  reloadSchema()
}
