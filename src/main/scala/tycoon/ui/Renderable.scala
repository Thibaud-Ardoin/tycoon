package tycoon.ui // #

import scala.collection.mutable.ListBuffer

import tycoon.game.{GridLocation, GridRectangle}

import scalafx.beans.property.StringProperty
import scalafx.geometry.Rectangle2D
import scalafx.scene.image.ImageView


abstract class Renderable(private var pos: GridLocation) {
  private var _tile: Tile = Tile.Default
  private var _gridRect: GridRectangle = new GridRectangle(pos, 1, 1)
  private val _printData = new ListBuffer[PrintableData]

  var visible: Boolean = true

  def tile: Tile = _tile
  def gridRect: GridRectangle = _gridRect
  def gridPos: GridLocation = _gridRect.pos
  def printData = _printData

  def tile_= (newTile: Tile) = {
    _tile = newTile
    _gridRect.cols = tile.width
    _gridRect.rows = tile.height
  }
  def gridPos_= (newGridPos: GridLocation) = _gridRect.pos = newGridPos

  def gridIntersects(other: Renderable): Boolean = gridRect.intersects(other.gridRect)
  def gridContains(pos: GridLocation): Boolean = gridRect.contains(pos)

  def width: Int = tile.width
  def height: Int = tile.height

  def gridWidth: Int = tile.width * Tile.SquareWidth
  def gridHeight: Int = tile.height * Tile.SquareHeight
}
