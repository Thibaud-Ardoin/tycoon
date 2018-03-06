package tycoon.objects.carriage

import tycoon.objects.structure._

abstract case class PassengerCarriage() extends Carriage {
  val max_passengers : Int
  var passengers : Int

  override def embark(town: Town) : Unit = {
    passengers = max_passengers.min(town.waiting_passengers)
    println(passengers, town.waiting_passengers)
    town.waiting_passengers -= passengers
    town.population -= passengers
  }

  override def debark(town: Town) : Unit = {
    println("debark")
    town.population += passengers
    passengers = 0
  }
}
