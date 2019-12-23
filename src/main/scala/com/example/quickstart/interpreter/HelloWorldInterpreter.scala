package com.example.quickstart.interpreter
import cats.implicits._
import cats.Applicative
import com.example.quickstart.algebra.HelloWorldAlg._
import com.example.quickstart.algebra.HelloWorldAlg

object HelloWorldInterpreter {
  def impl[F[_]: Applicative]: HelloWorldAlg[F] = new HelloWorldAlg[F] {
    def hello(n: Name): F[Greeting] =
      Greeting("Hello, " + n.name).pure[F]
  }
}
