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
//    javaOptions in run += "-Xbootclasspath/p:/your/path/nettySample/alpn-boot-8.1.9.v20160720.jar"
  )

//java -cp helloWorld-assembly-0.1-SNAPSHOT.jar -Xbootclasspath/p:/media/shiba/shibaHDD/develop/git/nettySample/alpn-boot-8.1.9.v20160720.jar com.github.uryyyyyyy.netty.http2Example.Http2Server

lazy val finagleHttp = (project in file("finagleHttp"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.twitter" %% "finagle-netty4-http" % "6.37.0"
    )
  )

lazy val akkaRemote = (project in file("akkaRemote"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-remote" % "2.4.10"
    )
  )
