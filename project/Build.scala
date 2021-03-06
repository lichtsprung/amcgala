import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import sbt._
import Keys._
import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin._

object AmcgalaAgents extends Build {

  lazy val formatSettings = SbtScalariform.scalariformSettings ++ Seq(
    ScalariformKeys.preferences in Compile := formattingPreferences,
    ScalariformKeys.preferences in Test := formattingPreferences
  )

  import scalariform.formatter.preferences._
  def formattingPreferences =
    FormattingPreferences()
      .setPreference(RewriteArrowSymbols, true)
      .setPreference(AlignParameters, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(DoubleIndentClassDeclaration, true)
      .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)

  val JavaDoc = config("genjavadoc") extend Compile

  val javadocSettings = inConfig(JavaDoc)(Defaults.configSettings) ++ Seq(
    libraryDependencies += compilerPlugin("com.typesafe.genjavadoc" %%
      "genjavadoc-plugin" % "0.8" cross CrossVersion.full),
    scalacOptions <+= target map (t => "-P:genjavadoc:out=" + (t / "java")),
    packageDoc in Compile <<= packageDoc in JavaDoc,
    sources in JavaDoc <<=
      (target, compile in Compile, sources in Compile) map ((t, c, s) =>
        (t / "java" ** "*.java").get ++ s.filter(_.getName.endsWith(".java"))),
    javacOptions in JavaDoc := Seq(),
    artifactName in packageDoc in JavaDoc :=
      ((sv, mod, art) =>
        "" + mod.name + "_" + sv.binary + "-" + mod.revision + "-javadoc.jar")
  )


  lazy val projectSettings = Defaults.coreDefaultSettings ++ Seq(
    name := "amcgala",
    version := "3.2.5",
    organization := "org.amcgala",
    scalaVersion := "2.11.1",
    scalacOptions := Seq(
      "-deprecation",
      "-feature",
      "-Xlint",
      "-optimise"
    ),
    incOptions := incOptions.value.withNameHashing(nameHashing = true),
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-remote" % "2.3.2" withSources() withJavadoc(),
      "com.typesafe.akka" %% "akka-actor" % "2.3.2" withSources() withJavadoc(),
      "org.scalatest" %% "scalatest" % "2.2.0" % "test" withSources() withJavadoc(),
      "java3d" % "vecmath" % "1.3.1",
      "com.googlecode.efficient-java-matrix-library" % "ejml" % "0.23" withSources() withJavadoc(),
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

  lazy val amcgala = Project(id = "amcgala", base = file("."), settings = projectSettings ++ assemblySettings ++ formatSettings ++ javadocSettings) settings(
    jarName in assembly := "amcgala.jar",
    test in assembly := {}
    )
}


object Resolvers {
  lazy val typesafeReleaseRepo = "Typesafe Snapshot Repository" at "http://repo.typesafe.com/typesafe/releases/"
  lazy val typesafeSnapshotRepo = "Typesafe Release Repository" at "http://repo.typesafe.com/typesafe/snapshots/"
  lazy val sonatypeSnapshotRepo = "Sonatype Snapshots Repository" at "https://oss.sonatype.org/content/repositories/snapshots/"
}
