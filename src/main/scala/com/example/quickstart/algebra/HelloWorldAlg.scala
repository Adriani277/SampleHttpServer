package com.example.quickstart.algebra

trait HelloWorldAlg[F[_]] {
  def hello(n: HelloWorldAlg.Name): F[HelloWorldAlg.Greeting]
}

object HelloWorldAlg {
  implicit def apply[F[_]](implicit ev: HelloWorldAlg[F]): HelloWorldAlg[F] = ev

  final case class Name(name: String)         extends AnyVal
  final case class Greeting(greeting: String) extends AnyVal
}
