package com.ojha.services

import cats.effect.IO
import com.ojha.model.Entry
import com.ojha.services.CalendarService.EntryRepo
import io.circe.{Encoder, Json}
import io.circe.generic.semiauto._
import org.http4s.circe.jsonOf
import org.http4s.dsl.io._
import org.http4s.{EntityDecoder, Method, Request, Response, Status, Uri}
import org.scalatest.{Matchers, WordSpec}

class CalendarServiceSpec extends WordSpec with Matchers {

  implicit val decoder: EntityDecoder[IO, Json] = jsonOf[IO, Json]

  "The calendar service" must {
    "return an entry" in {

      implicit val UserEncoder: Encoder[Entry] = deriveEncoder[Entry]

      val success: EntryRepo[IO] = (id: String) => IO.pure(Some(Entry(20180101, false)))

      val response: IO[Response[IO]] = CalendarService.calendarService[IO](success).orNotFound.run(
        Request(method = Method.GET, uri = Uri.uri("/entry/20180101") )
      )

      val expectedJson = Json.obj(
        ("date", Json.fromInt(20180101)),
        ("period",  Json.fromBoolean(false))
      )

      val happy = check[Json](response, Status.Ok, Some(expectedJson))
      happy shouldBe true

    }
  }



  def check[A](actual:        IO[Response[IO]],
               expectedStatus: Status,
               expectedBody:   Option[A])(
                implicit ev: EntityDecoder[IO, A]
              ): Boolean =  {
    val actualResp         = actual.unsafeRunSync
    val statusCheck        = actualResp.status == expectedStatus
    val bodyCheck          = expectedBody.fold[Boolean](
      actualResp.body.compile.toVector.unsafeRunSync.isEmpty)(
      expected => actualResp.as[A].unsafeRunSync == expected
    )
    statusCheck && bodyCheck
  }


}
