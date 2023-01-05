import com.github.tototoshi.csv._
import java.io.File

object main extends App {
  val reader = CSVReader.open(new File("Proyecto Integrador\src\main\scala\movie_dataset.csv"))
  val data = reader.allWithHeaders()
  reader.close()

  val calculoEstadis = (columna: String) => {
    val mayor = data.flatMap(x => x.get(columna)).maxBy(x => x.toLong)
    println("\n\tEl mayor valor de -"+ columna + " - es: "+ mayor)

    val menor = data.flatMap(x => x.get(columna)).minBy (x => x.toLong)
    println("\tEl menor valor de "+ columna + "- es: " + menor)

    val promedioConCero = data.flatMap(x => x.get(columna)).map(x => x.toDouble).sum /
      data.flatMap(x => x.get("presupuesto")).map(x => x.toDouble).size
    println("\tEl promedio (incluido el valor 0) de "+ columna + promedioConCero)

    val promedioSinCero= data.flatMap(x => x.get(columna)).map(x => x.toDouble).sum /
      data.flatMap(x => x.get("presupuesto")).filter(x => x.toInt != 0).map(x => x.toDouble).size
    println("\tEl promedio (sin incluir el valor 0) de "+ columna + "- es: " + promedioSinCero)
  }
}
