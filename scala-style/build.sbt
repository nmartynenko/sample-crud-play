import play.Project._

//project settings

name := "sample-crud-play-scala-style"

version := "1.1"

//dependencies resolvers

resolvers += "Sonatype" at "http://search.maven.org/remotecontent?filepath="

resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.io/releases/"))(Resolver.ivyStylePatterns)

resolvers += Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.io/snapshots/"))(Resolver.ivyStylePatterns)

//Scala's compiler and runtime settings

scalaVersion := "2.10.4"

scalaBinaryVersion := "2.10"

scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation")

//dependencies settings

libraryDependencies ++= Seq(
  //test dependencies
  "junit" % "junit" % "4.11" % "test",
  //runtime dependencies
  "com.typesafe.slick" %% "slick" % "2.0.1",
  "com.typesafe.play" %% "play-slick" % "0.6.0.1",
  "org.hsqldb" % "hsqldb" % "2.3.2",
  //security
  "be.objectify" %% "deadbolt-scala" % "2.2-RC2",
  "org.mindrot" % "jbcrypt" % "0.3m",
  //enable JDBC module for the project
  jdbc,
  cache
)

//apply plugin settings

playScalaSettings