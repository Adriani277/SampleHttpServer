package com.example.quickstart.interpreter
import cats.data.`package`.Reader
import scala.collection.concurrent.TrieMap
import com.example.quickstart.algebra.RepoAlg
import cats.effect.Sync

object RepoInterpreter {
  def inMemoryRepo[F[_]: Sync]: Reader[TrieMap[String, String], RepoAlg[F, String]] = Reader { db =>
    new RepoAlg[F, String] {
      def create(id: String, value: String): F[Unit] = Sync[F].delay(db.put(id, value))
      def delete(value: String): F[Unit]             = Sync[F].delay(db.remove(value))
      def read(id: String): F[Option[String]]        = Sync[F].delay(db.get(id))
      def readAll: F[List[String]]                   = Sync[F].delay(db.values.toList)
      def update(id: String, value: String): F[Unit] = Sync[F].delay(db.update(id, value))
    }
  }
}
