package com.oef.movies

import com.oef.movies.utils.Migration
import org.scalatest.BeforeAndAfterAll

trait IntegrationSpec extends BaseSpec with Migration with BeforeAndAfterAll {

  override protected def beforeAll() = {
    reloadSchema()
  }

  override protected def afterAll() = {
    reloadSchema()
  }

}
