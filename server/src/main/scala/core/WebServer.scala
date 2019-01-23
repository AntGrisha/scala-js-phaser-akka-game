package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import scala.util.{Failure, Success}

import modules.AllModules

object WebServer {
  def main(args: Array[String]) {

    val modules = new AllModules
    import modules._

    implicit val system = ActorSystem("server-system")
    implicit val materializer = ActorMaterializer()

    val config = ConfigFactory.load()
    val interface = config.getString("http.interface")
    val port = config.getInt("http.port")

    val service = new WebService()

    var scoreRoutes = modules.scoreEndpoints.scoreRoutes
    var coreRoutes = service.route

    val routes = coreRoutes ~ scoreRoutes

    Http().bindAndHandle(routes, interface, port).onComplete {
          case Success(b) => system.log.info(s"application is up and running at ${b.localAddress.getHostName}:${b.localAddress.getPort}")
          case Failure(e) => system.log.error(s"could not start application: {}", e.getMessage)
    }

    println(s"Server online at http://$interface:$port")
  }
}
