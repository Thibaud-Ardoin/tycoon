package tycoon.game


import scalafx.Includes._
import scalafx.geometry.Pos
import scalafx.scene.control.{Label, Tab, TabPane}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{HBox, VBox, Priority}
import tycoon.objects.structure._
import tycoon.objects.railway._
import tycoon.ui.Tile


sealed abstract class BuyableItem(val name: String, val price: Int, val tile: Tile) {
  def priceStr: String = "$" + price.toString

  var createByDragging: Boolean = false
}


case class BuyableStruct(override val name: String, override val price: Int, override val tile: Tile, val newInstance: (GridLocation, Int, TownManager) => Structure)
extends BuyableItem(name, price, tile) {

}


object BuyableStruct {
  def newSmallTown(pos: GridLocation, id: Int, townManager: TownManager): SmallTown = new SmallTown(pos, id, townManager)
  def newMediumTown(pos: GridLocation, id: Int, townManager: TownManager): MediumTown = new MediumTown(pos, id, townManager)
  def newLargeTown(pos: GridLocation, id: Int, townManager: TownManager): LargeTown = new LargeTown(pos, id, townManager)
  def newMine(pos: GridLocation, id: Int, townManager: TownManager): Mine = new Mine(pos, id)
  def newFarm(pos: GridLocation, id: Int, townManager: TownManager): Farm = new Farm(pos, id)
  def newFactory(pos: GridLocation, id: Int, townManager: TownManager): Factory = new Factory(pos, id)

  val SmallTown = new BuyableStruct("Small Town", 50, Tile.town, newSmallTown)
  val MediumTown = new BuyableStruct("Medium Town", 100, Tile.town, newMediumTown)
  val LargeTown = new BuyableStruct("Large Town", 200, Tile.town, newLargeTown)
  val Mine = new BuyableStruct("Mine", 50, Tile.mine, newMine)
  val Farm = new BuyableStruct("Farm", 100, Tile.farm1, newFarm)
  val Factory = new BuyableStruct("Factory", 150, Tile.factory, newFactory)
}


case class BuyableRail(override val name: String, override val price: Int, override val tile: Tile, val newInstance: GridLocation => Rail)
extends BuyableItem(name, price, tile) {
  createByDragging = true
}

object BuyableRail {
  def newRail(pos: GridLocation): Rail = new Rail(pos)

  val Rail = new BuyableRail("Rail", 5, Tile.straightRailBT, newRail)
}
