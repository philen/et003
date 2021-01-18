name := "et003"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= Seq (
  "com.badlogicgames.gdx" % "gdx-backend-lwjgl3" % "1.9.13",
  "com.badlogicgames.gdx" % "gdx-platform" % "1.9.13" classifier "natives-desktop",
  "com.badlogicgames.gdx" % "gdx-freetype" % "1.9.13",
  "com.badlogicgames.gdx" % "gdx-freetype-platform" % "1.9.13" classifier "natives-desktop",
  "io.monix" %% "monix" % "3.3.0",
  "org.scalatest" %% "scalatest" % "3.2.3" % Test,
)
