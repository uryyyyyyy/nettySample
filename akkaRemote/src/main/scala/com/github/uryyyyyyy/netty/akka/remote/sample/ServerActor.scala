package com.github.uryyyyyyy.netty.akka.remote.sample


import java.io.File

import akka.actor.{ActorLogging, Props, ActorSystem, Actor}
import com.typesafe.config.ConfigFactory

object RemoteApp extends App {
  val configFile = getClass.getClassLoader.getResource("server_application.conf").getFile
  val config = ConfigFactory.parseFile(new File(configFile))
  val system = ActorSystem("remote-system", config)
  val remote = system.actorOf(Props[RemoteActor], name="remote")
  system.log.info("Remote is ready")
}

class RemoteActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case Hello =>
      log.info("Remote received {} from {}", Hello, sender)
      sender ! Hello
    case msg: String =>
      log.info("Remote received {} from {}", msg, sender)
      sender ! "hi"
    case any =>
      log.info("Remote received unknown message {} from {}", any, sender)
  }
}

case object Hello