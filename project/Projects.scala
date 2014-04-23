import sbt._
import Keys._

object Projects extends Build {

    lazy val root = Project(id = "sample-crud-play",
                            base = file("."),
                            settings = Project.defaultSettings).aggregate(scala_style, spring_style)

    lazy val scala_style = Project(id = "scala-style",
                            base = file("./scala-style"))

    lazy val spring_style = Project(id = "spring-style",
                            base = file("./spring-style"))
}