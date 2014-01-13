import play.Project._

name := "sample-crud-play-spring-style"

version := "1.0"

resolvers ++= Seq(
  "Sonatype" at "http://search.maven.org/remotecontent?filepath=",
  "Spring milestones" at "https://repo.springsource.org/libs-milestone",
  Resolver.url("Objectify Play Repository", url("http://schaloner.github.io/releases/"))(Resolver.ivyStylePatterns),
  Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.io/snapshots/"))(Resolver.ivyStylePatterns)
)

libraryDependencies ++= Seq(
  //test dependencies
  "org.scalatest" %% "scalatest" % "2.0" % "test",
  "junit" % "junit" % "4.11" % "test",
  "com.jayway.jsonpath" % "json-path" % "0.9.1" % "test",
  "org.springframework" % "spring-test" % "4.0.0.RELEASE" % "test",
  //runtime dependencies
  //Spring dependencies
  "org.springframework" % "spring-aop" % "4.0.0.RELEASE",
  "org.springframework" % "spring-context-support" % "4.0.0.RELEASE",
  "org.springframework" % "spring-orm" % "4.0.0.RELEASE",
  "org.springframework" % "spring-webmvc" % "4.0.0.RELEASE",
  "org.springframework.security" % "spring-security-config" % "3.2.0.RELEASE",
  "org.springframework.data" % "spring-data-jpa" % "1.4.3.RELEASE",
  "org.springframework.scala" %% "spring-scala" % "1.0.0.RC1",
  //hibernate dependencies
  "org.hibernate" % "hibernate-core" % "4.3.0.Final",
  "org.hibernate" % "hibernate-ehcache" % "4.3.0.Final",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.0.Final",
  "org.hsqldb" % "hsqldb" % "2.3.1",
  //jackson
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.3.0",
  //security
  "be.objectify" %% "deadbolt-scala" % "2.2-RC2",
  //enable JDBC module for the project
  jdbc,
  cache
)

playScalaSettings