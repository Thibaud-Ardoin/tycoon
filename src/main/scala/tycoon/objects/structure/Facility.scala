package tycoon.objects.structure

import scala.collection.mutable.ListBuffer

import tycoon.objects.good._
import tycoon.game.GridLocation

import scalafx.beans.property.{IntegerProperty, StringProperty}

abstract class Facility(pos: GridLocation, id: Int) extends Structure(pos, id) {

  // workers
  protected var _workers = IntegerProperty(0)

  private val workersStr = new StringProperty
  workersStr <== _workers.asString
  printData += new Tuple2("Workers", workersStr)

  def workers : Int = _workers.value
  def workers_= (new_workers: Int) = _workers.set(new_workers)


  var products = new ListBuffer[Good]
  var stocks = new ListBuffer[IntegerProperty]
  var stocksStr = new ListBuffer[StringProperty]

  def displayProducts() {
    for (p <- products) {
      stocks += IntegerProperty(0)
      stocksStr += new StringProperty
      stocksStr.last <== stocks.last.asString
      printData += new Tuple2(p.label, stocksStr.last)
    }
  }
}
