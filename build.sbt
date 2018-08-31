name := "cyclical"

version := "0.1"

scalaVersion := "2.12.6"

val http4sVersion = "0.18.16"

// Only necessary for SNAPSHOT releases
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,

  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-generic" % "0.10.0-M1",
  "io.circe" %% "circe-literal" % "0.10.0-M1",


  //  "org.scalactic" %% "scalactic" % "3.0.5",

  // testing
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"

)


addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

scalacOptions ++= Seq("-Ypartial-unification")