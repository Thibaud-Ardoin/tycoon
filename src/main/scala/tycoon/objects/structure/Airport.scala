package tycoon.objects.structure

import scala.util.Random
import tycoon.game.GridLocation
import tycoon.game.Game
import tycoon.ui.Tile

case class Airport(pos: GridLocation, id: Int) extends Structure(pos, id) {
  tile = Tile.airport
  var dependanceTown = None
  def update(dt: Double) = {
  }
}
