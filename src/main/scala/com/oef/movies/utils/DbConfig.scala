package com.oef.movies.utils

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait DbConfig {
  protected val dbProfile: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("db.movies")
  protected val db = dbProfile.db
}
