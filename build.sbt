name := "et003"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq (
  "com.badlogicgames.gdx" % "gdx-backend-lwjgl3" % "1.10.0",
  "com.badlogicgames.gdx" % "gdx-platform" % "1.10.0" classifier "natives-desktop",
  "com.badlogicgames.gdx" % "gdx-freetype" % "1.10.0",
  "com.badlogicgames.gdx" % "gdx-freetype-platform" % "1.10.0" classifier "natives-desktop",
  "io.monix" %% "monix" % "3.4.0",
  "org.scalatest" %% "scalatest" % "3.2.9" % Test,
)
