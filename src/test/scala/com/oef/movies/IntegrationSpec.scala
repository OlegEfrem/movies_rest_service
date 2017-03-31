package com.oef.movies

import com.oef.movies.services.dao.MoviesDao
import com.oef.movies.utils.Migration
import org.scalatest.BeforeAndAfterAll
import scala.concurrent.ExecutionContext.Implicits.global

trait IntegrationSpec extends BaseSpec with Migration with BeforeAndAfterAll {
  val dao = MoviesDao()

  override protected def beforeAll() = {
    reloadSchema()
  }

  override protected def afterAll() = {
    reloadSchema()
  }

}
