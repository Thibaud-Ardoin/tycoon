package tycoon.objects.structure

import scala.collection.mutable.ListBuffer
import scala.Array

import tycoon.game.GridLocation
import tycoon.game.{Game, TownManager}
import tycoon.objects.structure._
import tycoon.objects.good._

import tycoon.ui.Tile

import scalafx.beans.property.{IntegerProperty, StringProperty}


abstract class Town(pos: GridLocation, id: Int, townManager: TownManager) extends Structure(pos, id) {
  tile = Tile.town

  // choose town name
  def chooseName() {
    try {
      /*val i = r.nextInt(townManager.unchosen_names.length)
      _name.set(townManager.unchosen_names(i))
      townManager.unchosen_names.remove(i)*/
      val nameId = r.nextInt(townManager.townNames.length)
      val name = townManager.townNames(nameId)
      _name.set(name)
      townManager.unchosenNames -= name
    }
    catch {
      case e: Exception => println("you've created too many towns")
    }
  }

  chooseName()

  //Booleans about town facilities
  var hasAirport = false
  var hasDock = false
  var airport : Option[Airport]= None
  var dock : Option[Dock]= None

   // _name = StringProperty(city_names(id))
  protected var _population = IntegerProperty(0)
  protected var _waiting_passengers = IntegerProperty(0)
  protected var _jobSeekers = IntegerProperty(0)

  printData += new Tuple2("Name", _name)

  private val populationStr = new StringProperty
  populationStr <== _population.asString
  printData += new Tuple2("Population", populationStr)

  def population : Int = _population.value
  def population_= (new_pop: Int) = _population.set(new_pop)


  private val jobSeekersStr = new StringProperty
  jobSeekersStr <== _jobSeekers.asString
  printData += new Tuple2("Job seekers", jobSeekersStr)

  def jobSeekers : Int = _jobSeekers.value
  def jobSeekers_= (new_seekers: Int) = _jobSeekers.set(new_seekers)

  // gestion of the waiting passengers

  var totalWaiters = 0
  var destinations = new ListBuffer[Structure]
  var waitersInt = new ListBuffer[IntegerProperty]
  var waitersStr = new ListBuffer[StringProperty]

  def displayWaiters() {
    printData += new Tuple2("Waiting passengers", StringProperty(""))
    for (town <- townManager.towns_list) {
      if (town != this) {
        destinations += town
        waitersInt += IntegerProperty(0)
        waitersStr += new StringProperty
        waitersStr.last <== waitersInt.last.asString
        printData += new Tuple2(town.name, waitersStr.last)
      }
    }
  }

  def waiters(i: Int) : Int = waitersInt(i).value

  // updates

  def updatePopulation () = {
    if (population < max_population) {
      val i = r.nextInt(population)
      population += i/50
    }
  }

  def updateJobSeekers () = {
    if (jobSeekers < population/5) {
      val i = r.nextInt(population)
      jobSeekers += i/100
    }
  }

  def updateWaiters () = {
    try {
      if (totalWaiters < population/3) {
        val new_waiters = (r.nextInt(population))/30
        totalWaiters += new_waiters
        val destination = r.nextInt(waitersInt.length)
        waitersInt(destination).set(waiters(destination) + new_waiters)
      }
    }
    catch {
      case e: Exception => ()
    }
  }

  override def update(dt: Double) = {
    intern_time += dt
    if (intern_time > 2) {
      intern_time -= 1
      updatePopulation()
      updateJobSeekers()
      updateWaiters()
      updateConsumption()
    }
  }

  // def position : GridLocation = pos
  // def name : String = _name.value
  def name_= (new_name: String) = _name.set(new_name)
  def waiting_passengers : Int = _waiting_passengers.value
  def waiting_passengers_= (new_wait_pass: Int) = _waiting_passengers.set(new_wait_pass)
  var max_population: Int = 1000
  population = 50 + r.nextInt(50)
  waiting_passengers = 0

  // requests of the town

  var requests = new ListBuffer[Good]
  var needsInt = new ListBuffer[IntegerProperty]
  var needsStr = new ListBuffer[StringProperty]
  var pricesInt = new ListBuffer[IntegerProperty]

  def needs(i: Int) : Int = needsInt(i).value
  def prices(i: Int) : Int = pricesInt(i).value

  def newRequest(good: Good, amount: Int) {
    requests += good
    needsInt += IntegerProperty(amount)
    needsStr += new StringProperty
    pricesInt += IntegerProperty(1) // will evolve with time using townManager
    needsStr.last <== needsInt.last.asString.concat(" for $").concat(pricesInt.last.asString).concat(" per unity")
    printData += new Tuple2(good.label, needsStr.last)
  }

  def satisfyRequest(good: Good, i: Int, soldQuantity: Int) : Boolean = {
    // returns true iff the request is completely satisfied
    needsInt(i).set(needs(i) - soldQuantity)
    if (soldQuantity == needs(i)) {
      // delete the request
      requests -= requests(i)
      needsInt -= needsInt(i)
      needsStr -= needsStr(i)
      pricesInt -= pricesInt(i)

      val data = printData.find(data => good.label == data._1)
      data match {
        case Some(d) => printData -= d
        case None => println("tycoon > objects > structure > Town : an unexisting request has been discovered")
      }
      true
    }
    else {
      false
    }
  }

  var hunger = 0
  var alreadyDiet = false
  var alreadyStarving = false
  val lunchTime = 100
  val dietTime = 150
  val starvingTime = 300

  // consumption of food
  def updateConsumption() = {
    if (hunger > lunchTime) {
      var nutritiousNeeds = population
      var i = 0
      // feed the population
      while (i < products.length && nutritiousNeeds > 0) {
        products(i) match {
          case Food(_) => {
            while (nutritiousNeeds > 0 && datedProducts(i).length > 0) {
              var m = datedProducts(i)(0)
              m.kind match {
                case f: Food => nutritiousNeeds -= m.quantity*f.nutritiousness
                case _ => println("Town > list products doesn't correspond to list datedProducts: enormous mistake!")
              }
              datedProducts(i) -= m
            }
          }
          case _ => () // for the moment we consider that people eat only food
        }
      }
      hunger *= nutritiousNeeds/population
      if (hunger > starvingTime) {
        population = (population-hunger).max(1)
        hunger += 5
        if (!alreadyStarving) {
          alreadyStarving = true
          newRequest(new Food("Cake"), population)
          throwEvent("["+name+"] Everyone is starving, nur noch ein Gott kann uns retten...")
        }
      }
      else {
        alreadyStarving = false
        if (hunger > dietTime) {
          hunger += 3
          if (!alreadyDiet) {
            alreadyDiet = true
            newRequest(new Food("Cake"), nutritiousNeeds/2)
            throwEvent("["+name+"] People are hungry!")
          }
        }
        else {
          alreadyDiet = false
          hunger += 2
        }
      }
    }
    else {
      alreadyStarving = false
      alreadyDiet = false
      hunger += 1
    }
  }
}
