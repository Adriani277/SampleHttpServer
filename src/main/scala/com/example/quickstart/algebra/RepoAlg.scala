package com.example.quickstart.algebra

trait RepoAlg[F[_], A] {
    def create(id: String, value: A): F[Unit]
    def read(id: String): F[Option[A]]
    def update(id: String, value: A): F[Unit]
    def delete(value: A): F[Unit]
    def readAll: F[List[A]]
  }