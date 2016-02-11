package dal

import scala.collection.immutable.IndexedSeq
import models._

import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.duration._
import play.api.libs.json._

import play.api.mvc._
import play.api.libs.ws._
import play.api.http._

import play.api.Play.current

import scala.concurrent._
import scala.concurrent.ExecutionContext
 import scala.concurrent.ExecutionContext.Implicits.global

class Application @Inject() (ws: WSClient) extends Controller {

}

object UserDaoRemote {

	val backendUrl = "http://localhost:9001"
	def create(userModel: User) {
		val futureResponse: Future[WSResponse] = WS.url(backendUrl+"/users/new").post(
			Map("name"->Seq(userModel.name), "email"->Seq(userModel.email)))
	}
	
	def list():Seq[User] =  {
		val futureResult: Future[Seq[User]] = WS.url(backendUrl+"/users").get().map {
			response => {
				val array = response.json.as[Array[Map[String,String]]]
				array.map((element:Map[String,String]) => 
					User(element.get("name").getOrElse("unknown"), element.get("email").getOrElse("unknown"))
				)
			}
		}
		return Await.result(futureResult,Duration(60000,"millis"))
	}

}