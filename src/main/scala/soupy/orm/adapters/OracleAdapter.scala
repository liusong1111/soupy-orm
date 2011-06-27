package soupy.orm.adapters

import soupy.orm.{Query, Adapter}
import java.util.Date

object OracleAdapter extends Adapter {
  def toSQL(query: Query): String = {
    implicit val adapter = this

    var sql = List[Option[String]](
      (if (query._select.isEmpty) Some("select *") else query._select),
      Some("from " + query._from),
      query._join,
      query._where.map("where " + _.toSQL),
      query._order.map("order by " + _),
      query._group.map("group by " + _),
      query._having.map("having " + _)
    ).filter(!_.isEmpty).map(_.get).mkString(" ")

    if (!query._limit.isEmpty || !query._offset.isEmpty) {
      val min = query._limit.getOrElse(20)
      val max = min + query._offset.getOrElse(0)
      sql = "select * from (select A.*, ROWNUM rn from (" +
        sql +
        ") where ROWNUM <= " +
        max +
        ") where rn >= " + min + ")"
    }

    sql
  }

  override def encode(value: Any) = {
    value match {
      case s: String => "'" + singleQuoteRegexp.replaceAllIn(s, "''") + "'"
      //encode Date
      case d: Date => "to_date('" + dateFormat.format(d) + "','yyyy-mm-dd')"
      case _ => value.toString
    }
  }
}