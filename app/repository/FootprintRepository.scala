package repository

import java.sql.Timestamp
import java.time.{LocalDateTime, ZoneId}

import javax.inject.{Inject, Singleton}
import models.foorprint.{Footprint, Todo}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
  *
  * @author chenhuanming
  *         Created at 2018/9/28
  */
@Singleton
class FootprintRepository @Inject()(@NamedDatabase("footprint") protected val dbConfigProvider: DatabaseConfigProvider)
                                   (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  implicit val localDateTimeColumnType = MappedColumnType.base[LocalDateTime, Timestamp](
    localDateTime => Timestamp.valueOf(localDateTime),
    ts => LocalDateTime.ofInstant(ts.toInstant, ZoneId.systemDefault())
  )

  private val Footprints = TableQuery[FootprintTable]

  def create(owner: String, desc: String): Future[Todo] = db.run {
    val createTime = LocalDateTime.now
    (Footprints.map(f => (f.owner, f.desc, f.createTime))
      returning Footprints.map(_.id)
      into ((ownerDesc, id) => Todo(id, ownerDesc._1, ownerDesc._2, createTime))
      ) += (owner, desc, createTime)
  }

  def update(footprint: Footprint): Future[Footprint] = db.run {
    Footprints.filter(_.id === footprint.id).update(footprint).map(_ => footprint)
  }

  def get(id: Int): Future[Option[Footprint]] = db.run(Footprints.filter(_.id === id).result.headOption)

  def load(owner: String, status: Short, from: LocalDateTime, to: LocalDateTime): Future[Seq[Footprint]] =
    db.run(Footprints.filter(f => f.owner === owner && f.status === status && f.createTime >= from && f.createTime <= to).result)

  private class FootprintTable(tag: Tag) extends Table[Footprint](tag, "footprint") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def owner = column[String]("owner")

    def desc = column[String]("desc")

    def status = column[Short]("status")

    def createTime = column[LocalDateTime]("createTime")

    def doneTime = column[LocalDateTime]("doneTime")

    def remark = column[String]("remark")

    def * = (id, owner, desc, status, createTime, doneTime.?, remark.?) <> ((Footprint.apply _).tupled, Footprint.unapply)
  }

}
