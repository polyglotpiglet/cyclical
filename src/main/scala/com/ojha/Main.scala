package com.ojha

import cats.effect._
import services.CalendarService
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext.Implicits.global


//object Main extends StreamApp[IO] {

//  val store: InMemoryEntryStore = new InMemoryEntryStore
//
//  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] =
//    BlazeBuilder[IO]
//      .bindHttp(8080, "localhost")
//      .mountService(CalendarService.calendarService, "/")
//      .serve
//}

/*
To do:
- look at Chris examples
- test calendar service
- implement data store with doobie
- implement other http functions for calendar store

Then
- make a quick ui

 */
