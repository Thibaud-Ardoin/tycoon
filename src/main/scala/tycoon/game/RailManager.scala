package tycoon.game

import tycoon.objects.graph._
import tycoon.objects.structure._
import tycoon.objects.railway._
import tycoon.ui._

import scalafx.collections.ObservableBuffer._
import scalafx.collections.ObservableBuffer
import scala.collection.mutable.{HashMap, ListBuffer}
import scalafx.beans.property.StringProperty


class RailManager(map: Map, tilemap: TileMap, gameGraph: Graph) {

  private val rails: ListBuffer[Rail] = new ListBuffer()
  var nbNeighborRails: Int = 0

  def createRail(pos: GridLocation) : Boolean = {
    val rail = new Rail(pos)
    var created = false
    if (tilemap.gridContains(rail.gridRect) && map.isUnused(rail.gridRect)) {
      nbNeighborRails = 0

      val neighbors = Array (
        new GridLocation(pos.col, pos.row - 1),
        new GridLocation(pos.col + 1, pos.row),
        new GridLocation(pos.col, pos.row + 1),
        new GridLocation(pos.col - 1, pos.row)
      )

      var firstDir: Int = -1
      var secondDir: Int = -1

      for (i <- 0 to 3) {
        map.getContentAt(neighbors(i)) match {
          case Some(e) =>
            if (!rail.road.finished)
              if(lookAround(rail, e)) {
                created = true
                if (firstDir == -1) firstDir = i
                else secondDir = i
              }
          case None => ()
        }
      }

      if (created) {
        rails += rail
        tilemap.addEntity(rail)
        map.add(rail.gridRect, rail)


        // apply correct rotation to rails
        if (rail.previous == rail && !rail.road.finished) { // first rail next to a struct
          if (firstDir == 0 || firstDir == 2) rail.tile = Tile.straightRailBT
          else if (firstDir == 1 || firstDir == 3) rail.tile = Tile.straightRailLR
          rail.previousDir = firstDir
        }
        else if (rail.next == rail && !rail.road.finished) { // non-terminal rail after an other rail
          if (firstDir == 0 || firstDir == 2) rail.tile = Tile.straightRailBT
          else if (firstDir == 1 || firstDir == 3) rail.tile = Tile.straightRailLR
          rail.previousDir = firstDir
          rail.previous.nextDir = (firstDir + 2) % 4
          correctRotation(rail.previous)
        }
        else { // rail placed between two rails or structs or between a rail and a struct
          // si il reste un pb c'est qu'il faut tester si firstDir et secondDir ne sont pas inversés
          rail.previousDir = firstDir
          rail.nextDir = secondDir
          if (rail.previous != rail) {
            rail.previous.nextDir = (rail.previousDir + 2) % 4
            correctRotation(rail.previous)
          }
          if (rail.next != rail) {
            rail.next.previousDir = (rail.nextDir + 2) % 4
            correctRotation(rail.next)
          }
          correctRotation(rail)
        }



      }
    }
    created
  }

  // set the good correction of a rail GIVEN its previousDir and nextDir are defined
  def correctRotation(rail: Rail) {
    if (rail.previousDir != -1 && rail.nextDir != -1) {
      if (rail.previousDir == 0 && rail.nextDir == 1) rail.tile = Tile.turningRailTR
      else if (rail.previousDir == 0 && rail.nextDir == 2) rail.tile = Tile.straightRailBT
      else if (rail.previousDir == 0 && rail.nextDir == 3) rail.tile = Tile.turningRailTL
      else if (rail.previousDir == 1 && rail.nextDir == 0) rail.tile = Tile.turningRailTR
      else if (rail.previousDir == 1 && rail.nextDir == 2) rail.tile = Tile.turningRailBR
      else if (rail.previousDir == 1 && rail.nextDir == 3) rail.tile = Tile.straightRailLR
      else if (rail.previousDir == 2 && rail.nextDir == 0) rail.tile = Tile.straightRailBT
      else if (rail.previousDir == 2 && rail.nextDir == 1) rail.tile = Tile.turningRailBR
      else if (rail.previousDir == 2 && rail.nextDir == 3) rail.tile = Tile.turningRailBL
      else if (rail.previousDir == 3 && rail.nextDir == 0) rail.tile = Tile.turningRailTL
      else if (rail.previousDir == 3 && rail.nextDir == 1) rail.tile = Tile.straightRailLR
      else if (rail.previousDir == 3 && rail.nextDir == 2) rail.tile = Tile.turningRailBL
    }
  }

  def lookAround(rail: Rail, neighbor: Any): Boolean =  {
    neighbor match {
      case (s: Structure) => {
        if (rail.road.startStructure == None) {
          rail.road.startStructure = Some(s)
          nbNeighborRails += 1
          true
        }
        else if (rail.road.startStructure != Some(s)) {
          rail.road = rail.previous.road
          if (rail != rail.previous) {
            rail.road.rails += rail
            rail.road.length += 1
          }
          rail.road.endStructure = Some(s)
          rail.road.finished = true
          gameGraph.newRoad(rail.road)
          println("tycoon > game > RailManager.scala > lookAround > new road of " + rail.road.length + " tracks")
          true
        }
        else false
      }

      case (previousRail: Rail) => {
        if (previousRail.road_head && !previousRail.road.finished) {
          nbNeighborRails += 1

          if (nbNeighborRails == 1) {
            rail.road = previousRail.road
            rail.road.rails += rail
            rail.road.length += 1
            rail.previous = previousRail
            previousRail.next = rail
            previousRail.road_head = false
            rail.road_head = true
          }

          // merge two sides of rails
          else if (previousRail.road.startStructure != rail.road.startStructure) {
            for (r <- previousRail.road.rails) {
              val tmp = r.next
              r.next = r.previous
              r.previous = tmp
            }
            rail.next = previousRail
            previousRail.previous = rail
            rail.road.finished = true
            rail.road.endStructure = previousRail.road.startStructure
            rail.road.rails ++= previousRail.road.rails
            rail.road.length += previousRail.road.length
            previousRail.road = rail.road
            gameGraph.newRoad(rail.road)
            println("tycoon > game > RailManager.scala > lookAround > new road of " + rail.road.length + " tracks")
          }
          true
        }
        else false
      }
      case _ => false
    }
  }
}
