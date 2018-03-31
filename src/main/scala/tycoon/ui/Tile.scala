package tycoon.ui

import tycoon.game.GridLocation

import scalafx.geometry.Rectangle2D
import scalafx.scene.CacheHint
import scalafx.scene.image.{Image,ImageView}


class Tile(val name: String, row: Int, col: Int, val width: Int = 1, val height: Int = 1) {
  // source rectangle's coordinates and size in Tile.tileset
  val sx = (col - 1) * Tile.SquareWidth
  val sy = (row - 1) * Tile.SquareHeight
  val sw = width * Tile.SquareWidth
  val sh = height * Tile.SquareHeight
}

object Tile {
  val tileset = new Image("file:src/main/resources/tileset.png")

  val SquareWidth = 32
  val SquareHeight = 32

  def getImageView(t: Tile): ImageView = {
    val img = new ImageView(tileset)
    img.viewport = new Rectangle2D(t.sx, t.sy, t.sw, t.sh)
    img
  }

  val default = new Tile("default", 0, 0)
  val tree = new Tile("tree", 3, 4)
  val rock = new Tile("rock", 4, 4)
  val plainWater = new Tile("plainWater", 6, 5)
  val plainSand = new Tile("plainSand", 4, 8)

  //vehicle stuff
  val locomotiveL = new Tile("locomotiveL", 3, 5)
  val locomotiveB = new Tile("locomotiveB", 3, 2)
  val locomotiveR = new Tile("locomotiveR", 4, 5)
  val locomotiveT = new Tile("locomotiveT", 3, 6)
  val passengerWagonT = new Tile("passengerWagonT", 4, 6)
  val passengerWagonB = new Tile("passengerWagonB", 3, 1)
  val passengerWagonL = new Tile("passengerWagonL", 5, 6)
  val passengerWagonR = new Tile("passengerWagonR", 6, 6)
  val goodsWagonR = new Tile("goodsWagonR", 7, 5)
  val goodsWagonB = new Tile("goodsWagonB", 3, 3)
  val goodsWagonL = new Tile("goodsWagonL", 7, 4)
  val goodsWagonT = new Tile("goodsWagonT", 7, 6)

  val liquidWagon = new Tile("liquidWagon", 6, 1)
  val plane = new Tile("plane", 6, 2)

  //structure tiles
  val town = new Tile("town", 4, 1, width = 2)
  val mine = new Tile("mine", 4, 3)
  val factory = new Tile("factory", 5, 1)
  val farm1 = new Tile("farm1", 5, 2, width = 2)
  val farm2 = new Tile("farm2", 3, 8, width = 2)
  val farm3 = new Tile("farm3", 3, 8, width = 2) // change
  val airport = new Tile("airport", 6, 3)

  val straightRailBT = new Tile("straightRailBT", 1, 1)
  val straightRailLR = new Tile("straightRailLR", 2, 6)
  val turningRailBR = new Tile("turningRailBR", 1, 2)
  val turningRailBL = new Tile("turningRailBL", 1, 6)
  val turningRailTR = new Tile("turningRailTR", 1, 5)
  val turningRailTL = new Tile("turningRailTL", 2, 5)


  val farm = Array(
    farm1,
    farm2,
    farm3
  )

  val grass = Array(
    new Tile("grass", 1, 3),
    new Tile("grass", 1, 4),
    new Tile("grass", 2, 1),
    new Tile("grass", 2, 2),
    new Tile("grass", 2, 3),
    new Tile("grass", 2, 4)
  )
}
