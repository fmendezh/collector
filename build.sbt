
organization := "org.gbif"

name := "collector"

version := "0.1"

scalaVersion := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  val reflectV = "2.10.5"
  val specs2V = "2.3.11"
  val gbifApiV = "0.37-SNAPSHOT"
  Seq(
    "org.gbif" % "gbif-api" % gbifApiV,
    "org.scala-lang" % "scala-reflect" % "2.11.7",
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
    "org.scala-lang.modules" %% "scala-xml" % "1.0.4",
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-client" % sprayV,
    "io.spray"            %%  "spray-json" % "1.3.2",
    "ch.qos.logback"      % "logback-classic" % "1.1.5",
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-slf4j" % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"   % specs2V % "test"
  )
}

resolvers ++=  Seq("gbif-all" at "http://repository.gbif.org/content/groups/gbif",
  "gbif-thirdparty" at "http://repository.gbif.org/content/repositories/thirdparty",
  "cloudera-release" at "http://repository.cloudera.com/artifactory/cloudera-repos/",
  "Maven Central Repo" at "http://repository.gbif.org/content/repositories/central/"
)

Revolver.settings
