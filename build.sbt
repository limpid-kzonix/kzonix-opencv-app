name := "OpenCV-App"

version := "1.0"

scalaVersion := "2.12.1"


// Platform classifier for native library dependencies


scalacOptions ++= Seq ("-unchecked", "-deprecation", "-optimize", "-Xlint")

// Some dependencies like `javacpp` are packaged with maven-plugin packaging
classpathTypes += "maven-plugin"

val javacppVersion = "1.3"
val platform = org.bytedeco.javacpp.Loader.getPlatform

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  "ImageJ Releases" at "http://maven.imagej.net/content/repositories/releases/",
  // Use local maven repo for local javacv builds
  "Local Maven Repository" at "file:///" + Path.userHome.absolutePath + "/.m2/repository"
)

libraryDependencies ++= Seq (

  "org.bytedeco" % "javacv" % "1.3.2",
  "org.bytedeco" % "javacpp" % "1.3.2",
  "org.bytedeco.javacpp-presets" % "flandmark" % "1.07-1.3" classifier "",
  "org.bytedeco.javacpp-presets" % "flandmark" % "1.07-1.3" classifier platform,
  "org.bytedeco.javacpp-presets" % "opencv" % "3.2.0-1.3" classifier "",
  "org.bytedeco.javacpp-presets" % "opencv" % "3.2.0-1.3"  classifier platform,
  "org.scalatest" %% "scalatest" % "3.0.3" % Test,
  "net.imagej"                   % "ij"              % "1.49v",
  "junit"                        % "junit"           % "4.12" % "test",
  "com.novocode"                 % "junit-interface" % "0.11-RC1" % "test",
  "com.typesafe.akka" %% "akka-actor" % "2.5.3",
  "com.typesafe.akka" %% "akka-agent" % "2.5.3",
  "com.typesafe.akka" %% "akka-camel" % "2.5.3",
  "com.typesafe.akka" %% "akka-cluster" % "2.5.3",
  "com.typesafe.akka" %% "akka-cluster-metrics" % "2.5.3",
  "com.typesafe.akka" %% "akka-cluster-sharding" % "2.5.3",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.5.3",
  "com.typesafe.akka" %% "akka-distributed-data" % "2.5.3",
  "com.typesafe.akka" %% "akka-multi-node-testkit" % "2.5.3",
  "com.typesafe.akka" %% "akka-osgi" % "2.5.3",
  "com.typesafe.akka" %% "akka-persistence" % "2.5.3",
  "com.typesafe.akka" %% "akka-persistence-query" % "2.5.3",
  "com.typesafe.akka" %% "akka-persistence-tck" % "2.5.3",
  "com.typesafe.akka" %% "akka-remote" % "2.5.3",
  "com.typesafe.akka" %% "akka-slf4j" % "2.5.3",
  "com.typesafe.akka" %% "akka-stream" % "2.5.3",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.3",
  "com.typesafe.akka" %% "akka-typed" % "2.5.3",
  "com.typesafe.akka" %% "akka-contrib" % "2.5.3"
)

// set MaxPermHeapSize for ignore VIDEOIO exceprtion
javaOptions += "-Xmx2G"

autoCompilerPlugins := true

// set the main class for 'sbt run'
mainClass in (Compile, run) := Some ("xorg.scala.api.Main")

// Set the prompt (for this build) to include the project id.
shellPrompt in ThisBuild := { state => "sbt:" + Project.extract(state).currentRef.project + "> "}

// Fork a new JVM for 'run' and 'test:run', to avoid JavaFX double initialization problems
fork := true


    