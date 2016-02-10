package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n._
import play.api.data.validation.Constraints._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import models._
import dal._

class UserController extends Controller {
 
	val userForm: Form[User] = Form{
		mapping(
			"name" -> text,
			"email" -> text
		)(User.apply)(User.unapply)
	}

	def showAddUserForm = Action {
		Ok(views.html.addUser(userForm))
	}
	
	def addUser = Action { implicit request =>
		userForm.bindFromRequest.fold(
			errors => BadRequest(views.html.index()),
			userData => {
				val newUser = models.User(userData.name, userData.email)
				UserDaoRemote.create(newUser)
				Redirect(routes.Application.index)
			}
		)	
	}
	
	def all = Action {
		var users:Seq[User] = UserDaoRemote.list()
		Ok(views.html.allUsers(users))
	}
	
	case class CreateUserForm(name: String, email: String)
}