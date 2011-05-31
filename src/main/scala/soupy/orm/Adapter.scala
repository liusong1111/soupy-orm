package soupy.orm

import adapters.{MysqlAdapter, OracleAdapter}
import java.lang.Exception

trait Adapter {
  def toSQL(query: Query): String = {
    var sql = List[Option[String]](
      (if (query._select.isEmpty) Some("select *") else query._select),
      Some("from " + query._from),
      query._join,
      query._where.map("where " + _.toSQL),
      query._order.map("order by " + _),
      query._group.map("group by " + _),
      query._having.map("having " + _),
      query._limit.map("limit " + _.toString),
      query._offset.map("offset " + _.toString)
    ).filter(!_.isEmpty).map(_.get).mkString("\n")

    if (!query._limit.isEmpty || !query._offset.isEmpty) {
      sql = paginate(sql, query._limit, query._offset)
    }

    sql
  }

  def toCountSQL(query: Query): String = {
    val sql = List[Option[String]](
      Some("select count(1)"),
      Some("from " + query._from),
      query._join,
      query._where.map("where " + _.toSQL),
      query._order.map("order by " + _),
      query._group.map("group by " + _),
      query._having.map("having " + _)
    ).filter(!_.isEmpty).map(_.get).mkString("\n")

    sql
  }

  def paginate(sql: String, limit: Option[Int], offset: Option[Int]): String
}

object Adapter{
  def apply(adapterName:String):Adapter = {
    adapterName match{
      case "mysql" => MysqlAdapter
      case "oracle" => OracleAdapter
      case _ => throw new Exception("adaper named \"" + adapterName + "\" not found")
    }
  }
}