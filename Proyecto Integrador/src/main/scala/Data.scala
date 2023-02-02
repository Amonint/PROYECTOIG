import com.github.tototoshi.csv._
import play.api.libs.json._
import java.io.File
import scala.util.matching.Regex

object Data extends App {

  val reader = CSVReader.open(new File("movie_dataset.csv"))
  val data = reader.allWithHeaders()
  reader.close()


  }