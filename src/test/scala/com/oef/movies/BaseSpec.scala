package com.oef.movies

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, OptionValues, WordSpec }

trait BaseSpec extends WordSpec with Matchers with ScalaFutures with OptionValues {

}
