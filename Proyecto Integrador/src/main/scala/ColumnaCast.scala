import com.github.tototoshi.csv._
import play.api.libs.json.{JsArray, Json}
import requests.Response
import java.io.File
import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

object ColumnaCast extends App {

  val reader = CSVReader.open(new File("C:\\Users\\camil\\FR Camilo Lopez\\Proyecto Pract\\PROYECTOIG" +
    "\\movie_dataset.csv"))
  val data = reader.allWithHeaders()
  reader.close()

  def actorsNames(dataRaw: String): Option[String] = {
    val response: Response = requests
      .post("http://api.meaningcloud.com/topics-2.0",
        data = Map("key" -> "cdb359dbf111cba0ccc61d7f992d0235",
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


}
