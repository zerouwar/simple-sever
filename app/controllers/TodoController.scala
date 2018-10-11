package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import services.TodoService

import scala.concurrent.{ExecutionContext, Future}

/**
  *
  * @author chenhuanming
  *         Created at 2018/9/28
  */
@Singleton
class TodoController @Inject()(cc: ControllerComponents, todoService: TodoService)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  /**
    * 创建一个Todo
    */
  def add = Action(parse.json).async { request =>
    val json: JsValue = Json.parse(request.body.toString())
    val owner = json("owner").as[String]
    val desc = json("desc").as[String]
    todoService.addTodo(owner, desc).map(t => Ok(Json.toJson(t)))
  }

  /**
    * 完成一个todo
    */
  def done(id: Int) = Action(parse.json).async { request =>
    val json: JsValue = Json.parse(request.body.toString())
    val remark = json("remark").as[String]

    val bo = todoService.toBO(id)
    if (bo isEmpty) {
      Future.successful(BadRequest("need remark"))
    }
    else {
      bo.get.done(remark).map(f => Created(Json.toJson(f)))
    }
  }

  /**
    * 获取今天的todo列表
    */
  def getToday(owner: String) = Action.async { request =>
    todoService.getTodayTodo(owner).map(todo => Ok(Json.toJson(todo)))
  }

}


