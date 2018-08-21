package com.ojha

import cats.effect._
import com.ojha.services.CalendarService
import fs2.{Stream, StreamApp}
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext.Implicits.global
import fs2.StreamApp.ExitCode


object Main extends StreamApp[IO] {

//  val helloWorldService = HttpService[IO] {
//    case GET -> Root / "hello" / name =>
//      Ok(s"Hello, $name.")
//  }

  val calendarService = new CalendarService

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] =
    BlazeBuilder[IO]
      .bindHttp(8080, "localhost")
      .mountService(calendarService.calendarService, "/")
      .serve
}
