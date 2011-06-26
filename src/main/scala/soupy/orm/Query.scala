package soupy.orm

import mappers.IntMapper
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

  def paginate(page: Int = 1, pageSize: Int = 20): Query = {
    val limit = pageSize
    val offset = (page - 1) * pageSize

    this.copy(_limit = Some(limit), _offset = Some(offset))
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

  //  def where(_whereClause: String, args: Map[String, _]): Query = {
  //    val _where = new MapRawCriteria(_whereClause, args)
  //    val the_where = if (this._where.isEmpty) {
  //      _where
  //    } else {
  //      this._where.get.where(_where)
  //    }
  //
  //    this.copy(_where = Some(the_where))
  //  }

  def where(_whereClause: String, args: Any*): Query = {
    val _where = new ListRawCriteria(_whereClause, args.toList)
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

  def all[A](implicit mapper: Mapper[A], repository: Repository): List[A] = {
    SQL(repository.adapter.toSQL(this)).all[A](mapper, repository)
  }

  def first[A](implicit mapper: Mapper[A], repository: Repository): Option[A] = {
    SQL(repository.adapter.toSQL(this.limit(1))).first[A](mapper, repository)
  }

  def count(implicit repository: Repository): Int = {
    SQL(repository.adapter.toCountSQL(this)).first(IntMapper, repository).get
  }

  def destroyAll(implicit repository: Repository): Int = {
    SQL(repository.adapter.toDeleteSQL(this)).executeUpdate(repository)
  }
}