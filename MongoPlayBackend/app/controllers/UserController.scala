package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n._
import play.api.data.validation.Constraints._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.libs.json._
import models._
import dal._

class UserController extends Controller {
 
	val userForm: Form[User] = Form{
		mapping(
			"name" -> text,
			"email" -> text
		)(User.apply)(User.unapply)
	}
	
	def addUser = Action { implicit request => {
			var queryParams: Map[String,String] = request.queryString.map { case (k,v) => k -> v.mkString }
			if(queryParams.get("name").isEmpty || queryParams.get("email").isEmpty) {
				BadRequest("Not enough data")
			}
			var name = queryParams.get("name").get
			var email = queryParams.get("email").get
			var user: User = User(name, email)
			UserDao.create(user)
			Ok("User created")
		}	
	}
	
	def all = Action {
		var users:Seq[User] = UserDao.list()
		var userMaps:Seq[Map[String,String]] = users.map((user: User) => Map("name" -> user.name, "email" -> user.email))
		Ok(Json.toJson(userMaps.toArray))
	}
	
	  
	case class CreateUserForm(name: String, email: String)
}