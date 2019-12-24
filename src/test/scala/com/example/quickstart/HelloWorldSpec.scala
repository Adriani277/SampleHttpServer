package com.example.quickstart

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.specs2.matcher.MatchResult
import com.example.quickstart.interpreter.HelloWorldInterpreter
import com.example.quickstart.http.routes.HelloWorldRoutes
import cats.effect.Sync
import cats.effect.ExitCase

class HelloWorldSpec extends org.specs2.mutable.Specification {

  implicit val idSync = new Sync[cats.Id] {
    def pure[A](x: A): cats.Id[A]                                                  = x
    def flatMap[A, B](fa: cats.Id[A])(f: A => cats.Id[B]): cats.Id[B]              = f(fa)
    def suspend[A](thunk: => cats.Id[A]): cats.Id[A]                               = thunk
    def handleErrorWith[A](fa: cats.Id[A])(f: Throwable => cats.Id[A]): cats.Id[A] = fa
    def raiseError[A](e: Throwable): cats.Id[A]                                    = ???
    def tailRecM[A, B](a: A)(f: A => cats.Id[Either[A, B]]): cats.Id[B]            = ???
    def bracketCase[A, B](acquire: cats.Id[A])(use: A => cats.Id[B])(
        release: (A, ExitCase[Throwable]) => cats.Id[Unit]
    ): cats.Id[B] = use(acquire)
  }

  "HelloWorld" >> {
    "return 200" >> {
      uriReturns200()
    }
    "return hello world" >> {
      uriReturnsHelloWorld()
    }
  }

  private[this] val retHelloWorld: Response[cats.Id] = {
    val getHW      = Request[cats.Id](Method.GET, uri"/hello/world")
    val helloWorld = HelloWorldInterpreter.impl[cats.Id]
    HelloWorldRoutes.route(helloWorld).orNotFound(getHW)
  }

  private[this] def uriReturns200(): MatchResult[Status] =
    retHelloWorld.status must beEqualTo(Status.Ok)

  private[this] def uriReturnsHelloWorld(): MatchResult[String] =
    retHelloWorld.as[String] must beEqualTo("{\"message\":\"Hello, world\"}")
}
