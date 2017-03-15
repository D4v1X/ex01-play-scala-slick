package models

import play.api.libs.json._
import utils.JsonUtil


case class User(id: Int, name: String, age: Int, address: String)

object User {

  //Play Json
  implicit val userWrites: OWrites[User] = Json.writes[User]


  implicit class Marshallable[T](user: User) {
    def toJsonWithJackson: String = JsonUtil.toJson(user)
    def toJsonWithPlayJson: String = Json.toJson(user).toString()
    def toJsonWithString: String = "{\"id\":" + user.id + ",\"name\":\"" + user.name + "\",\"age\":" + user.age + ",\"address\":\"" + user.address + "\"}"
  }


}
