package com.ojha.services

import cats.effect._
import cats.implicits._
import com.ojha.model.Entry
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._

object CalendarService {

  implicit val entryDecoder: EntityDecoder[IO, Entry] = jsonOf[IO, Entry]
  implicit val newEntryDecoder: EntityDecoder[IO, InboundEntry] = jsonOf[IO, InboundEntry]


  def calendarService[F[_]](repo: EntryRepo[F])(implicit F: Effect[F]): HttpService[F] = HttpService[F] {

    case GET -> Root / "entry" / IntVar(date) => repo.get(date).flatMap {
      case Some(entry) => Response(status = Status.Ok).withBody(entry.asJson)
      case None => F.pure(Response(status = Status.NotFound))
    }

    case req@PUT -> Root / "entry" / IntVar(date) => req.decodeJson[InboundEntry].flatMap(entry => {
      repo.createOrUpdate(createEntry(date, entry)).flatMap(_ => F.pure(Response(status = Status.Created)))
    })

    case DELETE -> Root / "entry" / IntVar(date) => repo.delete(date).flatMap {
      case Some(_) => F.pure(Response(status = Status.Ok))
      case None => F.pure(Response(status = Status.NotFound))
    }

  }

  private def createEntry(date: Int, newEntry: InboundEntry): Entry = Entry(date, newEntry.period)

  case class InboundEntry(period: Boolean)


}
