package com.example.quickstart

import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import cats.implicits._
import fs2.Stream
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import scala.concurrent.ExecutionContext.global
import com.example.quickstart.http.routes.HelloWorldRoutes
import com.example.quickstart.interpreter.HelloWorldInterpreter
import com.example.quickstart.interpreter.RepoInterpreter
import com.example.quickstart.interpreter.EchoInterpreter
import scala.collection.concurrent.TrieMap
import com.example.quickstart.http.routes.StorageRoute
import com.example.quickstart.http.routes.EchoRoute
import com.example.quickstart.http.routes.JokesRoute
import com.example.quickstart.interpreter.JokesInterpreter

object QuickstartServer {

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    val db: TrieMap[String, String] = TrieMap.empty

    for {
      client <- BlazeClientBuilder[F](global).stream
      helloWorldAlg = HelloWorldInterpreter.impl[F]
      jokeAlg       = JokesInterpreter.impl[F](client)
      echoAlg       = EchoInterpreter.impl[F]
      storageAlg    = RepoInterpreter.inMemoryRepo.apply(db)
      _             = db.put("Test", "Test")

      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract a segments not checked
      // in the underlying routes.
      httpApp = (
        HelloWorldRoutes.route[F](helloWorldAlg) <+>
          JokesRoute.route[F](jokeAlg) <+>
          EchoRoute.route[F](echoAlg) <+>
          StorageRoute.route[F](storageAlg)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- BlazeServerBuilder[F]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}
