package state

import com.definitelyscala.phaser.Physics.Arcade.Body
import com.definitelyscala.phaser._

import scala.scalajs.js
import style.ScoreStyle

class StartPage {

  var game: Game = _

  def preload(game: Game): Unit = {
    game.load.image("button", "assets/images/play-btn.png")
	game.load.image("name", "assets/images/name.png")
    game.load.image("background", "assets/images/background-menu.jpg")
    this.game = game
  }

  def create(game: Game): Unit = {
    var background = game.add.sprite(0, 0, "background")
    background.scale.set(0.5,0.5)
    var btnPlay = game.add.button(game.width/2, 470, "button", startGame _)
    btnPlay.scale.set(0.6,0.6)
	btnPlay.anchor.set(0.5,0.5)

	var name = game.add.sprite(game.width/2, 130, "name")
	name.scale.set(0.3,0.3)
	name.anchor.set(0.5,0.5)

	var txtInfo = game.add.text(game.width/2, 570, "COLLECT STARS! EVADE METEORS! SAVE THE GALAXY!", ScoreStyle("28px", "#fff"), game.world)
	txtInfo.anchor.set(0.5,0.5)
  }

  def update(game: Game): Unit = {
  }

  def startGame(): Unit = {
    this.game.state.start("Action", true, false)
  }

}