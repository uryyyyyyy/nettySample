name := """nettySample"""

version := "1.0"

lazy val commonSettings = Seq(
  organization := "com.github.uryyyyyyy",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.0" % "test"
  )
)

lazy val helloWorld = (project in file("helloWorld"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "io.netty" % "netty-all" % "4.1.4.Final"
    )
  )

lazy val finagleHttp = (project in file("finagleHttp"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.twitter" %% "finagle-netty4-http" % "6.37.0"
    ),
    mainClass in assembly := Some("com.github.uryyyyyyy.netty.finagle.http.server.Main")
  )