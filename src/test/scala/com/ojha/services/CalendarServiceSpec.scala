package com.ojha.services

import cats.data.Kleisli
import cats.effect.IO
import cats.implicits._
import com.ojha.model.Entry
import io.circe.generic.semiauto._
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.literal._
import io.circe.{Encoder, Json}
import org.http4s.dsl.io._
import org.http4s.{EntityEncoder, Message, Method, Request, Response, Status, Uri}
import org.scalatest.{Matchers, WordSpec}
import org.http4s.circe._

class CalendarServiceSpec extends WordSpec with Matchers {

  implicit val encoder: Encoder[Entry] = deriveEncoder[Entry]
  implicit val inboundEncoder: Encoder[InboundEntry] = deriveEncoder[InboundEntry]
  implicit val inboundEntryEncoder: EntityEncoder[IO, InboundEntry] = null
  //jsonEncoder[IO, InboundEntry]
  //
  private val repo = new InMemoryEntryRepo

  "The calendar service" must {
    "return a valid entry via get if it exists" in {

      // given
      repo.createOrUpdate(Entry(20180101, period = false))

      // when
      val programme: IO[Response[IO]] = CalendarService.calendarService[IO](repo).orNotFound.run(
        Request(method = Method.GET, uri = Uri.uri("/entry/20180101"))
      )

      // then
      val expectedJson = Json.obj(
        ("date", Json.fromInt(20180101)),
        ("period", Json.fromBoolean(false))
      )

      val response: Response[IO] = programme.unsafeRunSync

      response.status shouldBe Status.Ok
      response.as[Json].unsafeRunSync shouldBe expectedJson

    }

    "put a valid entry" in {

      val inbound = InboundEntry(false)

      val request: IO[Response[IO]] = for {
        request <- Request[IO](method = Method.PUT, uri = Uri.uri("/entry/20180101")).withBody(inbound.asJson)
        service <- CalendarService.calendarService[IO](repo).orNotFound.run(request)
      } yield service

      val response = request.unsafeRunSync()

      response.status shouldBe Status.Created
      val storedData = repo.getAll.unsafeRunSync()
      storedData shouldBe Seq(Entry(20180101, false))
    }
  }


  case class InboundEntry(period: Boolean)


}
