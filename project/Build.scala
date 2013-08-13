import sbt._
import Keys._


object AmcgalaAgents extends Build {

  lazy val projectSettings = Defaults.defaultSettings ++ Seq(
    name := "acmgala",
    version := "0.1.0",
    organization := "org.amcgala",
    scalaVersion := "2.10.2",
    javacOptions ++= Seq("-source", "1.7"),
    fork in run := true,
    libraryDependencies ++= Seq(
      "com.googlecode.efficient-java-matrix-library" % "ejml" % "0.22" withSources() withJavadoc(),
      "com.typesafe.akka" %% "akka-remote" % "2.3-SNAPSHOT" withSources() withJavadoc(),
      "com.typesafe.akka" %% "akka-actor" % "2.3-SNAPSHOT" withSources() withJavadoc(),
      "com.typesafe.akka" %% "akka-testkit" % "2.3-SNAPSHOT" withSources() withJavadoc(),
      "org.scalatest" % "scalatest_2.10" % "2.0.M6-SNAP35" % "test" withSources() withJavadoc(),
      "java3d" % "vecmath" % "1.3.1",
      "com.google.guava" % "guava" % "14.0.1" withSources() withJavadoc(),
      "org.slf4j" % "slf4j-api" % "1.7.5" withSources() withJavadoc(),
      "org.slf4j" % "slf4j-simple" % "1.7.5" withSources() withJavadoc(),
      "org.apache.commons" % "commons-math3" % "3.2" withSources() withJavadoc(),
      "org.lwjgl.lwjgl" % "lwjgl" % "2.9.0" withSources() withJavadoc()
    ),

    resolvers += Resolvers.typesafeReleaseRepo,
    resolvers += Resolvers.typesafeSnapshotRepo,
    resolvers += Resolvers.sonatypeSnapshotRepo
  )

  lazy val root = Project(id = "root", base = file("."), settings = projectSettings)
}


object Resolvers {
  lazy val typesafeReleaseRepo = "Typesafe Snapshot Repository" at "http://repo.typesafe.com/typesafe/snapshots/"
  lazy val typesafeSnapshotRepo = "Typesafe Release Repository" at "http://repo.typesafe.com/typesafe/releases/"
  lazy val sonatypeSnapshotRepo = "Sonatype Snapshots Repository" at "https://oss.sonatype.org/content/repositories/snapshots/"
}
