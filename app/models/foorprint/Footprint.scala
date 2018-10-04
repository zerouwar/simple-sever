package models.foorprint

import java.time.LocalDateTime

import play.api.libs.json._

/**
  *
  * @author chenhuanming
  *         Created at 2018/9/30
  */
case class Footprint(id: Int, owner: String, desc: String, status: Short, createTime: LocalDateTime, doneTime: Option[LocalDateTime], remark: Option[String])

object Footprint {
  implicit val footprintFormat = Json.format[Footprint]

  def build(todo: Todo): Footprint = {
    Footprint(todo.id, todo.owner, todo.desc, 0, todo.createTime, None, None)
  }


  object Status extends Enumeration {
    type Status = Value
    val TODO, DOING, DONE, ABORT = Value
  }

}
