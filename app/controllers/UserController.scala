package controllers

import javax.inject.Inject

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Source}
import akka.util.ByteString
import dao._
import models.User
import play.api.http.{ContentTypes, HttpEntity}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller, ResponseHeader, Result}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by davidsantiago on 5/3/17.
  */
class UserController @Inject()(userRepository: UserRepository)
  extends Controller {


  def getUsers = Action.async {
    userRepository.list().map { users =>
      Ok(Json.toJson(users))
    }
  }

  def getUsersChuck = Action {
    val source: Source[User, _] = Source.fromPublisher(userRepository.listStream())

    val flowToByteString: Flow[User, ByteString, NotUsed] =
      Flow[User].map(user => ByteString.fromString(user.toJsonWithJackson + ",\n"))

    Ok.chunked(source.via(flowToByteString))
  }

  def getUsersStream = Action {

    val source: Source[User, _] = Source.fromPublisher(userRepository.listStream())

    val sourceString: Source[String, _] = source.map(user => user.toJsonWithJackson).intersperse("[", ",\n", "]")

    val sourceByteString: Source[ByteString, _] = sourceString.fold(ByteString.fromString(""))(_ ++ ByteString(_))

    Result(
      header = ResponseHeader(200, Map.empty),
      body = HttpEntity.Streamed(sourceByteString, None, Some(ContentTypes.JSON))
    )
  }

}