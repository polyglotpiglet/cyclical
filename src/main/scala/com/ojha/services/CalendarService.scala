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

  implicit val decoder: EntityDecoder[IO, Entry] = jsonOf[IO, Entry]

  trait EntryRepo[F[_]] {
    def find(userId: String): F[Option[Entry]]
  }

  def calendarService[F[_]](repo: EntryRepo[F])(implicit F: Effect[F]): HttpService[F] = HttpService[F] {

    case GET -> Root / "entry" / date =>  repo.find(date).flatMap {
      case Some(entry) => Response(status = Status.Ok).withBody(entry.asJson)
      case None       => F.pure(Response(status = Status.NotFound))
    }
  }


}
