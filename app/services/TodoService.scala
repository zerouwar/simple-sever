package services

import java.time.{LocalDate, LocalDateTime}

import javax.inject.{Inject, Singleton}
import models.foorprint.{Footprint, Todo}
import repository.FootprintRepository

import scala.concurrent.{Await, ExecutionContext, Future}

/**
  *
  * @author chenhuanming
  *         Created at 2018/9/29
  */
@Singleton
class TodoService @Inject()(repository: FootprintRepository)(implicit exec: ExecutionContext) {

  def getTodayTodo(owner: String): Future[Seq[Footprint]] = repository.load(owner, Footprint.Status.TODO.id.toShort, LocalDate.now atStartOfDay, LocalDateTime.now())


  def addTodo(owner: String, desc: String): Future[Todo] = {
    repository.create(owner, desc)
  }

  def toBO(id: Int): Option[TodoBO] = {
    import scala.concurrent.duration._
    val f = Await.result(repository.get(id), 5 second)
    if (f isEmpty) None else Some(new TodoBO(f.get, repository))
  }
}

class TodoBO(todo: Todo, repository: FootprintRepository) {
  def done(): Future[Footprint] = {
    val footprint = Footprint build todo
    val updated = footprint.copy(status = Footprint.Status.DONE.id.toShort, doneTime = Some(LocalDateTime.now()))
    repository.update(updated)
  }
}

