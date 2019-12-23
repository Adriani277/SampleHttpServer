package com.example.quickstart.http.routes

import com.example.quickstart.algebra.HelloWorldAlg
import com.example.quickstart.algebra.HelloWorldAlg._
import com.example.quickstart.http.Encoders._
import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object HelloWorldRoutes {
  def route[F[_]: Sync](H: HelloWorldAlg[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorldAlg.Name(name))
          resp     <- Ok(greeting)
        } yield resp
    }
  }
}
