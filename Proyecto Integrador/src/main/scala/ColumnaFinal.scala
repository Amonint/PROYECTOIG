import com.github.tototoshi.csv.CSVReader
import play.api.libs.json.{JsArray, JsValue, Json}
import requests.Response
import scalikejdbc.{AutoSession, ConnectionPool, DBSession, scalikejdbcSQLInterpolationImplicitDef}

import java.io.File
import scala.util.Try
import scala.util.matching.Regex


object ColumnaFinal extends App {

  //Comandos para trabajar con CCV
  val reader = CSVReader.open(new File("C:\\Users\\camil\\FR Camilo Lopez\\Proyecto Pract\\PROYECTOIG" +
    "\\movie_dataset.csv"))
  val data = reader.allWithHeaders()
  reader.close()

  //Comando para poder conectar a la data base.
  Class.forName("com.mysql.cj.jdbc.Driver")
  ConnectionPool.singleton("jdbc:mysql://localhost:3306/movie_data", "root", "camello456")
  implicit val session: DBSession = AutoSession

  //Tratameinto para columna ID
  val id = data.flatMap(elem => elem.get("id")).map(_.toInt)


  //Tratamiento para columna cast
  def actorsNames(dataRaw: String): Option[String] = {
    val response: Response = requests
      .post("http://api.meaningcloud.com/topics-2.0",
        data = Map("key" -> "Aqui su ApiKey",
          "lang" -> "en",
          "txt" -> dataRaw,
          "tt" -> "e"),
        headers = Map("content-type" -> "application/x-www-form-urlencoded"))
    Thread.sleep(1000)
    if(response.statusCode == 200) {
      Option(response.text)
    } else
      Option.empty
  }

  val cast = data
    .map(row => row("cast"))
    .filter(_.nonEmpty)
    .map(StringContext.processEscapes)
    //.take(10) //Use un número limitado para hacer sus pruebas, pero, al final debe analizar todos los datos.
    .map(actorsNames)
    .map(json => Try(Json.parse(json.get)))
    .filter(_.isSuccess)
    .map(_.get)
    .flatMap(json => json("entity_list").as[JsArray].value)
    .map(_("form"))
    .map(data => data.as[String])
    .distinct


  //Tratameiento para columan original_Title
  val title = data.map(row => row("original_title"))



  //Tratamiento para columna Crew
  def replacePatternFull(original: String) : String = {
    var txtOr = original
    val pattern1: Regex = "(\\s\"(.*?)\",)".r
    val pattern2: Regex = "([a-z]\\s\"(.?)\"\\s[A-Z])".r
    val pattern3: Regex = "(:\\s'\"(.*?)',)".r

    txtOr = pattern2.replaceAllIn(txtOr, m => m.toString().replace("\"", "-u0022"))
    txtOr = pattern1.replaceAllIn(txtOr, m => m.toString().replace("'", "-u0027"))
    txtOr = pattern3.replaceAllIn(txtOr, m => m.toString().replace("\"", "-u0022"))
    txtOr
  }

  val crew = data
    .map(row => row("crew"))
    .map(replacePatternFull)
    .map(text => text.replace("'", "\""))
    .map(text => text.replace("-u0027", "'"))
    .map(text => text.replace("-u0022", "\\\""))
    .map(text => Try(Json.parse(text)))
    .filter(_.isSuccess)
    .map(_.get)
    .flatMap(_.as[List[JsValue]])
    .map(x => (x("name").as[String], x("gender").as[Int], x("department").as[String], x("job").as[String], x("credit_id").as[String],
      x("id").as[Int]))
    .distinct

  //Tratamiento para columna revenue
  val revenue = data.map(row => row("revenue"))


  //Comando inserte en general para todo el codigo

  val movis = data.map(x =>(
    x("id"),
    x("cast"),
    x("original_title"),
    x("revenue")
  ))

  val inserFinal = movis.map(x =>
  sql"""
    INSERT INTO DATASET(id, cast, original_title, revenue)
    VALUES
    (${x._1},${x._2},${x._3},${x._4})
    """.stripMargin
    .update
    .apply()
  )

  val InsertCrew = crew.map(x =>
    sql"""
  INSERT INTO dataserCrew(nombreCrew,generoCrew,departamentoCrew,trabajoCrew,creditosCrew,idCrew)
  VALUES
  (${x._1},${x._2},${x._3},${x._4},${x._5},${x._6})
  """.stripMargin
      .update
      .apply()
  )





}
