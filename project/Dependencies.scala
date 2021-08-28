import sbt._

object Dependencies {
  lazy val jacksonCore = "com.fasterxml.jackson.core" % "jackson-core" % "2.12.5"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.9" % Test
}
