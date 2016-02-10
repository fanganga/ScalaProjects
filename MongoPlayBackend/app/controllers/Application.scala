package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

class Application extends Controller {


  def index = Action {
    Ok("This is the backend service")
  }


}
