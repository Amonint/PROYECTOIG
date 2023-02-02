import com.github.tototoshi.csv._
import java.io.File

object colNumerica extends App {

  val reader = CSVReader.open(new File("src/main/scala/movie_dataset.csv"))
  val data = reader.allWithHeaders()
  reader.close()

  //budget
  val budget = data.flatMap(elem => elem.get("budget")).map(_.toInt)
  val men0Budget = budget.min
  val menorpromBudget = budget.filter(_ > 0).min
  val PromBudget = budget.sum.toDouble / budget.size
  val maxBudget = budget.max

  printf("\nEl promedio de budget es: %s\n" +
    "El valor minimo de budget incluido 0 es: %s\n" +
    "El valor minimo de budget sin 0 es: %s\n" +
    "El valor maximo de budget es %s\n",
    PromBudget, men0Budget, menorpromBudget, maxBudget)

  //popularity
  val popularity = data.flatMap(elem => elem.get("popularity")).map(_.toDouble)
  val men0popularity = popularity.min
  val minPopularity = popularity.filter(_ > 0).min
  val PromPopularity = popularity.sum / popularity.size
  val maxPopularity = popularity.max

  printf("\nEl promedio de popularity es: %s\n" +
    "El valor minimo de popularity incluido 0 es: %s\n" +
    "El valor minimo de popularity sin 0 es: %s\n" +
    "El valor maximo de popularity es %s\n",
    PromPopularity, men0popularity, minPopularity, maxPopularity)

  //revenue
  val revenue = data.flatMap(elem => elem.get("revenue")).map(_.toDouble)
  val min0Revenue = revenue.min
  val minRevenue = revenue.filter(_ > 0).min
  val PromRevenue = revenue.sum / revenue.size
  val maxRevenue = revenue.max

  printf("\nEl promedio de revenue es: %s\n" +
    "El valor minimo de revenue incluido 0 es: %s\n" +
    "El valor minimo de revenue sin 0 es: %s\n" +
    "El valor maximo de revenue es %s\n",
    PromRevenue, min0Revenue, minRevenue, maxRevenue)

  //vote_average
  val vote_average = data.flatMap(elem => elem.get("vote_average")).map(_.toDouble)
  val min0Average = vote_average.min
  val minAverage = vote_average.filter(_ > 0).min
  val promAverage = vote_average.sum / vote_average.size
  val maxAverage = vote_average.max

  printf("\nEl promedio de vote_average es: %s\n" +
    "El valor minimo de vote_average incluido 0 es: %s\n" +
    "El valor minimo de vote_average sin 0 es: %s\n" +
    "El valor maximo de vote_average es %s\n",
    promAverage, min0Average, minAverage, maxAverage)

  //vote_count
  val vote_count = data.flatMap(elem => elem.get("vote_count")).map(_.toInt)
  val min0vote_count = vote_count.min
  val min_vote_count = vote_count.filter(_ > 0).min
  val prom_vote_count = vote_count.sum.toDouble / vote_count.size
  val max_vote_count = vote_count.max

  printf("\nEl promedio de vote_count es: %s\n" +
    "El valor minimo de vote_count incluido 0 es: %s\n" +
    "El valor minimo de vote_count sin 0 es: %s\n" +
    "El valor maximo de vote_count es %s\n",
    prom_vote_count, min0vote_count, min_vote_count, max_vote_count)
}
