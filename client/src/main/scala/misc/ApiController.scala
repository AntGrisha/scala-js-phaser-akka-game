package misc
import scala.scalajs.js
import scala.scalajs.js.JSON
import js.Dynamic.{literal => lit}
import org.scalajs.dom.{XMLHttpRequest, Event}
import upickle.default._
import scala.concurrent.{Promise, Future}
import scala.collection.mutable._
import scala.collection.immutable.Map
import org.scalajs.dom.ext._
import org.scalajs.dom.ext.Ajax.InputData

object ApiController {
	def updateScores(): Future[XMLHttpRequest] = {
		   return Ajax.get("/api/scores")
	}

	def sendScores(score: Score): Future[XMLHttpRequest] = {
		// var scoreJS = new ScoreJS(score.getUsername(), score.getPoints(), score.getTime())
		val scoreJS = lit(username = score.getUsername(), points = score.getPoints(), time = score.getTime())
		val ajaxReq: Map[String, String] = Map.empty
		return Ajax.post("/api/scores", JSON.stringify(scoreJS), 2000, ajaxReq + ("Content-Type" -> "application/json"))
	}
}
