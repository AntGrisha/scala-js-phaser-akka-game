package example

import com.definitelyscala.phaser.Physics.Arcade.Body
import com.definitelyscala.phaser._

import scala.scalajs.js
import org.scalajs.dom

import state.StartPage
import state.ResultPage
import state.ActionPage
import style.ScoreStyle
import misc.Results
import misc.Score
import misc.State
import misc.ApiController

object ScalaJSGame extends js.JSApp {
  def main(): Unit = {

    var name = dom.window.prompt("Please enter your name", "Anonymous");
    name.trim
    if (name.isEmpty) {  
      name = "Guest"   
    }

    Results.setUserName(name)

    dom.document.getElementById("username").textContent = name
    dom.document.getElementById("scalajsShoutOut").textContent = "Scala.js is up & running!"


    val game = new Game(800, 600, Phaser.AUTO, "game")
    var actionState = new ActionPage
    var menuState = new StartPage
    var resultState = new ResultPage

    game.state.add("Menu", State(menuState.preload, menuState.create, menuState.update))
    game.state.add("Action", State(actionState.preload, actionState.create,actionState.update))
    game.state.add("Result", State(resultState.preload, resultState.create,resultState.update))
    game.state.start("Menu", true, false)
  }
}
