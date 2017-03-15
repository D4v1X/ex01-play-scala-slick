package dao

import javax.inject.{Inject, Singleton}

import models.User
import play.api.db.slick.DatabaseConfigProvider
import slick.backend.DatabasePublisher
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by davidsantiago on 5/3/17.
  */
@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider) {

  // We want the JdbcProfile for this provider
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import driver.api._
  import slick.jdbc.{GetResult => GR}

  /** Table description of table tm_user. Objects of this class serve as prototypes for rows in queries. */
  private class TmUserTable(_tableTag: Tag) extends Table[User](_tableTag, "tm_user") {

    def * = (id, name, age, address) <> ((User.apply _).tupled, User.unapply)

    /** Maps whole row to an option. Useful for outer joins. */

    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(age), Rep.Some(address))
      .shaped.<>(
      { r => import r._; _1.map(_ => (User.apply _).tupled((_1.get, _2.get, _3.get, _4.get))) },
      (_: Any) => throw new Exception("Inserting into ? projection not supported.")
    )

    /** Database column id SqlType(serial), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
    /** Database column name SqlType(varchar), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255, varying = true))
    /** Database column age SqlType(int4) */
    val age: Rep[Int] = column[Int]("age")
    /** Database column address SqlType(varchar), Length(255,true) */
    val address: Rep[String] = column[String]("address", O.Length(255, varying = true))
  }


  /** GetResult implicit for fetching TmUserRow objects using plain SQL queries */
  implicit def GetResultUser(implicit e0: GR[Int], e1: GR[String]): GR[User] = GR {
    prs =>
      import prs._
      (User.apply _).tupled((<<[Int], <<[String], <<[Int], <<[String]))
  }

  /** Collection-like TableQuery object for table TmUser */
  private val users = new TableQuery(tag => new TmUserTable(tag))


  def list(): Future[Seq[User]] = db.run {
    users.result
  }

  def getPublisher(): DatabasePublisher[User] = {
    db.stream(users.result)
  }

  def listStream(): DatabasePublisher[User] = {
    val query = for {user <- users} yield user
    val action = query.result
    db.stream(action)
  }

  def listParStream(): DatabasePublisher[User] = {
    val query = for {user <- users if (user.id % 2) === 0} yield user

    val action = query.result
    db.stream(action)
  }

  def listImParStream(): DatabasePublisher[User] = {
    val query = for {user <- users if (user.id % 2).!=(0)} yield user
    val action = query.result
    db.stream(action)
  }

}
