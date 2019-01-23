package style

import scala.scalajs.js

object ScoreStyle {
  def apply(fontSize: String, fill: String): ScoreStyle = {
    js.Dynamic.literal(fontSize = fontSize, fill = fill).asInstanceOf[ScoreStyle]
  }
}

@js.native
trait ScoreStyle extends js.Object {
  val fontSize: String = js.native
  val fill: String = js.native
}