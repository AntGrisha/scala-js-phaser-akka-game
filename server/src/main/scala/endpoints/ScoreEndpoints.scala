package endpoints

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.{ActorMaterializer, Materializer}


import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._

import shared.SharedMessages
import models._
import models.repository._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class ScoreEndpoints(repository: ScoreRepository)(implicit ec: ExecutionContext, mat: Materializer) {

  val scoreRoutes =
    pathPrefix("api" / "scores") {
      (get & pathEndOrSingleSlash) {
        onComplete(repository.findAll()) {
          case Success(scores) =>
            complete(Marshal(scores).to[ResponseEntity].map { e => HttpResponse(entity = e) })
          case Failure(e)          =>
            complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
        }
      } ~ (post & pathEndOrSingleSlash & entity(as[Score])) { score =>
        onComplete(repository.save(score)) {
          case Success(id) =>
            complete(HttpResponse(status = StatusCodes.Created, headers = List(Location(s"/api/scores/$id"))))
          case Failure(e)  =>
            complete(Marshal(Message(e.getMessage)).to[ResponseEntity].map { e => HttpResponse(entity = e, status = StatusCodes.InternalServerError) })
        }
      }
    }


}