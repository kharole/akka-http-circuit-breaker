name := "akka-http-circuit-breaker"

version := "1.0"

scalaVersion := "2.12.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {
  Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.4.17",
    "com.typesafe.akka" %% "akka-slf4j" % "2.4.17",
    "com.typesafe.akka" %% "akka-http-core" % "10.0.4",
    "com.typesafe.akka" %% "akka-http" % "10.0.4",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.4"
  )
}
