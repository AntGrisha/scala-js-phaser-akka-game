package models

import io.circe.syntax._
import io.circe._

import org.bson.types.ObjectId


case class Score(_id: ObjectId, username: String, points: Int, time: Int) {
  require(username != null, "username not informed")
  require(username.nonEmpty, "username cannot be empty")
}

object Score {
  implicit val encoder: Encoder[Score] = (a: Score) => {
    Json.obj(
      "id" -> a._id.toHexString.asJson,
      "username" -> a.username.asJson,
      "points" -> a.points.asJson,
	    "time" -> a.time.asJson
    )
  }

  implicit val decoder: Decoder[Score] = (c: HCursor) => {
    for {
      username <- c.downField("username").as[String]
      points <- c.downField("points").as[Int]
	    time <- c.downField("time").as[Int]
    } yield Score(ObjectId.get(), username, points, time)
  }
}

case class Message(message: String)

object Message {
  implicit val encoder: Encoder[Message] = m => Json.obj("message" -> m.message.asJson)
}