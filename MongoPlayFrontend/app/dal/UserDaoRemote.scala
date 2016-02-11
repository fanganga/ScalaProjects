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
import play.api.Logger

import play.api.Play.current

import scala.concurrent._
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

import java.lang.Exception._

class Application @Inject() (ws: WSClient) extends Controller {

}

object UserDaoRemote {

	val backendUrl = "http://localhost:9001"
	def create(userModel: User) {
		val futureResponse: Future[WSResponse] = 
			WS.url(backendUrl+"/users/new").post(Map("name"->Seq(userModel.name), "email"->Seq(userModel.email))).map {
				response => {
					if (response.status != 200) {
						Logger.error("Failed to add new user")
						throw new Exception("Could not add new user")
					}
					response
				}
		}
		Await.result(futureResponse,Duration(60000,"millis"))		
	}
	
	def list():Seq[User] =  {
		val futureResult: Future[Seq[User]] = WS.url(backendUrl+"/users").get().map {
			response => {
				if (response.status != 200) {
					Logger.error("Failed to get list of all users from backend")
					throw new Exception("Could not get list from Backend")
				}
				val array = response.json.as[Array[Map[String,String]]]
				array.map((element:Map[String,String]) => 
					User(element.get("name").getOrElse("unknown"), element.get("email").getOrElse("unknown"))
				)
			}
		}
		return Await.result(futureResult,Duration(60000,"millis"))
	}

}