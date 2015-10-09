import sbt._
import Keys._


object ScalaValidations extends Build {

  lazy val _scalacOptions = Seq("-deprecation", "-unchecked", "-feature")

  lazy val commonSettings = Seq(
    version := "1.0",
    organization := "com.payit",
    scalaVersion := "2.11.4",
    ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },
    resolvers ++= Seq(
      "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases",
      "Sonatype Releases"  at "http://oss.sonatype.org/content/repositories/releases",
      "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
    ),
    scalacOptions ++= _scalacOptions,
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature"),
    scalacOptions in Test ++= Seq("-Yrangepos")
  )

  lazy val root = Project(id = "scala-validations", base = file("."),
    settings = commonSettings ++ Seq(
      name := "scala-validations",
      libraryDependencies ++= Seq(
        "org.specs2" %% "specs2-core" % "3.6.4" % "compile",
        "org.specs2" % "specs2-mock_2.11" % "3.6.4-20150928232002-999c515" % "compile"
      )
    )
  )
}