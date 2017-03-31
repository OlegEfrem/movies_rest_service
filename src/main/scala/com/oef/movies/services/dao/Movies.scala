package com.oef.movies.services.dao

import com.oef.movies.models.{ MovieIdentification, MovieInformation }
import com.oef.movies.services.dao.MoviesDaoDefinitions.MoviesDaoImpl
import slick.lifted.ProvenShape

import scala.concurrent.{ ExecutionContext, Future }

private[services] trait MoviesDao {

  def create(movieInformation: MovieInformation): Future[Unit]

  def read(movieIdentification: MovieIdentification): Future[Option[MovieInformation]]

  def update(movieInformation: MovieInformation): Future[Unit]
}

private[services] object MoviesDao {
  def apply()(implicit executionContext: ExecutionContext): MoviesDao = new MoviesDaoImpl()
}

private[dao] object MoviesDaoDefinitions extends SlickDAO[MovieInformation, MovieInformation] {

  import dbProfile.profile.api._

  override lazy val query: TableQuery[MoviesTable] = TableQuery[MoviesTable]

  override def toRow(domainObject: MovieInformation): MovieInformation = ???

  override def fromRow(dbRow: MovieInformation): MovieInformation = ???

  class MoviesTable(tag: Tag) extends Table[MovieInformation](tag, "movies") {
    override def * : ProvenShape[MovieInformation] =
      (imdbId, screenId, movieTitle, availableSeats, reservedSeats) <> (MovieInformation.tupled, MovieInformation.unapply)

    def imdbId = column[String]("imdb_id")

    def screenId = column[String]("screen_id")

    def movieTitle = column[String]("movie_title")

    def availableSeats = column[Int]("available_seats")

    def reservedSeats = column[Int]("reserved_seats")

    def pk = primaryKey("movies_pk", (imdbId, screenId))

  }

  class MoviesDaoImpl(implicit executionContext: ExecutionContext) extends MoviesDao {
    override def create(movieInformation: MovieInformation): Future[Unit] = {
      db.run(query += movieInformation).map(_ => ())
    }

    override def read(movieIdentification: MovieIdentification): Future[Option[MovieInformation]] = {
      db.run(query.filter(e => e.imdbId === movieIdentification.imdbId && e.screenId === movieIdentification.screenId).result)
        .map(_.headOption)
    }

    override def update(movieInformation: MovieInformation): Future[Unit] = {
      db.run(
        query.filter(e => e.imdbId === movieInformation.imdbId && e.screenId === movieInformation.screenId)
          .update(movieInformation)
      ).map(_ => ())
    }
  }

}
