package com.ojha.services

import cats.effect.IO
import com.ojha.model.Entry
import io.circe.{Encoder, Json}
import io.circe.generic.semiauto._
import org.http4s.circe.jsonOf
import org.http4s.dsl.io._
import org.http4s.{EntityDecoder, Message, Method, Request, Response, Status, Uri}
import org.scalatest.{Matchers, WordSpec}

class CalendarServiceSpec extends WordSpec with Matchers {

  implicit val decoder: EntityDecoder[IO, Json] = jsonOf[IO, Json]

  private val repo = new InMemoryEntryRepo

  "The calendar service" must {
    "return a valid entry via get" in {

      implicit val UserEncoder: Encoder[Entry] = deriveEncoder[Entry]

      repo.createOrUpdate(Entry(20180101, period = false))

      val programme: IO[Response[IO]] = CalendarService.calendarService[IO](repo).orNotFound.run(
        Request(method = Method.GET, uri = Uri.uri("/entry/20180101"))
      )

      val expectedJson = Json.obj(
        ("date", Json.fromInt(20180101)),
        ("period", Json.fromBoolean(false))
      )


      val response: Response[IO] = programme.unsafeRunSync
      response.status shouldBe Status.Ok

      response.as[Json].unsafeRunSync shouldBe expectedJson

    }

    //    "return a valid entry via get" in {
    //
    //      implicit val UserEncoder: Encoder[Entry] = deriveEncoder[Entry]
    //
    //      repo.createOrUpdate(Entry(20180101, period = false))
    //
    //      val response: IO[Response[IO]] = CalendarService.calendarService[IO](repo).orNotFound.run(
    //        Request(method = Method.GET, uri = Uri.uri("/entry/20180101") )
    //      )
    //
    //      val expectedJson = Json.obj(
    //        ("date", Json.fromInt(20180101)),
    //        ("period",  Json.fromBoolean(false))
    //      )
    //
    //      check[Json](response, Status.Ok, Some(expectedJson)) shouldBe true
    //    }
  }




}
