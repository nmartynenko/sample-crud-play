import play.Project._

name := "sample-crud-play-scala-style"

version := "1.0"

resolvers += "Sonatype" at "http://search.maven.org/remotecontent?filepath="

resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.io/releases/"))(Resolver.ivyStylePatterns)

resolvers += Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.io/snapshots/"))(Resolver.ivyStylePatterns)

libraryDependencies ++= Seq(
  //test dependencies
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "junit" % "junit" % "4.11" % "test",
  //runtime dependencies
  "com.typesafe.slick" %% "slick" % "1.0.1",
  "com.typesafe.play" %% "play-slick" % "0.5.0.8",
  "org.hsqldb" % "hsqldb" % "2.3.1",
  //security
  "be.objectify" %% "deadbolt-scala" % "2.2-RC2",
  "org.mindrot" % "jbcrypt" % "0.3m",
  //enable JDBC module for the project
  jdbc,
  cache
)

playScalaSettings