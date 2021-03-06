package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n._
import play.api.data.validation.Constraints._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.Logger
import models._
import dal._

class UserController extends Controller {
 
	val userForm: Form[User] = Form{
		mapping(
			"name" -> nonEmptyText,
			"email" -> nonEmptyText
		)(User.apply)(User.unapply)
	}

	def showAddUserForm = Action {
		Ok(views.html.addUser(userForm))
	}
	
	def addUser = Action { implicit request =>
		userForm.bindFromRequest.fold(
			formWithErrors => {
				Logger.error("Attempt to add user failed")
				for( errorMessage <- formWithErrors.errors ) {
					Logger.error(errorMessage.message)
				}
				BadRequest(views.html.addUser(formWithErrors))
			},
			userData => {
				try{
					Logger.debug("Adding user with name "+userData.name+" and email "+ userData.email)
					val newUser = models.User(userData.name, userData.email)
					UserDaoRemote.create(newUser)
					Redirect(routes.Application.index).flashing(
						"success" -> "User created"	
					)
				} catch {
					case e:Exception => {
						BadRequest(views.html.error(e.getMessage))
					}
				}
			}
		)	
	}
	
	def all = Action {
		Logger.debug("Fetching lsit of all users")
		try {
			var users:Seq[User] = UserDaoRemote.list()
			Ok(views.html.allUsers(users))
		} catch {
			case e:Exception => {
				BadRequest(views.html.error(e.getMessage))
			}
		}
	}
	
	case class CreateUserForm(name: String, email: String)
}