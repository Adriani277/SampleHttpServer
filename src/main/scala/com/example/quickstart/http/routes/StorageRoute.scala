package com.example.quickstart.http.routes
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.Sync
import com.example.quickstart.algebra.RepoAlg
import com.example.quickstart.http.Encoders._
import cats.implicits._
import cats.Applicative._
import cats.Applicative

object StorageRoute {
  def route[F[_]: Sync](repo: RepoAlg[F, String]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "storage" => Ok(repo.readAll)
      case req @ PUT -> Root / "storage" / id =>
        req.decode[String] { body =>
          Ok(
            for {
              _    <- repo.create(id, body)
              resp <- repo.readAll
            } yield resp
          )
        }
      case DELETE -> Root / "storage" / id =>
        Ok(
          for {
            _   <- repo.delete(id)
            res <- repo.readAll
          } yield res
        )
    }
  }
}
