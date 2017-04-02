package com.oef.movies

import com.oef.movies.helpers.TestData
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, OptionValues, WordSpec }

import scala.concurrent.duration._
import scala.language.postfixOps

trait BaseSpec extends WordSpec with Matchers with ScalaFutures with OptionValues with TestData {
  implicit val patience = PatienceConfig(5 seconds, 100 millis)
}
