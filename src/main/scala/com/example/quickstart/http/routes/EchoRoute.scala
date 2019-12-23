package com.example.quickstart.http.routes

import com.example.quickstart.algebra.EchoAlg
import com.example.quickstart.algebra.EchoAlg._
import com.example.quickstart.http.Encoders._
import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object EchoRoute {
  def route[F[_]: Sync](E: EchoAlg[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "echo" / value =>
        for {
          received <- E.echo(Repeat(value))
          resp     <- Ok(received)
        } yield resp
    }
  }
}
