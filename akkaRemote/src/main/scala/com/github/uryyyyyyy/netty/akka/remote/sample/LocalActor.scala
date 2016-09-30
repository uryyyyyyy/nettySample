package com.github.uryyyyyyy.netty.akka.remote.sample

import java.io.File

import akka.actor.{ActorLogging, Props, ActorSystem, Actor}
import com.typesafe.config.ConfigFactory

class LocalActor extends Actor with ActorLogging {
  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    val remoteActor = context.actorSelection(LocalApp.config.getString("app.remote-system.remote-actor"))
    log.info("Remote actor is {}", remoteActor)
    remoteActor ! "hi"
    remoteActor ! Hello
  }

  override def receive: Receive = {
    case msg: String =>
      log.info("Client received {} from {}", msg, sender)
    case Hello =>
      log.info("Client received {} from {}", Hello, sender)
    case any =>
      log.info("Client received unknown message {} from {}", any, sender)
  }
}

object LocalApp extends App {
  val configFile = getClass.getClassLoader.getResource("local_application.conf").getFile
  val config = ConfigFactory.parseFile(new File(configFile))
  val system = ActorSystem("client-system", config)
  val localActor = system.actorOf(Props[LocalActor], name = "local")
}