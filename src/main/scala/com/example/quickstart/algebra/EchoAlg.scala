package com.example.quickstart.algebra
import cats.kernel.Eq
import cats.implicits._

trait EchoAlg[F[_]] {
  def echo(value: EchoAlg.Repeat): F[EchoAlg.Say]
}

object EchoAlg {
  implicit def apply[F[_]](implicit ev: EchoAlg[F]): EchoAlg[F] = ev

  final case class Repeat(value: String) extends AnyVal
  final case class Say(say: String)      extends AnyVal
  object Say {
    implicit val eq = new Eq[Say] {
      def eqv(x: Say, y: Say): Boolean = x.say === y.say
    }
  }
}
