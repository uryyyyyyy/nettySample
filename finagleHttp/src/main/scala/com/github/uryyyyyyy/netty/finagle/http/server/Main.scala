package com.github.uryyyyyyy.netty.finagle.http.server

import com.twitter.finagle.http.service.RoutingService
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service, http}
import com.twitter.util.{Await, Future}

object Main {

  def main (args: Array[String] ): Unit = {

    val action = new Service[Request, Response] {
      def apply(request: http.Request): Future[http.Response] = {
        val res = http.Response(request.version, http.Status.Ok)
        res.setContentString("Ok")
        Future.value(res)
      }
    }

    def execute(userId: String) = new Service[Request, Response] {

      def failWith(request: Request, e: Exception):Response = {
        e.printStackTrace()
        val res = http.Response(request.version, http.Status.BadRequest)
        res.setContentString("error")
        res
      }

      def complete(request: Request, s: String):Response = {
        val res = http.Response(request.version, http.Status.Ok)
        res.setContentString(s)
        Future.value(res)
        res
      }

      def apply(request: Request): Future[http.Response] = {
        val resultString = s"user id: ${userId}"
        val res = complete(request, resultString)
        Future.value(res)
      }
    }

    import com.twitter.finagle.http.path._
    val router = RoutingService.byPathObject[Request] {
      case Root / "userId" / userId => execute(userId)
      case Root / "userId" => execute("uuu")
      case _                             => action
    }

    // rpcサーバの起動
    val server = Http.serve(":9999", router)
    Await.ready(server)
  }

}
