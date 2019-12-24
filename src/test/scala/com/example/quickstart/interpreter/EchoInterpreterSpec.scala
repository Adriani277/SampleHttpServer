package com.example.quickstart.interpreter

import org.scalatest.flatspec.AnyFlatSpec
import cats.implicits._
import com.example.quickstart.algebra.EchoAlg.Repeat
import com.example.quickstart.algebra.EchoAlg.Say
import org.specs2.matcher.MustMatchers
import org.scalatest.matchers.should.Matchers
import org.scalacheck.Prop._

class EchoInterpreterSpec extends AnyFlatSpec with Matchers {
  "echo" should "satisfy the identity law" in forAll { (s: String) =>
    EchoInterpreter.impl[cats.Id].echo(Repeat(s)) eqv Say(s)
  }
}
