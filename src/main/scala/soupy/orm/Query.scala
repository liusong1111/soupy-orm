package soupy.orm

import java.sql.{PreparedStatement, ResultSet}
import parts._

case class Query(val _from: String = null,
                 val _select: Option[String] = None,
                 val _join: Option[String] = None,
                 val _where: Option[Criteria] = None,
                 val _order: Option[String] = None,
                 val _group: Option[String] = None,
                 val _having: Option[String] = None,
                 val _offset: Option[Int] = None,
                 val _limit: Option[Int] = None) {
  def from(_from: String): Query = {
    this.copy(_from = _from)
  }

  def select(_select: String): Query = {
    this.copy(_select = Some(_select))
  }

  def join(_join: String): Query = {
    this.copy(_join = Some(_join))
  }

  def group(_group: String): Query = {
    this.copy(_group = Some(_group))
  }

  def having(_having: String): Query = {
    this.copy(_group = Some(_having))
  }

  def offset(_offset: Int): Query = {
    this.copy(_offset = Some(_offset))
  }

  def limit(_limit: Int): Query = {
    this.copy(_limit = Some(_limit))
  }

  // composite order
  def order(_order: String): Query = {
    this.copy(_order = Some(_order))
  }

  // composite criteria
  def where(_where: Criteria): Query = {
    val the_where = if (this._where.isEmpty) {
      _where
    } else {
      this._where.get.where(_where)
    }

    this.copy(_where = Some(the_where))
  }

  def &&(_where: Criteria): Query = {
    where(_where)
  }

  def ||(_where: Criteria): Query = {
    val the_where = if (this._where.isEmpty) None else Some(this._where.get || _where)

    this.copy(_where = the_where)
  }

  def toSQL: String = {
    val sql = List[Option[String]](
      (if (_select.isEmpty) Some("select *") else _select),
      Some("from " + _from),
      _join,
      _where.map("where " + _.toSQL),
      _order.map("order by " + _),
      _group.map("group by " + _),
      _having.map("having " + _),
      _limit.map("limit " + _.toString),
      _offset.map("offset " + _.toString)
    ).filter(!_.isEmpty).map(_.get).mkString("\n")

    sql
  }



  override def toString = toSQL
}