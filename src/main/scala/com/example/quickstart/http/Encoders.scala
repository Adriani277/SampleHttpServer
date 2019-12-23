package com.example.quickstart.http
import com.example.quickstart.algebra.HelloWorldAlg._
import com.example.quickstart.algebra.EchoAlg._
import com.example.quickstart.algebra.JokesAlg._
import cats.Applicative
import cats.effect.Sync
import io.circe.{Encoder, Json, Decoder}
import org.http4s.{EntityEncoder, EntityDecoder}
import org.http4s.circe._
import io.circe.generic.semiauto._

object Encoders {
  implicit val greetingEncoder: Encoder[Greeting] = new Encoder[Greeting] {
    final def apply(a: Greeting): Json = Json.obj(
      ("message", Json.fromString(a.greeting))
    )
  }
  implicit def greetingEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Greeting] =
    jsonEncoderOf[F, Greeting]

  implicit def listEntityEncoder[F[_]: Applicative]: EntityEncoder[F, List[String]] =
    jsonEncoderOf[F, List[String]]

  implicit val echoEncoder: Encoder[Say] = new Encoder[Say] {
    final def apply(a: Say): Json = Json.obj(
      ("message", Json.fromString(a.say))
    )
  }

  implicit def sayEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Say] =
    jsonEncoderOf[F, Say]

  implicit val jokeDecoder: Decoder[Joke] = deriveDecoder[Joke]
  implicit def jokeEntityDecoder[F[_]: Sync]: EntityDecoder[F, Joke] =
    jsonOf
  implicit val jokeEncoder: Encoder[Joke] = deriveEncoder[Joke]
  implicit def jokeEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Joke] =
    jsonEncoderOf
}
