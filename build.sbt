name := "Scala-Mongo Example"

version := "0.1"

//scalaVersion := "2.9.2"

//libraryDependencies ++= Seq( 
//    "org.scalatest" %% "scalatest" % "1.6.1" % "test",
//    "org.mongodb" %% "casbah" % "2.4.1",
//    "com.novus" %% "salat" % "1.9.1"
//)

scalaVersion := "2.10.0"

libraryDependencies ++= Seq( 
    "org.scalatest" %% "scalatest" % "1.9.1" % "test",
    "org.mongodb" %% "casbah-core" % "2.5.0-SNAPSHOT",
    "com.novus" %% "salat-core" % "1.9.2-SNAPSHOT"
)


resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

