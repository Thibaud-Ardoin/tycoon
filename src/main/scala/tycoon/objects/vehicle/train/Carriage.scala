package tycoon.objects.vehicle.train

import scala.collection.mutable.ListBuffer

import tycoon.ui.Renderable
import tycoon.objects.structure._
import tycoon.objects.railway._
import tycoon.game.{GridLocation, Player}
import tycoon.objects.vehicle._
import scalafx.beans.property._

abstract class Carriage(id: Int, initialTown: Structure, _owner: Player) extends TrainElement(id, initialTown, _owner) {

  var stops = new ListBuffer[Structure]

  def owner: Player = _owner

  def rotation(v: Int) = { } // ??



  def embark(structure: Structure, stops: ListBuffer[Structure])
  def debark(s: Structure)

  val cost = 20
  var weight: Double = 200


}
