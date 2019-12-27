package com.example.quickstart.interpreter
import com.example.quickstart.algebra.KvStoreAlg

object KvStoreInterpreter {
  import cats.mtl.MonadState

  def InMemoryStateBased[F[_], K, V](s: MonadState[F, Map[K, V]]): KvStoreAlg[F, K, V] =
    new KvStoreAlg[F, K, V] {
      def put(key: K, value: V): F[Unit] = s.modify(_ + (key -> value))
      def get(key: K): F[Option[V]]      = s.inspect(_.get(key))
      def remove(key: K): F[Unit]        = s.modify(_ - key)
    }
}
