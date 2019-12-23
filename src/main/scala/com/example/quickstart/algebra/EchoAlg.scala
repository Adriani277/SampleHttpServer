package com.example.quickstart.algebra

trait EchoAlg[F[_]] {
  def echo(value: EchoAlg.Repeat): F[EchoAlg.Say]
}

object EchoAlg {
  implicit def apply[F[_]](implicit ev: EchoAlg[F]): EchoAlg[F] = ev

  final case class Repeat(value: String) extends AnyVal
  final case class Say(say: String)      extends AnyVal
}
