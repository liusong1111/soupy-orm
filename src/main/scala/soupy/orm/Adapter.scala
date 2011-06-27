package soupy.orm

import adapters.{MysqlAdapter, OracleAdapter}
import java.lang.Exception

trait Adapter {
  def toSQL(query: Query): String

  def toDeleteSQL(query: Query): String = {
    assert(query._limit.isEmpty)
    assert(query._offset.isEmpty)

    implicit val adapter = this

    val sql = List[Option[String]](
      Some("delete from " + query._from),
      query._join,
      query._where.map("where " + _.toSQL),
      query._order.map("order by " + _),
      query._group.map("group by " + _),
      query._having.map("having " + _)
    ).filter(!_.isEmpty).map(_.get).mkString(" ")

    sql
  }

  def toCountSQL(query: Query): String = {
    implicit val adapter = this

    val sql = List[Option[String]](
      Some("select count(1)"),
      Some("from " + query._from),
      query._join,
      query._where.map("where " + _.toSQL),
      query._group.map("group by " + _),
      query._having.map("having " + _)
    ).filter(!_.isEmpty).map(_.get).mkString(" ")

    sql
  }
}

object Adapter {
  def apply(adapterName: String): Adapter = {
    adapterName match {
      case "mysql" => MysqlAdapter
      case "oracle" => OracleAdapter
      case _ => throw new Exception("adaper named \"" + adapterName + "\" not found")
    }
  }
}