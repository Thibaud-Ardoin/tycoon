package tycoon.game

import tycoon.objects.graph.Map
import tycoon.ui.Tile
import tycoon.ui.Renderable
import scala.util.Random
import scalafx.geometry.Rectangle2D
import scala.collection.mutable.ListBuffer
import scala.math



class TileMap (val width: Int, val height: Int, nbEntityLayers: Int = 2) {
  private val backgroundLayer = Array.fill[Option[Tile]](width, height)(None)
  private val entities : Array[ListBuffer[Renderable]] = new Array(nbEntityLayers)
  for (i <- 0 to nbEntityLayers - 1)
    entities(i) = new ListBuffer[Renderable]

  def getBackgroundTile(col: Int, row: Int) = backgroundLayer(col)(row)
  def addBackgroundTile(col: Int, row: Int, tile: Tile) = backgroundLayer(col)(row) = Some(tile)

  def addEntity(e: Renderable, layer: Int = 0) = entities(layer) += e
  def removeEntity(e: Renderable, layer: Int = 0) = entities(layer) -= e
  def getEntities: Array[ListBuffer[Renderable]] = entities.clone()

  def gridContains(rect: GridRectangle) =
    (rect.left >= 0 && rect.top >= 0 && rect.right <= width - 1 && rect.bottom <= height - 1)

  def checkTile (pos : GridLocation, tile : Tile) : Boolean = {
    backgroundLayer(pos.col)(pos.row) match {
      case Some(t) => {
        if (t == tile) true
        else false
      }
      case None => false
    }
  }

  def checkGrass (pos : GridLocation) : Boolean = {
    var ret = false
    for (t <- Tile.grassAndGround) {
      if (checkTile(pos,t))
        ret = true
    }
    ret
  }

  /* randomly fill background layer of map using tiles
      and set Ores, trees and rocks*/
  def fillBackground(tiles: Array[Tile], map : Map) : Unit = {
    if (tiles.length >= 1) {
      val rGrass = scala.util.Random
      val rTreeAndRock = scala.util.Random
      // var lakeStarter = new ListBuffer[Int,Int]
      for { // UTILISE Array.fill !!
        row <- 0 to height - 1
        col <- 0 to width - 1
      } {
        backgroundLayer(col)(row) = Some(tiles(rGrass.nextInt(tiles.length-2)))
      }
    }
  }
  def placeLandscape(choosenPoint : Int, generatedPoints : Int) :Unit = {
    val rGrass = scala.util.Random
    val rTreeAndRock = scala.util.Random
    var lakeStarter = new ListBuffer[GridLocation]
    for {
      row <- 0 to height - 1
      col <- 0 to width - 1
    } {
      if (rTreeAndRock.nextInt(70) == 1) {
        backgroundLayer(col)(row) = Some(Tile.rock)
      }
      if (rTreeAndRock.nextInt(40) == 2) {
        backgroundLayer(col)(row) = Some(Tile.tree)
      }
      if (rTreeAndRock.nextInt(generatedPoints) == 3) {
        // backgroundLayer(col)(row) = Some(Tile.plainWater)
        lakeStarter += new GridLocation(col,row)
      }
    }
    var teselationPoints : Array[ListBuffer[GridLocation]] = new Array(lakeStarter.size)
    // var lakeCenters = new ListBuffer[Int]
    for (i <- 0 to lakeStarter.size -1) {
      teselationPoints(i) = new ListBuffer[GridLocation]
    }
    for {
        row <- 0 to height - 1
        col <- 0 to width - 1
    } {
      var distance = height + width
      var nearestPoint = 0
      var counter = 0
      for {pos <- lakeStarter} {
        var x = Math.abs(pos.row - row)
        var y = Math.abs(pos.col - col)
        var r = Math.ceil(Math.sqrt(Math.pow(x,2) + Math.pow(y,2))).toInt
        if (r<distance) {
          distance = r
          nearestPoint = counter
        }
        counter+=1
      }
      teselationPoints(nearestPoint) += new GridLocation(col, row)
    }
    for (i <- 0 to lakeStarter.size -1) {
      var randomPoint = scala.util.Random
      if (randomPoint.nextInt(choosenPoint) == 1) {
        for (pos <- teselationPoints(i)) {
          backgroundLayer(pos.col)(pos.row) = Some(Tile.plainWater)
        }
          // traiter les cas de bordure de lac pour rendre les bon tiles
        for (pos <- teselationPoints(i)) {
          if (pos.col > 0 && pos.row > 0 && pos.row< height -2 && pos.col < width -2) {
            val neighbors = Array (
              new GridLocation(pos.col, pos.row - 1),
              new GridLocation(pos.col + 1, pos.row),
              new GridLocation(pos.col, pos.row + 1),
              new GridLocation(pos.col - 1, pos.row)
            )
            for (j <- 0 to 3) {
              if (checkGrass(neighbors(j))) {
                backgroundLayer(pos.col)(pos.row) = Some(Tile.plainSand)
              }
            }
          }
        }
      }
    }


    /*for (pos <- lakeStarter) {

      for {
        row <- 0 to height - 1
        col <- 0 to width - 1
      // row <- (pos.row -10) to (pos.row +10)
      // col <- (pos.col -10) to (pos.col +10)
      } {
        // if (row < width-1 && col < height-1) {
        val rAdditionalWater = scala.util.Random
        var x = Math.abs(pos.row - row)
        var y = Math.abs(pos.col - col)
        var r = Math.ceil(Math.sqrt(Math.pow(x,2) + Math.pow(y,2))).toInt
        //   //disjoin borers and plain water..
        if ((x<30) && (y <20)) {
          if (r<15) {
            backgroundLayer(col)(row) = Some(Tile.plainWater)
          }
          else {
            if (rAdditionalWater.nextInt(2) == 1) {
              if (col > 0 && row > 0 && row< height -2 && col < width -2) {
                val neighbors = Array (
                  new GridLocation(col, row - 1),
                  new GridLocation(col + 1, row),
                  new GridLocation(col, row + 1),
                  new GridLocation(col - 1, row)
                )
                for (i <- 0 to 3) {
                  if (checkTile(neighbors(i),Tile.plainWater)) {
                    backgroundLayer(col)(row) = Some(Tile.plainWater)
                  }
                }
              }
            }
          }
        }
      }
    }*/
  }
}
