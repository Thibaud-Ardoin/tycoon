package tycoon.objects.vehicle

import scala.collection.mutable.ListBuffer

import scalafx.scene.image.Image

import tycoon.ui.Tile
import tycoon.ui.Sprite
import tycoon.GridLocation
import tycoon.Game
import tycoon.ui.DraggableTiledPane

import tycoon.objects.carriage._
import tycoon.objects.railway._
import tycoon.objects.structure._

class BasicTrain(town : Town) extends Train(town) {
  // x: Int, y: Int
  // val init_town = road.start_town.get
  // var init_pos : GridLocation = init_town.position
  // println (init_town)
  // println (init_pos)
  // println (road.rails)
  // println (road.rails.apply(1).position)
/*
  var current_rail = road.rails(0)

  // looking for the first rail of the trail
  for (rail <- road.rails) {
    if (rail.previous == rail) {
       current_rail = rail
    }
  }
  // private var counter = 0 */
  //private var intern_time : Double = 0
/*
  def update_box (dt: Double) = {
    intern_time += dt
    if (intern_time > 1) {
      if (current_rail == current_rail.next) arrival()
      else {
        current_rail = current_rail.next
        gridLoc = current_rail.position
        intern_time = 0

        //need to orientate the locooo
        if (current_rail.get_tile_type == 1) {
          tile.getView.rotate = 90
        }
      }
    }
  }

  def update (dt : Double) {
    //update_box(dt)
    location match {
      case Some(town) => departure()
      case None => update_box(dt)
    }
  }*/

  // var pos_y = y
  var visible = false
  var speed = 10
  var destination_x = 0
  var destination_y = 0
  val weight = 50
  val cost = 200
  var current_rail : Option[Rail] = None
  //var trail = road.rails
  val tile = new Tile(Sprite.tile_locomotive)
  var carriagesList = new ListBuffer[Carriage]()
  var pos : GridLocation = location match {
    case Some(structure : Town) => {
      new GridLocation(structure.position.get_x +1,structure.position.get_y)
    }
    case Some(structure ) => structure.position
    case None => current_rail match {
      case Some(rail) => rail.position
      case None => new GridLocation(0,0) // not supposed to happen
    }
  }
  gridLoc = pos
}
