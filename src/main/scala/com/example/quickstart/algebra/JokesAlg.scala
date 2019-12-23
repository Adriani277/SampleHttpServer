package com.example.quickstart.algebra

trait JokesAlg[F[_]] {
  def get: F[JokesAlg.Joke]
}

object JokesAlg {
  def apply[F[_]](implicit ev: JokesAlg[F]): JokesAlg[F] = ev

  final case class Joke(joke: String)      extends AnyVal
  final case class JokeError(e: Throwable) extends RuntimeException
}
