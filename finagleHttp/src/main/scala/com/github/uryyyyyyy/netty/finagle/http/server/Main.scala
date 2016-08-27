package com.github.uryyyyyyy.netty.finagle.http.server

import com.twitter.finagle.http.service.RoutingService
import com.twitter.finagle.http.{Cookie, Request, Response}
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

      def apply(request: Request): Future[http.Response] = {
        val resultString = s"user id: ${userId}"
        val res = http.Response(request.version, http.Status.Ok)
        res.setContentString(resultString)

        val cookie = new Cookie("key", "value")
        cookie.httpOnly_=(true)
        res.addCookie(cookie)
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
