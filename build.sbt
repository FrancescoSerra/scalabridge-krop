// give the user a nice default project!
ThisBuild / organization := "org.creativescala"
ThisBuild / scalaVersion := "3.6.3"
ThisBuild / semanticdbEnabled := true

// Dependency versions
val kropVersion = "0.9.4"
val munitVersion = "0.7.29"
val logbackVersion = "1.5.0"

// Run this command (build) to do everything involved in building the project
commands += Command.command("build") { state =>
  "clean" ::
    "compile" ::
    "test" ::
    "scalafixAll" ::
    "scalafmtAll" ::
    "scalafmtSbt" ::
    "dependencyUpdates" ::
    "reload plugins; dependencyUpdates; reload return" ::
    state
}

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.creativescala" %% "krop-core" % kropVersion,
    "org.scalameta" %% "munit" % munitVersion % Test
  )
)

lazy val root = project
  .in(file("."))
  .settings(
    name := """scalabridge"""
  )
  .aggregate(backend, frontend, shared.jvm, shared.js)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(
    name := """scalabridge-shared""",
    commonSettings,
    Compile / unmanagedSourceDirectories ++= Seq(
      baseDirectory.value.getParentFile / "src"
    ),
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  )
  .enablePlugins(KropLayout)

val circeVersion = "0.14.12"

lazy val backend = project
  .in(file("backend"))
  .settings(
    name := """scalabridge-backend""",
    commonSettings,
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % logbackVersion % Runtime
    ),
    // This sets Krop into development mode, which gives useful tools for
    // developers. Krop runs in production mode if you don't set this.
    run / javaOptions += "-Dkrop.mode=development",
    reStart / javaOptions += "-Dkrop.mode=development",
    run / fork := true
  )
  // KropTwirlLayout must come after SbtTwirl as it changes Twirl configuration
  .enablePlugins(SbtTwirl)
  .enablePlugins(KropLayout, KropTwirlLayout)
  .dependsOn(shared.jvm)

lazy val frontend = project
  .in(file("frontend"))
  .settings(
    name := """scalabridge-frontend""",
    commonSettings
  )
  .enablePlugins(ScalaJSPlugin, KropLayout)
  .dependsOn(shared.js)

// This configures the welcome message you see when you start sbt. Change it to
// add tasks that are useful for your project.
import sbtwelcome._

logo :=
  raw"""
     |Welcome to
     |
     | _     _  ______  _____   _____
     | |____/  |_____/ |     | |_____]
     | |    \_ |    \_ |_____| |
     |
     |Version ${kropVersion}
   """.stripMargin

usefulTasks := Seq(
  UsefulTask(
    "~backend/reStart",
    "Start the backend server, and restart on any change"
  ),
  UsefulTask("build", "Build everything from scratch"),
  UsefulTask("~compile", "Compile with file-watcher enabled")
)
