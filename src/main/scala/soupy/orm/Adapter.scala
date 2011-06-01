package soupy.orm

import adapters.{MysqlAdapter, OracleAdapter}
import java.lang.Exception

trait Adapter {
  def toSQL(query: Query): String
  
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