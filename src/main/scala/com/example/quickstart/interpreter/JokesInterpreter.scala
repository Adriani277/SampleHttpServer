package com.example.quickstart.interpreter

import org.http4s.client.Client
import cats.effect.Sync
import com.example.quickstart.algebra.JokesAlg.{Joke, JokeError}
import org.http4s.client.dsl.Http4sClientDsl
import cats.implicits._
import com.example.quickstart.algebra.JokesAlg
import org.http4s.{EntityDecoder, EntityEncoder, Method, Uri, Request}
import org.http4s.Method._
import org.http4s.implicits._
import org.http4s._
import com.example.quickstart.http.Encoders._

object JokesInterpreter {
  def impl[F[_]: Sync](C: Client[F]) = new JokesAlg[F] {
    val dsl = new Http4sClientDsl[F] {}
    import dsl._
    def get: F[Joke] =
      C.expect[Joke](GET(uri"https://icanhazdadjoke.com/"))
        .adaptError { case t => JokeError(t) } // Prevent Client Json Decoding Failure Leaking
  }
}
