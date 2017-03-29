package com.oef.movies.services

import scala.concurrent.Future

trait ImdbService {
  //http://myapifilms.com/imdb/idIMDB?idIMDB=tt0133093&token=1bc4d87a-ede2-4547-8591-a88adde7d37e
  def movieTitleBy(imdbId: String): Future[Option[String]]

}
