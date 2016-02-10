import scala.collection.immutable.IndexedSeq
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._

object Main {

  case class Pizza(toppings: List[String])

  val margherita = Pizza(List("Tomato", "Mozarrella"))

  def prepareMargherita(): Future[Pizza] = Future.successful(margherita)

  def findPizzaWithHam(pizzas: List[Pizza]): Option[Pizza] =
    pizzas.find(_.toppings.contains("Ham"))

  def getCalories(pizza: Pizza): Long = 200

  def getCaloriesFuture(pizza: Future[Pizza]): Future[Long] =
    pizza.map(getCalories)

  def getCaloriesOption(pizza: Option[Pizza]): Option[Long] =
    pizza.map(getCalories)


  def getCaloriesM[M[_] : Functor](pizza: M[Pizza]): M[Long] =
    pizza.map(getCalories)

  def addSupplement[M[_] : Monad](pizzaM: M[Pizza],
                                  supplementM: M[String]): M[Pizza] = {
    for (
      pizza <- pizzaM;
      supplement <- supplementM
    ) yield {
      val toppings: List[String] = pizza.toppings :+ supplement
      pizza.copy(toppings = toppings)
    }
  }


  def main(args: Array[String]) {
    println("HELLO")
    getCaloriesM(prepareMargherita()).foreach(println)
    getCaloriesM(Option(margherita)).foreach(println)
  }
}