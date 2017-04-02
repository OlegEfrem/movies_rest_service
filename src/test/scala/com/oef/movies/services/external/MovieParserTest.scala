package com.oef.movies.services.external

import com.oef.movies.UnitSpec

class MovieParserTest extends UnitSpec {

  "MovieParserTest" should {
    import TestData._

    "parse movie title form the json structure for an existing imdbId" in {
      MovieParser.parse(json) shouldBe "The Matrix"
    }

    "return error code and message from the json structure for non existing imdbId" in {
      MovieParser.parse(errorJsons) shouldBe """{"code":112,"message":"Incorrect IMDB Id"}"""
    }

  }
}
// scalastyle:off
object TestData {
  val json =
    """{
      |   "data":{
      |      "movies":[
      |         {
      |            "title":"The Matrix",
      |            "originalTitle":"",
      |            "year":"1999",
      |            "releaseDate":"19990331",
      |            "directors":[
      |               {
      |                  "name":"Lana Wachowski",
      |                  "id":"nm0905154"
      |               },
      |               {
      |                  "name":"Lilly Wachowski",
      |                  "id":"nm0905152"
      |               }
      |            ],
      |            "writers":[
      |               {
      |                  "name":"Lilly Wachowski",
      |                  "id":"nm0905152"
      |               },
      |               {
      |                  "name":"Lana Wachowski",
      |                  "id":"nm0905154"
      |               }
      |            ],
      |            "runtime":"136 min",
      |            "urlPoster":"https://images-na.ssl-images-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_UX182_CR0,0,182,268_AL_.jpg",
      |            "countries":[
      |               "USA"
      |            ],
      |            "languages":[
      |               "English"
      |            ],
      |            "genres":[
      |               "Action",
      |               "Sci-Fi"
      |            ],
      |            "plot":"Thomas A. Anderson is a man living two lives. By day he is an average computer programmer and by night a hacker known as Neo. Neo has always questioned his reality, but the truth is far beyond his imagination. Neo finds himself targeted by the police when he is contacted by Morpheus, a legendary computer hacker branded a terrorist by the government. Morpheus awakens Neo to the real world, a ravaged wasteland where most of humanity have been captured by a race of machines that live off of the humans' body heat and electrochemical energy and who imprison their minds within an artificial reality known as the Matrix. As a rebel against the machines, Neo must return to the Matrix and confront the agents: super-powerful computer programs devoted to snuffing out Neo and the entire human rebellion.",
      |            "simplePlot":"A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
      |            "idIMDB":"tt0133093",
      |            "urlIMDB":"http://www.imdb.com/title/tt0133093",
      |            "rating":"8.7",
      |            "metascore":"73",
      |            "rated":"R",
      |            "votes":"1,290,110",
      |            "type":"Movie"
      |         }
      |      ]
      |   },
      |   "about":{
      |      "version":"2.30.0",
      |      "serverTime":"2017/04/02 15:01:43"
      |   }
      |}""".stripMargin

  val errorJsons = """{
                     |   "error":{
                     |      "code":112,
                     |      "message":"Incorrect IMDB Id"
                     |   },
                     |   "about":{
                     |      "version":"2.30.0",
                     |      "serverTime":"2017/04/02 18:07:39"
                     |   }
                     |}""".stripMargin
}
// scalastyle:on