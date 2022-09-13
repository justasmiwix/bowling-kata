ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "bowling-kata",
    idePackagePrefix := Some("com.kata.bowling")
  )

libraryDependencies += "org.specs2" %% "specs2-core"  % "4.16.1" % "test"
libraryDependencies += "org.specs2" %% "specs2-junit" % "4.16.1" % "test"
