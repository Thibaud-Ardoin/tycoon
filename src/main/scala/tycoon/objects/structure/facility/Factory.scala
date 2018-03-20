package tycoon.objects.structure

import scala.util.Random
import tycoon.game.GridLocation
import tycoon.game.Game
import tycoon.ui.Tile

case class Factory(pos: GridLocation, id: Int) extends Facility(pos, id) {
  tile = new Tile(Tile.factory_tile)
  //val price = game.mine_price //To choose
}
