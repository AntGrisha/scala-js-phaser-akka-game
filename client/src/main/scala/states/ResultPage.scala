package state

import com.definitelyscala.phaser._

import scala.scalajs.js
import style.ScoreStyle
import misc.Results
import misc.Score

class ResultPage {
  
  var game: Game = _

  def preload(game: Game): Unit = {
    this.game = game
		game.load.image("background-result", "assets/images/background-results.png")
  }

  def create(game: Game): Unit = {
    var background = game.add.sprite(0, 0, "background-result")
    // background.scale.set(0.5,0.5)
    var btnPlay = game.add.button(game.width/2,500,"button", startGame _)
    btnPlay.scale.set(0.6,0.6)
		btnPlay.anchor.set(0.5,0.5)

		var userScore = Results.getCurrentScore()

		var txtUserScore = game.add.text(game.width/2, 60, userScore.toString(), ScoreStyle("30px", "#fff"), game.world)
		txtUserScore.anchor.set(0.5,0.5)

		var txtTopScores = game.add.text(game.width/2, 140, "TOP RESULTS", ScoreStyle("28px", "#fff"), game.world)
		txtTopScores.anchor.set(0.5,0.5)
		var yPos: Int = 200
		var topScores = Results.getTopScores()

		for ( score <- topScores){
			var txtTopScore = game.add.text(game.width/2, yPos, score.toString(), ScoreStyle("22px", "#fff"), game.world)
			txtTopScore.anchor.set(0.5,0.5)
			yPos += 30
		}

		var txtTryAgain = game.add.text(game.width/2, 400, "TRY AGAIN!", ScoreStyle("28px", "#fff"), game.world)
		txtTryAgain.anchor.set(0.5,0.5)
		}

		def update(game: Game): Unit = {
		}

		def startGame(): Unit = {
			this.game.state.start("Action", true, false)
		}
}