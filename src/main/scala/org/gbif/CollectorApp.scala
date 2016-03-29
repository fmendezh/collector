package org.gbif

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

object CollectorApp extends App {
  implicit val system = ActorSystem("collector-service")

  val service = system.actorOf(Props[Collector], "collector-service")

  //If we're on cloud foundry, get's the host/port from the env vars
  lazy val host = Option(System.getenv("VCAP_APP_HOST")).getOrElse("localhost")
  lazy val port = Option(System.getenv("VCAP_APP_PORT")).getOrElse("8080").toInt
  IO(Http) ! Http.Bind(service, host, port = port)

}
