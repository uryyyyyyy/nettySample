name := """nettySample"""

version := "1.0"

lazy val nettyVersion = "4.1.0.CR7"

lazy val commonSettings = Seq(
	organization := "com.github.uryyyyyyy",
	scalaVersion := "2.11.7",
	libraryDependencies ++= Seq(
		"io.netty" % "netty-all" % "4.1.0.CR7",
		"org.scalatest" %% "scalatest" % "2.2.4" % "test",
		"junit" % "junit" % "4.12" % "test"
	)
)

lazy val helloWorld = (project in file("helloWorld")).
		settings(commonSettings: _*)