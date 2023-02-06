import scala.util.matching.Regex
import com.github.tototoshi.csv.CSVReader
import java.io.File
import scala.util.{Failure, Success, Try}
import play.api.libs.json._


object ColumaCrew extends App{

  val reader = CSVReader.open(new File("C:\\Users\\camil\\FR Camilo Lopez\\Proyecto Pract\\PROYECTOIG" +
    "\\movie_dataset.csv"))
  val data = reader.allWithHeaders()
  reader.close()

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
    .size

}