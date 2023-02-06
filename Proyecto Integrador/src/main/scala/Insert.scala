import com.github.tototoshi.csv.CSVReader
import scalikejdbc._
import play.api.libs.json._
import requests.Response
import java.io.File
import scala.util.Try
import scala.util.matching.Regex
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths, StandardOpenOption}

object Insert extends App {


  val reader = CSVReader.open(new File("src/main/scala/movie_dataset.csv"))
  val data: List[Map[String, String]] = {
    reader.allWithHeaders()
  }
  reader.close()
  def actorsNames(dataRaw: String): Option[String] = {
    val response: Response = requests
      .post("http://api.meaningcloud.com/topics-2.0",
        data = Map("key" -> "Aqui el ApiKey",
          "lang" -> "en",
          "txt" -> dataRaw,
          "tt" -> "e"),
        headers = Map("content-type" -> "application/x-www-form-urlencoded"))
    Thread.sleep(500)
    if(response.statusCode == 200) {
      Option(response.text)
    } else
      Option.empty
  }

  val cast = data
    .map(row => row("cast"))
    .filter(_.nonEmpty)
    .map(StringContext.processEscapes)
    .take(4803) //Use un número limitado para hacer sus pruebas, pero, al final debe analizar todos los datos.
    .map(actorsNames)
    .map(json => Try(Json.parse(json.get)))
    .filter(_.isSuccess)
    .map(_.get)
    .flatMap(json => json("entity_list").as[JsArray].value)
    .map(_("form"))
    .map(data => data.as[String])


  val Movie = data
  .map(x =>
    (
      x("budget").toLong,
      x("id").toInt,
      x("tagline")
   match {
        case valueOfRT if valueOfRT.isEmpty => 0.0
        case valueOfRT => valueOfRT.toDouble
      },
    ))

println(Movie)

val newRowMovie = Movie.map(x =>
  sql"""
       |INSERT INTO Movie (cast, budget, idMovie,  tagline, )
       |VALUES
       |(${x._1}, ${x._2}, ${x._3}, ${x._4}})
       """.stripMargin
    .update
.apply())