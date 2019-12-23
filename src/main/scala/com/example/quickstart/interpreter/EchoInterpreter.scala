package com.example.quickstart.interpreter
import com.example.quickstart.algebra.EchoAlg
import cats.Applicative
import cats.implicits._

object EchoInterpreter {
  def impl[F[_]: Applicative] = new EchoAlg[F] {
    def echo(value: EchoAlg.Repeat): F[EchoAlg.Say] = EchoAlg.Say(value.value).pure[F]
  }
}
