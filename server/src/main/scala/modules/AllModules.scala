package modules

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.softwaremill.macwire._
import endpoints.ScoreEndpoints
import models.repository.ScoreRepository
import config.Mongo

import scala.concurrent.ExecutionContext

class AllModules extends EndpointModule

trait EndpointModule extends AkkaModules with RepositoryModule {
  lazy val scoreEndpoints = wire[ScoreEndpoints]
}

trait MongoModule {
  lazy val codecRegistry = Mongo.codecRegistry
  lazy val scoreCollection = Mongo.scoreCollection
}

trait RepositoryModule extends AkkaModules with MongoModule {
  lazy val scoreRepository = wire[ScoreRepository]
}

trait AkkaModules {
  implicit lazy val system = ActorSystem("simpleHttpServerJson")
  implicit lazy val materializer = ActorMaterializer()
  implicit lazy val executor: ExecutionContext = system.dispatcher
}