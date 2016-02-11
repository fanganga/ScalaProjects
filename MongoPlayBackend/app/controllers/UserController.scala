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
import play.api.Logger
import models._
import dal._

class UserController extends Controller {
 
	val userForm: Form[User] = Form{
		mapping(
			"name" -> text,
			"email" -> text
		)(User.apply)(User.unapply)
	}
	
	def addUserPost = Action { implicit request => {
			Logger.debug("Received post request to add user")
			Logger.debug(request.body.toString)
			userForm.bindFromRequest.fold(
				formWithErrors => {
					Logger.error("Attempt to add user failed")
					for( errorMessage <- formWithErrors.errors ) {
						Logger.error(errorMessage.message)
					}
					BadRequest("Cannot add user")
				},
				userData => {
					Logger.debug("Adding user with name "+userData.name+" and email "+ userData.email)
					val newUser = models.User(userData.name, userData.email)
					UserDao.create(newUser)
					Ok("User created")
				}
			)
		}	
	}
	
	def all = Action {
		Logger.debug("Received request to list all users")
		var users:Seq[User] = UserDao.list()
		var userMaps:Seq[Map[String,String]] = users.map((user: User) => Map("name" -> user.name, "email" -> user.email))
		Ok(Json.toJson(userMaps.toArray))
	}
	
	  
	case class CreateUserForm(name: String, email: String)
}