name := "kzonix-opencv-app"

version := "1.0"

scalaVersion := "2.12.14"


// Platform classifier for native library dependencies

// Some dependencies like `javacpp` are packaged with maven-plugin packaging
classpathTypes += "maven-plugin"

val javacppVersion = "1.3"
val platform = org.bytedeco.javacpp.Loader.getPlatform


libraryDependencies ++= Seq (
  "org.bytedeco.javacpp-presets" % "flandmark" % "1.07-1.4.4" classifier "",
  "org.bytedeco.javacpp-presets" % "flandmark" % "1.07-1.4.4" classifier platform,
  "org.scalatest" %% "scalatest" % "3.0.3" % Test,
  "net.imagej"                   % "ij"              % "1.53j",
  "junit" % "junit" % "4.13.2" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "com.typesafe.akka" %% "akka-actor" % "2.6.15",
  "com.typesafe.akka" %% "akka-slf4j" % "2.6.15",
  "com.typesafe.akka" %% "akka-stream" % "2.6.15",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.6.15"
)

// set MaxPermHeapSize for ignore VIDEOIO exceprtion
javaOptions += "-Xmx2G"

autoCompilerPlugins := true

// Set the prompt (for this build) to include the project id.
shellPrompt in ThisBuild := { state => "sbt:" + Project.extract(state).currentRef.project + "> " }


    