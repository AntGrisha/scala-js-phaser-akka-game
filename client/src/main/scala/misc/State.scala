package misc

import com.definitelyscala.phaser._
import scala.scalajs.js
// This state is the way scala.js models a Javascript literal object -> https://www.w3schools.com/js/js_objects.asp
object State {
  def apply(preload: (Game) => Unit,  create: (Game) => Unit, update: (Game) => Unit): State = {
    js.Dynamic.literal(preload = preload, create = create, update = update).asInstanceOf[State]
  }
}

@js.native
trait State extends js.Object {
  def preload: Unit = js.native
  def create: Unit = js.native
  def update: Unit = js.native
}