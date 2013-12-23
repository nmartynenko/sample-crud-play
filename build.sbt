import play.Project._

name := "sample-crud-play"

version := "1.0"

resolvers += "Sonatype" at "http://search.maven.org/remotecontent?filepath="

resolvers += "Spring milestones" at "https://repo.springsource.org/libs-milestone"

//test dependencies

libraryDependencies += "org.scalatest" %% "scalatest" % "2.0" % "test"

libraryDependencies += "junit" % "junit" % "4.11" % "test"

libraryDependencies += "com.jayway.jsonpath" % "json-path" % "0.9.1" % "test"

libraryDependencies += "org.springframework" % "spring-test" % "4.0.0.RELEASE" % "test"

//runtime dependencies

//Spring dependencies
libraryDependencies += "org.springframework" % "spring-aop" % "4.0.0.RELEASE"

libraryDependencies += "org.springframework" % "spring-context-support" % "4.0.0.RELEASE"

libraryDependencies += "org.springframework" % "spring-orm" % "4.0.0.RELEASE"

libraryDependencies += "org.springframework" % "spring-webmvc" % "4.0.0.RELEASE"

libraryDependencies += "org.springframework.security" % "spring-security-config" % "3.2.0.RELEASE"

libraryDependencies += "org.springframework.security" % "spring-security-taglibs" % "3.2.0.RELEASE"

libraryDependencies += "org.springframework.data" % "spring-data-jpa" % "1.4.3.RELEASE"

libraryDependencies += "org.springframework.scala" %% "spring-scala" % "1.0.0.RC1"

//hibernate dependencies
libraryDependencies += "org.hibernate" % "hibernate-core" % "4.3.0.Final"

libraryDependencies += "org.hibernate" % "hibernate-ehcache" % "4.3.0.Final"

libraryDependencies += "org.hibernate" % "hibernate-entitymanager" % "4.3.0.Final"

libraryDependencies += "org.hsqldb" % "hsqldb" % "2.3.1"

//enable JDBC module for the project
libraryDependencies += jdbc

playScalaSettings