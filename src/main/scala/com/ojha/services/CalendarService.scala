package com.ojha.services

import cats.effect._
import com.ojha.InMemoryEntryStore
import com.ojha.model.Entry
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

import io.circe.syntax._

class CalendarService {

  val store: InMemoryEntryStore = new InMemoryEntryStore

  val calendarService: HttpService[IO] = HttpService[IO] {

    case GET -> Root / "entry" => Ok(store.getAll.asJson)

    case req @ PUT -> Root / "entry" => {
      req.decode[Entry] { entry =>
        store.put(entry)
        Ok()
      }
    }

  }

}