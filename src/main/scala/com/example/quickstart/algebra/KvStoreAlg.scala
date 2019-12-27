package com.example.quickstart.algebra

import cats.Monad
import cats.kernel.Eq
import cats.implicits._

trait KvStoreAlg[F[_], K, V] {
  def put(key: K, value: V): F[Unit]
  def get(key: K): F[Option[V]]
  def remove(key: K): F[Unit]
}

object KvStoreAlg {
  def apply[F[_], K, V](implicit s: KvStoreAlg[F, K, V]): KvStoreAlg[F, K, V] = s
}

final class KvStoreLaws[F[_]: Monad, K: Eq, V](store: KvStoreAlg[F, K, V]) {
  import store._
  import cats.laws._

  def putGetPersistence(k: K, v: V) =
    put(k, v) *> get(k) <->
      put(k, v) *> v.some.pure[F]

  def getIdempotence(k: K, v: V) =
    put(k, v) *> get(k) *> get(k) <->
      put(k, v) *> get(k)

  def lastOverwriteWins(k: K, vOld: V, v: V) =
    put(k, vOld) *> put(k, v) *> get(k) *>
      put(k, v) *> get(k)

  def removeDeletes(k: K, v: V) =
    put(k, v) *> remove(k) *> get(k) <->
      none[V].pure[F]

  def canWriteAfterRemoe(k: K, v: V, v2: V) =
    put(k, v) *> remove(k) *> put(k, v2) *> get(k) <->
      put(k, v2) *> get(k)

  def putIndependence(k: K, v: V, k2: K, v2: V) =
    (k neqv k2) ==>
      put(k, v) *> put(k2, v2) *> (get(k), get(k2)).tupled <->
        put(k, v) *> put(k2, v2) *> (v.some.pure[F], v2.some.pure[F]).tupled

  def deleteIndependeceLeft(k: K, v: V, k2: K, v2: V) =
    (k neqv k2) ==>
      put(k, v) *> put(k2, v2) *> remove(k) *> get(k2) <->
        put(k2, v2) *> v2.some.pure[F]

  def deleteIndependeceRight(k: K, v: V, k2: K, v2: V) =
    (k neqv k2) ==>
      put(k, v) *> put(k2, v2) *> remove(k2) *> get(k) <->
        put(k, v) *> v.some.pure[F]

  implicit class ConditionalLaws(cond: Boolean) {
    def ==>[A](law: IsEq[F[A]]): IsEq[Option[F[A]]] =
      if (cond) mapResult(law)(_.some)
      else IsEq(None, None)

    private def mapResult[A, B](iseqA: IsEq[A])(f: A => B): IsEq[B] =
      IsEq(f(iseqA.lhs), f(iseqA.rhs))
  }
}
