package com.example.quickstart.algebra
import cats.Monad
import cats.kernel.Eq
import cats.implicits._
import cats.tests.CatsSuite

final class KvStoreInMemoryTest extends CatsSuite {}

final class KvStoreLaws[F[_]: Monad, K: Eq, V](store: KvStoreAlg[F, K, V]) {}
