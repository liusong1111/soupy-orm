package soupy.orm.adapters

import soupy.orm.{Query, Adapter}
import java.util.Date

object MysqlAdapter extends Adapter {
  Class.forName("com.mysql.jdbc.Driver").newInstance

  def toSQL(query: Query): String = {
    implicit val adapter = this

    val sql = List[Option[String]](
      Some(query._select.getOrElse("select *")),
      Some("from " + query._from),
      query._join,
      query._where.map("where " + _.toSQL),
      query._order.map("order by " + _),
      query._group.map("group by " + _),
      query._having.map("having " + _),
      query._limit.map("limit " + _.toString),
      query._offset.map("offset " + _.toString)
    ).filter(!_.isEmpty).map(_.get).mkString(" ")

    sql
  }

  override def encode(value: Any) = {
    value match {
      case s: String => "'" + singleQuoteRegexp.replaceAllIn(s, "''") + "'"
      //encode Date
      case d: Date => "'" + dateFormat.format(d) + "'"
      case _ => value.toString
    }
  }
}