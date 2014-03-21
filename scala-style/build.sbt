import play.Project._

name := "sample-crud-play-scala-style"

version := "1.1"

resolvers += "Sonatype" at "http://search.maven.org/remotecontent?filepath="

resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.io/releases/"))(Resolver.ivyStylePatterns)

resolvers += Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.io/snapshots/"))(Resolver.ivyStylePatterns)

libraryDependencies ++= Seq(
  //test dependencies
  "org.scalatest" %% "scalatest" % "2.1.2" % "test",
  "junit" % "junit" % "4.11" % "test",
  //runtime dependencies
  "com.typesafe.slick" %% "slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick" % "0.6.0.1",
  "org.hsqldb" % "hsqldb" % "2.3.2",
  //security
  "be.objectify" %% "deadbolt-scala" % "2.2-RC2",
  "org.mindrot" % "jbcrypt" % "0.3m",
  //enable JDBC module for the project
  jdbc,
  cache
)

playScalaSettings