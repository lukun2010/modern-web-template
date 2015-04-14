name := "modern-web-template"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.4"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += "Local Maven Repository" at "file:///c:/Users/lu.kun/.m2/repository"

libraryDependencies ++= Seq(
  "com.google.inject" % "guice" % "3.0",
  "javax.inject" % "javax.inject" % "1",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23",
  "org.webjars" % "bootstrap" % "3.3.1",
  "org.webjars" % "angularjs" % "1.3.8",
  "org.webjars" % "angular-ui-bootstrap" % "0.12.0",
  "com.google.guava" % "guava" % "11.0.2",
  "org.apache.avro" % "avro" % "1.7.7",
  "com.bestv.bi" % "schemaclient" % "1.0-SNAPSHOT",
  "org.schemarepo" % "schema-repo-client" % "0.1.3-SNAPSHOT",
  "com.sun.jersey.contribs" % "jersey-multipart" % "1.15",
  "com.sun.jersey.contribs" % "jersey-guice" % "1.15",
  "com.sun.jersey" % "jersey-server" % "1.15",
  "com.sun.jersey" % "jersey-core" % "1.15",
  "com.sun.jersey" % "jersey-client" % "1.15",
  "org.apache.kafka" % "kafka_2.11" % "0.8.2.1" exclude("javax.jms","jms") exclude("com.sun.jdmk","jmxtools") exclude("com.sun.jmx","jmxri"),
  "org.apache.zookeeper" % "zookeeper" % "3.4.5" exclude("javax.jms","jms") exclude("com.sun.jdmk","jmxtools") exclude("com.sun.jmx","jmxri"),
  "org.mockito" % "mockito-core" % "1.10.17" % "test")
