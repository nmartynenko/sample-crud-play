import play.Project._

//project settings

name := "sample-crud-play-spring-style"

version := "1.1"

//dependencies resolvers

resolvers ++= Seq(
  "Sonatype" at "http://search.maven.org/remotecontent?filepath=",
  "Spring milestones" at "https://repo.springsource.org/libs-milestone"
)

//Scala's compiler and runtime settings

scalaVersion := "2.10.4"

scalaBinaryVersion := "2.10"

scalacOptions ++= Seq(
  "-feature",
  "-unchecked",
  "-deprecation"
)

//dependencies settings

libraryDependencies ++= Seq(
  //test dependencies
  "junit" % "junit" % "4.11" % "test",
  "com.jayway.jsonpath" % "json-path" % "0.9.1" % "test",
  "org.springframework" % "spring-test" % "4.0.3.RELEASE" % "test",
  //runtime dependencies
  //Spring dependencies
  "org.springframework" % "spring-aop" % "4.0.3.RELEASE",
  "org.springframework" % "spring-context-support" % "4.0.3.RELEASE",
  "org.springframework" % "spring-orm" % "4.0.3.RELEASE",
  "org.springframework" % "spring-webmvc" % "4.0.3.RELEASE",
  "org.springframework.security" % "spring-security-config" % "3.2.3.RELEASE",
  "org.springframework.data" % "spring-data-jpa" % "1.5.2.RELEASE",
  "org.springframework.scala" %% "spring-scala" % "1.0.0.RC1",
  //hibernate dependencies
  "org.hibernate" % "hibernate-core" % "4.3.5.Final",
  "org.hibernate" % "hibernate-ehcache" % "4.3.5.Final",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.5.Final",
  "org.hsqldb" % "hsqldb" % "2.3.2",
  //jackson
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.3.3",
  //validation
  "net.sf.oval" % "oval" % "1.84",
  //enable JDBC module for the project
  jdbc,
  cache
)

//apply plugin settings

playScalaSettings