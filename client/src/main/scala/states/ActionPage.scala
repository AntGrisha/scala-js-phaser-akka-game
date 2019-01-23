package state

import com.definitelyscala.phaser._
import com.definitelyscala.phaser.Physics.Arcade.Body

import scala.scalajs.js
import scala.scalajs.js.timers._
import scala.collection.mutable._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js.JSON
import scala.scalajs.js.JSConverters._

import scala.util.Random
import style.ScoreStyle
import misc._


class ActionPage {

  var game: Game = _
  var player: Sprite = _

  var platforms: Group = _
  var stars: Group = _
  var meteors: Group = _

  var cursors: CursorKeys = _
  var spacebarKey: Key = _

  var score: Int = 0
  var time: Int = 0
  var scoreText: Text = _
  var timeText: Text = _
  var deadText: Text = _

  var clearIntervals = () => {}

  def preload(game: Game): Unit = {
    this.game = game
    game.load.image("logo", "assets/images/phaser.png")
    game.load.image("sky", "assets/images/background-results.png")
    game.load.image("ground", "assets/images/platform.png")
    game.load.image("star", "assets/images/star.png")
    game.load.image("meteor", "assets/images/meteor.png")
    game.load.spritesheet("dude", "assets/images/dude.png", 32, 48)
  }

  def create(game: Game): Unit = {
    game.physics.startSystem(PhysicsObj.ARCADE)
    game.add.sprite(0, 0, "sky")
    this.score = 0
    this.time = 0
    // platform group defined, used for collision testing, add here the floor and ledges
    platforms = game.add.group()
    platforms.enableBody = true

    // declaring ground, scaling it and making immovable, because it's a floor :)
    val ground= platforms.create(0, game.world.height - 64, "ground").asInstanceOf[Sprite]

    ground.scale.set(2,2)

    ground.body match {
      case body: Body =>
        body.immovable = true
    }

    // Same as ground
    // val ledge1 = platforms.create(400, 400, "ground")
    // ledge1.body.immovable = true
    // val ledge2 = platforms.create(-150, 250, "ground")
    // ledge2.body.immovable = true

    // Dude is now alive!
    player = game.add.sprite(45, game.world.height-200, "dude")

    // Adding physics to our dude
    game.physics.arcade.enable(player)

    player.body match {
      case body: Body =>
        body.bounce.y = 0.3
        body.gravity.y = 800
        body.collideWorldBounds = true
    }

    // Adding animations to our dude
    player.animations.add("left",Array[Double](0,1,2,3).toJSArray, 10, true)
    player.animations.add("right",Array[Double](5,6,7,8).toJSArray,10, true)

    // Declaring the keys to move our dude
    spacebarKey = game.input.keyboard.addKey(KeyCode.SPACEBAR)
    game.input.keyboard.addKeyCapture(KeyCode.SPACEBAR)
    cursors = game.input.keyboard.createCursorKeys()

    stars = game.add.group()
    stars.enableBody = true

    meteors = game.add.group()
    meteors.enableBody = true

    // Creating the score
    scoreText = game.add.text(16, 16, "score: 0", ScoreStyle("32px", "#fff"), game.world)
    timeText = game.add.text(16, 50, "Time: 0", ScoreStyle("32px", "#fff"), game.world)

    val handleRain = setInterval(300) {
          val meteor = meteors.create(Random.nextDouble() * 800, 0, "meteor").asInstanceOf[Sprite]
          meteor.scale.set(0.5,0.5)
          meteor.body match {
            case body: Body =>
            // make random
              body.gravity.y = 105
              body.setSize(100, 50, 50, 25);
          }
    }

    val handleStars = setInterval(1000) {
          val star = stars.create(Random.nextDouble() * 800, 0, "star").asInstanceOf[Sprite]
          star.body match {
            case body: Body =>
              body.gravity.y = 105
              body.collideWorldBounds = true
              body.bounce.y = 0.3
          }
    }

    val handleTime =  setInterval(1000) { 
      time += 1
      timeText.text = s"Time: $time"
    }

    this.clearIntervals = () => {
      clearInterval(handleRain)
      clearInterval(handleStars)
      clearInterval(handleTime)
    }

  }

  val collectStar: js.Function2[Sprite, Sprite, Sprite] =
    (player: Sprite, star: Sprite) => {
    score += 10
    scoreText.text = s"Score: $score"
    star.kill()
  }

  val destroyStar: js.Function2[Sprite, Sprite, Sprite] =
    (star: Sprite, platform: Sprite) => {
    star.kill()
  }

  val collectMeteor: js.Function2[Sprite, Sprite, Sprite] =
    (player: Sprite, meteor: Sprite) => {
    meteor.kill()
    //game.state.start("Result", true, false)
  }

  val destroyMeteor: js.Function2[Sprite, Sprite, Sprite] =
    (meteor: Sprite, platform: Sprite) => {
    meteor.kill()
  }

  // game end   
  def endGame(player: Sprite, meteor: Sprite) : Unit = {
      this.clearIntervals()

      meteor.kill()
      player.kill()
      deadText = game.add.text(game.width/2, game.height/2, "YOU ARE DEAD", ScoreStyle("32px", "#fff"), game.world)
      deadText.anchor.set(0.5,0.5)
      
      setTimeout(1000) {
        var score = new Score(Results.getUserName(), this.score, this.time)
        ApiController.sendScores(score).map(response =>
          {
              Results.setCurrentScore(score)
              ApiController.updateScores().map(xhr =>
                {
                  val rawArray:js.Dynamic = JSON.parse(xhr.responseText)
                  val myArray = rawArray.asInstanceOf[js.Array[ScoreJS]]
                  var list: MutableList[Score] = MutableList()
                  for (scoreJS <- myArray){
                    var score = new Score(scoreJS.username, scoreJS.points, scoreJS.time)
                    list += score
                  }
                  Results.setTopScores(list)
                  this.game.state.start("Result", true, false)
              })
          })
      }     
  }

// TODO: stars in update
  def update(game: Game): Unit = {

    // Physics
    game.physics.arcade.collide(player, platforms)
    game.physics.arcade.collide(stars, platforms)
    game.physics.arcade.overlap(meteors, platforms, destroyMeteor, null)
    game.physics.arcade.overlap(player, stars, collectStar, null)
    game.physics.arcade.overlap(player, meteors, endGame _, null)


    // Movement, gravity, animations
    player.body match {
      case body: Body =>
        if (cursors.left.isDown) {
          body.velocity.x = -150
          player.animations.play("left")
        } else if (cursors.right.isDown) {
          body.velocity.x = 150
          player.animations.play("right")
        } else {
          player.animations.stop()
          body.velocity.x = 0
          player.frame = 4
        }
        if (spacebarKey.isDown && body.touching.down) {
          body.velocity.y = -550
        }
    }
  }

}

@js.native
trait ScoreJS extends js.Object {
  val username: String = js.native
  val points: Int = js.native
  val time: Int = js.native
}