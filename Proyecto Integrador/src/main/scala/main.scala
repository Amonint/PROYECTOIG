import com.github.tototoshi.csv._
import play.api.libs.json._
import play.api.libs.functional.syntax._

import java.io.File

object main extends App {
  val reader = CSVReader.open(new File("C:\\Users\\camil\\FR Camilo Lopez\\Proyecto Pract\\movie_dataset.csv"))
  val data = reader.allWithHeaders()
  reader.close()

}
