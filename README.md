scala_mongo_example
===================

Sample code for interacting with Mongo from Scala, covers usage of Casbah and Salat

## Versions used:
 Scala 2.9.2
 sbt 0.12
 Casbah 2.4.1 - Layer ontop of java mongo driver to make it more like Scala
 Salat 1.9.1  - Provides case-class serialization

## Run Tests:
    sbt test

## Intelli-J Setup:
 sbt is able to generate an Intelli-J project, a plugin is required.

1) Install the plugin
    $ cat ~/.sbt/plugins/build.sbt 

    resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

    addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.3.0-SNAPSHOT")

2) Run:
    sbt gen-idea


