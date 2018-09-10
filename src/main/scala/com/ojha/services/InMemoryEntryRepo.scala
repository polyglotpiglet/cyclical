package com.ojha.services

import cats.effect.IO
import com.ojha.model.Entry

import scala.collection.mutable

class InMemoryEntryRepo extends EntryRepo[IO] {

  private val data = mutable.Map.empty[Int, Entry]

  override def get(date: Int): IO[Option[Entry]] = IO.pure(data.get(date))

  override def createOrUpdate(entry: Entry): IO[Unit] = IO.pure(data.put(entry.date, entry))

  override def getAll: IO[Iterable[Entry]] = {
    val sorted = data.values.toList.sortBy(b => b.date)
    IO.pure(sorted)
  }

  override def delete(date: Int): IO[Option[Entry]] = IO.pure(data.remove(date))

}
