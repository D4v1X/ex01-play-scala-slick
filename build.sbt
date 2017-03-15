import com.typesafe.config.ConfigFactory

name := """ex01-play-scala-slick"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies += "com.typesafe.play" %% "play-slick" % "2.0.2"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2"
libraryDependencies += "org.postgresql" % "postgresql" % "9.4.1212"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.9"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.9"
//libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.2.0" % "compile"

libraryDependencies += specs2 % Test


/** *************************************************
  * // Usage:
  * SourceCodeGenerator configURI [outputDir]
  * SourceCodeGenerator profile jdbcDriver url outputDir pkg [user password]
  * *
  * Options:
  * - configURI: A URL pointing to a standard database config file (a fragment is
  * resolved as a path in the config), or just a fragment used as a path in
  * application.conf on the class path
  *
  * - profile: Fully qualified name of Slick profile class, e.g. "slick.jdbc.H2Profile"
  * - jdbcDriver: Fully qualified name of jdbc driver class, e.g. "org.h2.Driver"
  * - url: JDBC URL, e.g. "jdbc:postgresql://localhost/test"
  * - outputDir: Place where the package folder structure should be put
  * - pkg: Scala package the generated code should be places in
  * - user: database connection user name
  * - password: database connection password
  * *
  * When using a config file, in addition to the standard config parameters from
  * slick.basic.DatabaseConfig you can set "codegen.package" and
  * "codegen.outputDir". The latter can be overridden on the command line.
  * *************************************************/

// code generation task
//val genSlickModel = TaskKey[Seq[File]]("gen-tables")
//
//val conf = ConfigFactory.parseFile(new File("conf/database.conf")).resolve()
//
//val genSlickTask =
//  Def.task {
//    //  (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (src, cp, r, s) =>
//    val src = sourceManaged.value
//    val cp = (fullClasspath in Compile).value
//    val r = (runner in Compile).value
//    val s = streams.value
//    val outputDir = (src / "slick").getPath
//    // place generated files in sbt's managed sources folder
//    // connection info
//    val profile = conf.getString("slick.dbs.default.driver").dropRight(1)
//    val jdbcDriver = conf.getString("slick.dbs.default.db.driver")
//    val url = conf.getString("slick.dbs.default.db.url")
//    val user = conf.getString("slick.dbs.default.db.user")
//    val password = conf.getString("slick.dbs.default.db.password")
//    val pkg = "dao"
//    toError(
//      r.run("slick.codegen.SourceCodeGenerator", cp.files,
//        Array(profile, jdbcDriver, url, outputDir, pkg, user, password),
//        s.log))
//    val fileName = outputDir + s"/$pkg/Tables.scala"
//    Seq(file(fileName))
//  }
//
//genSlickModel := genSlickTask.value
//
//unmanagedSourceDirectories in Compile += sourceManaged.value / "main/generated"

//sourceGenerators in Compile <+= genSlickTask // register automatic code generation on every compile, remove for only manual use






