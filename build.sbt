import sbt._
import org.allenai.plugins.CoreDependencies
import org.allenai.plugins.CoreDependencies._
import sbtrelease._
import sbtrelease.ReleaseStateTransformations._

name := "Word2VecJava"

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-lang3" % "3.1",
  "com.google.guava" % "guava" % "18.0",
  "commons-io" % "commons-io" % "2.4",
  "log4j" % "log4j" % "1.2.17",
  "joda-time" % "joda-time" % "2.3",
  "org.apache.thrift" % "libfb303" % "0.9.1",
  "org.apache.commons" % "commons-math3" % "3.4.1",
  "org.scalacheck" %% "scalacheck" % "1.12.0" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test)

// Override the problematic new release plugin.
lazy val releaseProcessSetting = releaseProcess := Seq(
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  pushChanges
)


lazy val buildSettings = Seq(
  organization := "org.allenai.word2vec",
  crossScalaVersions := Seq(defaultScalaVersion),
  scalaVersion <<= crossScalaVersions { (vs: Seq[String]) => vs.head },
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")),
  homepage := Some(url("https://github.com/allenai/Word2VecJava")),
  scmInfo := Some(ScmInfo(
    url("https://github.com/allenai/Word2VecJava"),
    "https://github.com/allenai/Word2VecJava.git")),
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  bintrayPackage := s"${organization.value}:${name.value}_${scalaBinaryVersion.value}",
  pomExtra :=
    <developers>
      <developer>
        <id>allenai-dev-role</id>
        <name>Allen Institute for Artificial Intelligence</name>
        <email>dev-role@allenai.org</email>
      </developer>
    </developers>)

javacOptions in doc := Seq(
  "-source", "1.7",
  "-Xdoclint:none")

lazy val word2vecjavaRoot = Project(
  id = "word2vecjavaRoot",
  base = file("."),
  settings = buildSettings ++ releaseProcessSetting
).enablePlugins(LibraryPlugin)
