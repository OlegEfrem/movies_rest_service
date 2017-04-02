package com.oef.movies.services.external

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.util.ByteString
import com.oef.movies.utils.ActorContext
import play.api.libs.json.{ JsDefined, Json }

import scala.concurrent.Future
import scala.io.Source

trait ImdbService {
  //http://myapifilms.com/imdb/idIMDB?idIMDB=tt0133093&token=1bc4d87a-ede2-4547-8591-a88adde7d37e
  def movieTitleById(imdbId: String): Future[String]

}

object ImdbService {
  def apply(): ImdbService =
    new ImdbServiceImpl()
}

class ImdbServiceImpl() extends ImdbService with ActorContext {

  override def movieTitleById(imdbId: String): Future[String] = {
    javaGet(url(imdbId)).map(MovieParser.parse)
  }

  private def url(imdbId: String) = s"""http://myapifilms.com/imdb/idIMDB?idIMDB=$imdbId&token=1bc4d87a-ede2-4547-8591-a88adde7d37e"""

  @throws(classOf[java.io.IOException])
  @throws(classOf[java.net.SocketTimeoutException])
  private def javaGet(url: String, connectTimeout: Int = 5000, readTimeout: Int = 60000, requestMethod: String = "GET"): Future[String] = {
    Future {
      import java.net.{ HttpURLConnection, URL }
      val connection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
      connection.setConnectTimeout(connectTimeout)
      connection.setReadTimeout(readTimeout)
      connection.setRequestMethod(requestMethod)
      val inputStream = connection.getInputStream
      val content = Source.fromInputStream(inputStream).mkString
      // scalastyle:off
      if (inputStream != null) inputStream.close()
      // scalastyle:on
      content
    }
  }

  //TODO: figure out the redirect thing
  private def akkaGet(url: String): Future[String] = {
    def bodyToString(entity: ResponseEntity): Future[String] = entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(_.utf8String)

    val response = Http().singleRequest(HttpRequest(uri = url))
    response flatMap {
      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        bodyToString(entity).map(MovieParser.parse)
      case resp @ HttpResponse(code, _, entity, _) =>
        bodyToString(entity).flatMap(b => Future.failed(new Exception(s"Upstream error: HTTP-$code, with body: $b")))
    }
  }
}

object MovieParser {
  def parse(jsonString: String): String = {
    val jsValue = Json.parse(jsonString)
    jsValue \ "error" match {
      case JsDefined(v) => v.toString()
      case _ => ((jsValue \ "data" \ "movies")(0) \ "title").as[String]
    }
  }

}
