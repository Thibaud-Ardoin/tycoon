package tycoon

import tycoon.game.Game
import tycoon.ui.{CreditsScreen, GameCreationScreen, GameScreen, StartScreen}

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.{PerspectiveCamera, Scene}
import scalafx.scene.layout.{Pane, StackPane}


object Settings { // to be moved
  val gameWidth = 500
  val gameHeight = 500
}



object Main extends JFXApp {
  val game = new Game(Settings.gameWidth, Settings.gameHeight)

  val startScreen = new StartScreen()
  val gameCreationScreen = new GameCreationScreen(game)
  val gameScreen = new GameScreen(game)
  val creditsScreen = new CreditsScreen()

  val content = new StackPane()
  val appScene = new Scene(new StackPane(content))

  val appStage = new PrimaryStage {
    title = "Tycoon Game"
    resizable = true
    maximized = true
    minWidth = 800
    minHeight = 600
    scene = appScene
  }
  stage = appStage

  // initial screen
  content.getChildren().add(startScreen)

  def switchScreen(newScreen: Pane) = {
    content.getChildren.clear()
    content.getChildren.add(newScreen)
  }

  startScreen.setOnStartGame(new Runnable {
    def run() = {
      switchScreen(gameCreationScreen)
      gameCreationScreen.init()
      game.start()
    }
  })

  startScreen.setOnOpenCredits(new Runnable {
    def run() = {
      switchScreen(creditsScreen)
      appScene.camera = new PerspectiveCamera
      appStage.resizable = false
    }
  })

  gameCreationScreen.setOnValidate(new Runnable {
    def run() = {
      switchScreen(gameScreen)
      gameScreen.init()
    }
  })

  creditsScreen.setOnExit(new Runnable {
    def run() = {
      switchScreen(startScreen)
      appStage.resizable = true
    }
  })
}
