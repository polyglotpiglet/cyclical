package com.ojha.services

import com.ojha.model.Entry

trait EntryRepo[F[_]] {

  def get(date: Int): F[Option[Entry]]

  def createOrUpdate(entry: Entry): F[Unit]

  def getAll: F[Iterable[Entry]]

  def delete(date: Int): F[Option[Entry]]

}
