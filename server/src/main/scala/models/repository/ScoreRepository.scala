package models.repository

import models.Score
import org.mongodb.scala._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.bson.ObjectId

import scala.concurrent.{ExecutionContext, Future}

class ScoreRepository(collection: MongoCollection[Score])(implicit ec: ExecutionContext) {
  def findAll(): Future[Seq[Score]] =
    collection
      .find()
	  .sort(orderBy(descending("points"), ascending("time")))
	  .limit(5)
	  .toFuture()
      

  def save(score: Score): Future[String] =
    collection
      .insertOne(score)
      .head
      .map { _ => score._id.toHexString }
}