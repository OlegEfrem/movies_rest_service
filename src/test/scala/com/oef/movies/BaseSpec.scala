package com.oef.movies

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, OptionValues, WordSpec }
import scala.language.postfixOps
import scala.concurrent.duration._

trait BaseSpec extends WordSpec with Matchers with ScalaFutures with OptionValues {
  implicit val patience = PatienceConfig(5 seconds, 100 millis)
}
