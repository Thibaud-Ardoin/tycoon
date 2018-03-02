package tycoon.ui

import tycoon.{ Game, GridLocation }
import tycoon.objects.structure.Town

import scalafx.Includes._
import scalafx.scene.Scene

import scalafx.beans.property.{ StringProperty, IntegerProperty }
import scalafx.beans.binding.Bindings

import scalafx.scene.paint.Color._
import scalafx.scene.paint.{ Stops, LinearGradient }
import scalafx.scene.layout.{ BorderPane, VBox }
import scalafx.scene.text.Text
import scalafx.geometry.{ Pos, Insets, Rectangle2D }
import scalafx.scene.control.Button
import scalafx.scene.image.{ Image, ImageView }
import scalafx.scene.input.{ KeyCode, KeyEvent, MouseEvent }
import scala.collection.mutable.ListBuffer

import scalafx.scene.input.MouseEvent

import scala.collection.mutable.HashMap
import scalafx.scene.control.{ TextField, Button }
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{ BorderPane, HBox }
import scalafx.collections.ObservableBuffer
import scalafx.collections.ObservableBuffer._ // Add, Remove, Reorder, Update

class RailCreation(var game: Game) extends BorderPane {
  private var onFinished = new Runnable { def run() {} }

  def setOnFinished(r: Runnable) = {
    onFinished = r
  }
  //private val tiledPane = new DraggableTiledPane(game.tilemap, game.padding)

  // game.entities.onChange((_, changes) => {
  //   for (change <- changes)
  //     change match {
  //       case Add(_, added) =>
  //         added.foreach(town => game.tiledPane.addEntity(town))
  //       case Remove(_, removed) =>
  //         removed.foreach(town => game.tiledPane.removeEntity(town))
  //       case Reorder(from, to, permutation) => ()
  //       case Update(pos, updated) => ()
  //     }
  // })


  // private val min_towns: Int = 2
  // private val max_towns: Int = 5
  //
  // private var nb_towns = IntegerProperty(0)
  // private var nb_towns_str = new StringProperty
  // nb_towns_str <== nb_towns.asString

  // check whether click is simple click or drag


  private var mouse_anchor_x: Double = 0
  private var mouse_anchor_y: Double = 0

  def init(): Unit = {
    center = gamePane
    top = menuPane
    gamePane.center = game.tiledPane // update
  }

  private val gamePane = new BorderPane {

    style = "-fx-background-color: lightgreen"

    onMousePressed = (e: MouseEvent) => {
      requestFocus()

      mouse_anchor_x = e.x
      mouse_anchor_y = e.y
    }

    onMouseReleased = (e: MouseEvent) => {
      if (e.x == mouse_anchor_x && e.y == mouse_anchor_y) {
        // creation of a city
        // if (nb_towns.get() < max_towns) {
        //   val pos: GridLocation = game.tiledPane.screenPxToGridLoc(e.x, e.y)
        //
        //   if (game.createTown(pos)) {
        //     nb_towns.set(nb_towns.get() + 1)
      }
    }
  }

  private val menuPane = new HBox {
    style = """-fx-background-color: linear-gradient(darkgreen, green, green);
               -fx-border-color: transparent transparent black transparent;
               -fx-border-width: 1;"""
    alignment = Pos.Center

    private val name_field = new TextField {
      promptText = "Choose a player name"
      margin = Insets(10)
    }

    children = Seq(
      new Text {
        text = "Click to place "+" up to " +  " cities"
        margin = Insets(10)
      },
      new Text {
        text =  "/"
        //fill = when(nb_towns >= min_towns) choose Blue otherwise Red
        margin = Insets(10)
      },
      new Text {
        text = "→"
        margin = Insets(10)
      },
      name_field,
      new Text {
        text = "→"
        margin = Insets(10)
      },
      new Button {
        text = "Return to game"
        margin = Insets(10)

        onMouseClicked = (e: MouseEvent) => {
          onFinished.run()
          //back to game screen
          /*
          if (nb_towns.get() >= min_towns && name_field.text.get().length() > 0) {
            game.playerName = name_field.text.get()
            onValidate.run()
*/          }


      },
      new Text {
        text = "|"
        margin = Insets(10)
      },
      // new Button {
      //   text = "Reset"
      //   margin = Insets(10)
      //
      //   onMouseClicked = (e: MouseEvent) => {
      //     nb_towns.set(0)
      //     name_field.text = ""
      //     game.removeAllTowns()
      //   }
      // }
    )
    onMouseClicked = (e: MouseEvent) => { requestFocus() }
  }

}
