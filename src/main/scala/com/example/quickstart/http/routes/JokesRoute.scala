package com.example.quickstart.http.routes

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import com.example.quickstart.algebra.JokesAlg
import com.example.quickstart.http.Encoders._

object JokesRoute {
  def route[F[_]: Sync](J: JokesAlg[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "joke" =>
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
    }
  }
}
