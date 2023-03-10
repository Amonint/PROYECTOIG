import com.github.tototoshi.csv._

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.cibo.evilplot.plot._
import com.cibo.evilplot.plot.aesthetics.DefaultTheme.{DefaultElements, DefaultTheme}
import play.api.libs.json.Json
object graficas extends App {

  implicit val theme = DefaultTheme.copy(
    elements = DefaultElements.copy(categoricalXAxisLabelOrientation = 45)
  )

  val reader = CSVReader.open(new File("src/main/scala/movie_dataset.csv"))
  val data = reader.allWithHeaders()
  reader.close()

  val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  val releaseDateList = data.map(row => row("release_date"))
    .filter(!_.equals("")).map(text => LocalDate.parse(text, dateFormatter))

  val yearReleaseList = releaseDateList.map(_.getYear).map(_.toDouble)

  Histogram(yearReleaseList)
    .title("Años de lanzamiento")
    .xAxis()
    .yAxis()
    .xbounds(1916.0, 2018.0)
    .render()
    .write(new File("C:\\Users\\Abraham\\Downloads\\ReleaseList.png"))

  val productionCompanies = data
    .flatMap(row => row.get("production_companies"))
    .map(row => Json.parse(row))
    .flatMap(jsonData => jsonData \\ "name")
    .map(jsValue => jsValue.as[String])
    .groupBy(identity)
    .map { case (keyword, lista) => (keyword, lista.size) }
    .toList
    .sortBy(_._2)
    .reverse

  val pCompaniesValues = productionCompanies.take(10).map(_._2).map(_.toDouble)
  val pCompanieLables = productionCompanies.take(10).map(_._1)

  BarChart(pCompaniesValues)
    .title("Compañias Productoras")
    .xAxis(pCompanieLables)
    .yAxis()
    .frame()
    .yLabel("Productions")
    .bottomLegend()
    .render()
    .write(new File("C:\\Users\\Abraham\\Downloads\\pCompaniesValues.png"))

  val budget = data.flatMap(row => row.get("budget")).map(_.toDouble)

  Histogram(budget)
    .title("Budget")
    .xAxis()
    .yAxis()
    .xbounds(budget.min, budget.max)
    .render()
    .write(new File("C:\\Users\\Abraham\\Downloads\\budget.png"))

  val popularity = data.flatMap(elem => elem.get("popularity")).map(_.toDouble)

  Histogram(popularity)
    .title("popularity")
    .xAxis()
    .yAxis()
    .xbounds(popularity.min, popularity.max)
    .render()
    .write(new File("C:\\Users\\Abraham\\Downloads\\popularity.png"))

  val revenue = data.flatMap(elem => elem.get("revenue")).map(_.toDouble)

  Histogram(revenue)
    .title("revenue")
    .xAxis()
    .yAxis()
    .xbounds(revenue.min, revenue.max)
    .render()
    .write(new File("C:\\Users\\Abraham\\Downloads\\revenue.png"))

  var runtime = data.flatMap(elem => elem.get("runtime")).filter(_.isEmpty != true)
  val runtime0 = runtime.filter(_.isEmpty == true).map(_ => "0")
  runtime = runtime ++ runtime0
  val runtime1 = runtime.map(_.toDouble)

  Histogram(runtime1)
    .title("runtime")
    .xAxis()
    .yAxis()
    .xbounds(runtime1.min, runtime1.max)
    .render()
    .write(new File("C:\\Users\\Abraham\\Downloads\\Runtime.png"))

  val vote_average = data.flatMap(elem => elem.get("vote_average")).map(_.toDouble)

  Histogram(vote_average)
    .title("vote_average")
    .xAxis()
    .yAxis()
    .xbounds(vote_average.min, vote_average.max)
    .render()
    .write(new File("C:\\Users\\Abraham\\Downloads\\Vote_average.png"))

  val vote_count = data.flatMap(elem => elem.get("vote_count")).map(_.toDouble)

  Histogram(vote_count)
    .title("vote_count")
    .xAxis()
    .yAxis()
    .xbounds(vote_count.min, vote_count.max)
    .render()
    .write(new File("C:\\Users\\Abraham\\Downloads\\Vote_count.png"))

  //Datos tipo cadena

  val genres = data.flatMap(x => x.get("genres"))
    .groupBy(identity)
    .map { case (keyword, lista) => (keyword, lista.size) }
    .toList
    .sortBy(_._2)
    .reverse

  BarChart(genres.take(10).map(_._2).map(_.toDouble))
    .title("genres")
    .xAxis(genres.take(10).map(_._1))
    .yAxis()
    .frame()
    .bottomLegend()
    .render()
    .write(new File("C:\\Users\\Abraham\\Downloads\\Genres.png"))

  val release_date = data.flatMap(x => x.get("release_date"))
    .groupBy(identity)
    .map { case (keyword, lista) => (keyword, lista.size) }
    .toList
    .sortBy(_._2)
    .reverse

  BarChart(release_date.take(10).map(_._2).map(_.toDouble))
    .title("release_date")
    .xAxis(release_date.take(10).map(_._1))
    .yAxis()
    .frame()
    .bottomLegend()
    .render()
    .write(new File("C:\\Users\\Abraham\\Downloads\\Release_date.png"))

  val status = data.flatMap(x => x.get("status"))
    .groupBy(identity)
    .map { case (keyword, lista) => (keyword, lista.size) }
    .toList
    .sortBy(_._2)
    .reverse

  BarChart(status.take(10).map(_._2).map(_.toDouble))
    .title("status")
    .xAxis(status.take(10).map(_._1))
    .yAxis()
    .frame()
    .bottomLegend()
    .render()
    .write(new File("C:\\Users\\Abraham\\Downloads\\Status.png"))

}

