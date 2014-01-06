import play.Project._

name := "sample-crud-play-scala-style"

version := "1.0"

resolvers += "Sonatype" at "http://search.maven.org/remotecontent?filepath="

libraryDependencies ++= Seq(
  //test dependencies
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "junit" % "junit" % "4.11" % "test",
  //runtime dependencies
  "com.typesafe.slick" %% "slick" % "1.0.1",
  "com.typesafe.play" %% "play-slick" % "0.5.0.8",
  "org.hsqldb" % "hsqldb" % "2.3.1",
  //enable JDBC module for the project
  jdbc
)

playScalaSettings