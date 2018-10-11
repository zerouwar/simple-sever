name := "simple-server"

version := "1.0"

lazy val `simple-server` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

resolvers += "chenhuanming Maven Repository" at "https://raw.githubusercontent.com/zerouwar/my-maven-repo/master"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice)
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.12"
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.0"
libraryDependencies += "cn.chenhuanming" % "hooks-server" % "0.0.1"


unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

      
