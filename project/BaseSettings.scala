import sbt.Keys._
import sbt._

/**
 * Defines settings for the projects:
 *
 * - Scalastyle default
 * - Scalac, Javac: warnings as errors, target JDK 1.7
 * - Fork for run
 */
object BaseSettings extends sbtassembly.AssemblyKeys {
  import net.virtualvoid.sbt.graph.Plugin._
  import sbtassembly.MergeStrategy

  /**
   * Common project settings
   */
  val baseSettings: Seq[Def.Setting[_]] =
    graphSettings ++
      Seq(
        organization := "com.wesovi",
        scalaVersion := "2.11.5",
        version := "0.0.1-SNAPSHOT",
        scalacOptions in Compile ++= Seq("-encoding", "UTF-8", "-target:jvm-1.7", "-deprecation", "-unchecked", "-Ywarn-dead-code", "-feature"),
        scalacOptions in (Compile, doc) <++= (name in (Compile, doc), version in (Compile, doc)) map DefaultOptions.scaladoc,
        javacOptions in (Compile, compile) ++= Seq("-source", "1.7", "-target", "1.7", "-Xlint:unchecked", "-Xlint:deprecation", "-Xlint:-options"),
        javacOptions in doc := Seq(),
        javaOptions += "-Xmx2G",
        outputStrategy := Some(StdoutOutput),
        // for One-JAR
        exportJars := true,
        // this will be required for monitoring until Akka monitoring SPI comes in
        fork := true,
        fork in test := true,
        sbtPlugin := false,
        resolvers := ResolverSettings.resolvers,
        assemblyMergeStrategy in assembly := {
          case "application.conf" => MergeStrategy.concat
          case x =>
            val oldStrategy = (assemblyMergeStrategy in assembly).value
            oldStrategy(x)
        }
      )

  val projectSettings: Seq[Def.Setting[_]] =
    Seq(
      publishArtifact := true
    )

}
