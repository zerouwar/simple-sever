package models.foorprint

import java.time.LocalDateTime

import play.api.libs.json.Json

/**
  *
  * @author chenhuanming
  *         Created at 2018/9/30
  */

case class Todo(id: Int, owner: String, desc: String, createTime: LocalDateTime)

object Todo {
  implicit val todoFormat = Json.format[Todo]

  implicit def footprint2Todo = (f: Footprint) => Todo(f.id, f.owner, f.desc, f.createTime)
}
