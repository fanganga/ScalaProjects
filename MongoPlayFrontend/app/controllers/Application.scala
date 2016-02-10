package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

class Application extends Controller {


  def index = Action {
	implicit request =>
		Ok(views.html.index())
    Ok(views.html.index()).flashing("success" -> "Welcome")
  }


}
