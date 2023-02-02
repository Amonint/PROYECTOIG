import com.github.tototoshi.csv._
import play.api.libs.json._
import java.io.File
import scala.util.matching.Regex

object Crew extends App {

  val reader = CSVReader.open(new File("movie_dataset.csv"))
  val data = reader.allWithHeaders()
  reader.close()

  val crew = data.flatMap(x => x.get("Crew"))
    .groupBy {
      case crew => crew
    }.map {
    case crew => (crew._1, crew._2.size)
  }

  def replacePattern(original : String) : String = {
    var txtOr = original
    val patter: Regex = "(\\s\"(.*?)\",)".r
    for(n <- patter.findAllIn(original)){
      val textOriginal = m
      val replacementText = m.replace("'", "-u0027")
      txtOr = txtOr.replace((txtOriginal, replacementText))
    }
    txtOr
  }

  def replacePattern2(original: String): String = {
    var txtOr = original
    val patter: Regex = "([a - z]\\s\"(.*?)\"\\s*[A-Z])".r
    for (m <- patter.findAllIn(original)) {
      val textOriginal = m
      val replacementText = m.replace("'", "-u0022")
      txtOr = txtOr.replace((txtOriginal, replacementText))
    }
    txtOr
  }

  def replacePattern3(original: String): String = {
    var txtOr = original
    val patter: Regex = "(:\\s'\"(.*?)',)".r
    for (n <- patter.findAllIn(original)) {
      val textOriginal = m
      val replacementText = m.replace("'", "-u0023")
      txtOr = txtOr.replace((txtOriginal, replacementText))
    }
    txtOr
  }

  val crew = data
    .map(row => row("crew"))
    .map(replacePattern2)
    .map(replacePattern)
    .map(replacePattern3)
    .map(text =>text.replace("'", "\""))
    .map(text => text.replace("-u0027", "'"))
    .map(text => text.replace("-u0022", "\\\""))
    .filter(_.isSuccess)
    .size
  println(crew)




}