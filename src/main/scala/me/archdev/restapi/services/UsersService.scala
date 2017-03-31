package me.archdev.restapi.services

import me.archdev.restapi.models.db.UserEntityTable
import me.archdev.restapi.models.{ UserEntityUpdate, UserEntity }
import org.mindrot.jbcrypt.BCrypt

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object UsersService extends UsersService

trait UsersService extends UserEntityTable {

  import driver.api._

  def getUsers(): Future[Seq[UserEntity]] = db.run(users.result)

  def getUserById(id: Long): Future[Option[UserEntity]] = db.run(users.filter(_.id === id).result.headOption)

  def getUserByLogin(login: String): Future[Option[UserEntity]] = db.run(users.filter(_.username === login).result.headOption)

  def createUser(user: UserEntity): Future[UserEntity] = db.run(users returning users += user.withHashedPassword())

  def updateUser(id: Long, userUpdate: UserEntityUpdate): Future[Option[UserEntity]] = getUserById(id).flatMap {
    case Some(user) =>
      val updatedUser = userUpdate.merge(user)
      db.run(users.filter(_.id === id).update(updatedUser)).map(_ => Some(updatedUser))
    case None => Future.successful(None)
  }

  def deleteUser(id: Long): Future[Int] = db.run(users.filter(_.id === id).delete)

}