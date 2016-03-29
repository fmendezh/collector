package org.gbif

import akka.actor.{ActorSystem, Actor}
import akka.event.Logging
import akka.pattern.ask
import akka.io.IO
import akka.util.Timeout
import com.typesafe.config.{ConfigRenderOptions, ConfigFactory}
import spray.can.Http
import spray.http._
import spray.routing._
import spray.client.pipelining._
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.json._
import HttpMethods._
import spray.http.HttpResponse
import scala.concurrent._
import scala.concurrent.duration._
import scala.collection.JavaConversions._
import scala.util.Try

class Collector extends Actor with CollectorRoute {
  implicitly[spray.routing.RoutingSettings](RoutingSettings.default(context))
  implicit val rSettings = RoutingSettings.default(context)

  def actorRefFactory = context
  def receive = runRoute(route)
}

trait CollectorRoute extends HttpService {

  implicit def executionContext: ExecutionContextExecutor = actorRefFactory.dispatcher
  implicit val system: ActorSystem = ActorSystem()

  val log = Logging(system, getClass)

  val config = ConfigFactory.load()

  val backends = config.getObjectList("backends") map {backend => backend.get("name").render().replaceAll("\"","") -> backend.get("url").render().replaceAll("\"","")}

  val route =  {
    path("search") {
      get {
            respondWithMediaType(MediaTypes.`application/json`) {
              (ctx: RequestContext) => ctx.complete {
                val results = Future sequence backends.map(backend => search(backend._2, backend._1, ctx.request.message.uri.query))
                val responses = Await result(results, 2 seconds)
                responses.map(response => (response._1, response._2.entity.asString)).toMap.toJson
              }
            }
      }
    }
  }


  def search(uri: String, backend: String, query: Uri.Query) : Future[(String,HttpResponse)] = {
    try {
      log.debug(Uri(uri).toString())
      val pipeline = sendReceive
      pipeline(Get(Uri(uri).withQuery(query)))
        .map(response => (backend, response))
    } catch {
      case ex : Throwable =>
        log.error(ex,"Error  URI")
        throw ex
    }
  }
}
