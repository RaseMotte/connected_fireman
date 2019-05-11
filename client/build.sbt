name := """play-scala-seed"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = project in file(".")

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.12.6", "2.11.12")

libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.3.0"

