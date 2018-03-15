package tycoon.objects.graph

import scala.collection.mutable.ListBuffer

import tycoon.objects.carriage._
import tycoon.objects.railway._
import tycoon.objects.structure._
import tycoon.objects.vehicle._
import tycoon.Game
import tycoon.GridLocation

class Route(itinerary : ListBuffer[Road], train : Train, game : Game) {

  var on_the_road = true
  var dir_indicator = 1


  var current_road : Option[Road]= None
  println (itinerary)

  def departure () = {

    train.boarding()
    for (carriage <- train.carriages_list) {
      game.playerMoney.set(game.playerMoney.get() + carriage.passengers * carriage.ticket_price)
      println(carriage.passengers)
    }
    val start = train.location.get

    //to select the right direction according to the construction sens
    if (train.location.get == itinerary(itinerary.size - 1).start_town.get) dir_indicator = 0
    else dir_indicator = 1

    println("departure")
    train.location = None
    start.list_trains -= train
    train.visible = true
    current_road = Some(itinerary(itinerary.size - 1))
    itinerary.remove(itinerary.size - 1)
    for (rail <- (current_road.get).rails) {
      if (rail.direction((dir_indicator - 1) %2) == rail) {
       train.current_rail = Some(rail)
      }
    }
    train_rotation(train)
    train.setPos((train.current_rail.get).position)
    //train.current_rail = current_road.rails
  }

  def arrival (road: Road) = {
    if (dir_indicator == 1) {train.location = road.start_town}
    else {train.location = road.end_town}
    println(train.location.get.population)
    train.location match {
      case Some(s) => s.list_trains += train
      case None => ()
    }
    train.landing()

    train.visible = false
    train.current_rail = None
    //intern_time -=1(train.location.get).position
    train.setPos(new GridLocation(train.location.get.position.col +1,train.location.get.position.row))
    if (itinerary.size == 0) {
      on_the_road = false
    }
    for (car <- train.carriages_list) {
      car.setPos(new GridLocation(-1,-1))
      car.current_rail = None
    }
  }

// give the tile the orientation fitting with the rail
  def train_rotation (thing : Train) = {
    var r = thing.current_rail.get
    //choosing rail with witch we compare the direction tookeen by the train
    var comp_rail = r.direction((dir_indicator - 1) %2)
    var plus = 1
    if (!(r == r.direction(dir_indicator))) {
      comp_rail = r.direction(dir_indicator)
      plus = 0
    }
    var x = r.position.col - comp_rail.position.col
    var y = r.position.row - comp_rail.position.row
    if (x == 1) {
      thing.rotation(90 + plus*180)
    }
    if (x == -1) {
      thing.rotation(-90+ plus*180)
    }
    if (y == 1) {
      thing.rotation(180+ plus*180)
    }
    if (y == -1) {
      thing.rotation(0+ plus*180)
    }
  }

  def wagon_rotation (thing : Carriage) = {
    var r = thing.current_rail.get
    //choosing rail with witch we compare the direction tookeen by the train
    var comp_rail = r.direction((dir_indicator - 1) %2)
    var plus = 1
    if (!(r == r.direction(dir_indicator))) {
      comp_rail = r.direction(dir_indicator)
      plus = 0
    }
    var x = r.position.col - comp_rail.position.col
    var y = r.position.row - comp_rail.position.row
    if (x == 1) {
      thing.rotation(90 + plus*180)
    }
    if (x == -1) {
      thing.rotation(-90+ plus*180)
    }
    if (y == 1) {
      thing.rotation(180+ plus*180)
    }
    if (y == -1) {
      thing.rotation(0+ plus*180)
    }
  }
  protected var intern_time : Double = 0
  //train mouvment
  def update_box (dt: Double, road : Road) = {
    train.current_rail match {
      case Some(rail) => {
        intern_time += dt
        if (intern_time > 1) {
          if (rail.direction(dir_indicator) == rail) {
            arrival(road)
            intern_time -=1
          }
          else {
            train.current_rail = Some(rail.direction(dir_indicator))
            train_rotation(train)
            train.setPos(rail.direction(dir_indicator).position)
            intern_time -=1
            //carriages update mouvment
            if (!train.carriages_list.isEmpty) {
              var rail_chain1 : Option[Rail] = Some(rail)
              var rail_chain2 : Option[Rail] = Some(rail)
              for (car <- train.carriages_list) {
                rail_chain2 = car.current_rail
                car.current_rail = rail_chain1
                rail_chain1 match {
                  case Some(r) => {
                    car.setPos(r.position)
                    wagon_rotation(car)
                  }
                  case None => {}
                }
                rail_chain1 = rail_chain2
              }
            }
          }
        }
      }
      case None => {
      }
    }
  }


  def update (dt : Double) {
    if (on_the_road) {
      train.location match {
        case Some(town) => departure()
        case None => update_box(dt, current_road.get)
      }
    }
  }
}
