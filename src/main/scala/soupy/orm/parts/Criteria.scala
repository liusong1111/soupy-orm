package soupy.orm.parts

import soupy.orm.Adapter

trait Criteria extends Part {
  def where(criteria: Criteria) = {
    new AndCriteria(this, criteria)
  }

  def orWhere(criteria: Criteria) = {
    new OrCriteria(this, criteria)
  }

  def &&(criteria: Criteria) = {
    where(criteria)
  }

  def ||(criteria: Criteria) = {
    new OrCriteria(this, criteria)
  }
}

trait SimpleCriteria extends Criteria

class NormalCriteria[A](val prop: String, val op: String, val value: A) extends SimpleCriteria {
  override def toSQL(implicit adapter: Adapter) = prop + " " + op + " " + adapter.encode(value)

  override def toPrepare(implicit adapter: Adapter): (String, List[_]) = {
    val sql = prop + " " + op + " ? "
    val params = List(value)
    (sql, params)
  }
}

class EqualCriteria[A](override val prop: String, override val value: A) extends NormalCriteria(prop, "=", value)

class NotEqualCriteria[A](override val prop: String, override val value: A) extends NormalCriteria(prop, "<>", value)

class GreatThanCriteria[A](override val prop: String, override val value: A) extends NormalCriteria(prop, ">", value)

class GreatEqualCriteria[A](override val prop: String, override val value: A) extends NormalCriteria(prop, ">=", value)

class LessThanCriteria[A](override val prop: String, override val value: A) extends NormalCriteria(prop, "<", value)

class LessEqualCriteria[A](override val prop: String, override val value: A) extends NormalCriteria(prop, "<=", value)

class LikeCriteria[A](override val prop: String, override val value: A) extends NormalCriteria(prop, "like", value)

class IsNullCriteria(val prop: String) extends SimpleCriteria {
  override def toSQL(implicit adapter:Adapter) = prop + " is null"
}

class IsNotNullCriteria(val prop: String) extends SimpleCriteria {
  override def toSQL(implicit adapter:Adapter) = prop + " is not null"
}

class InCriteria[A](val prop: String, val values: List[A]) extends SimpleCriteria {
  override def toSQL(implicit adapter:Adapter) = {
    if (!values.isEmpty) {
      prop + " in (" + values.map {
        value => adapter.encode(value)
      }.mkString(",") + ")"
    } else {
      " 1=1 "
    }
  }
}

trait CompositeCriteria extends Criteria

class AndCriteria(val criterias: Criteria*) extends CompositeCriteria {
  override
  def toSQL(implicit adapter: Adapter) = {
    criterias.map {
      criteria => if (criteria.isInstanceOf[OrCriteria]) "(" + criteria.toSQL + ")" else criteria.toSQL
    }.mkString(" AND ")
  }

  override
  def toPrepare(implicit adapter: Adapter) = {
    val stat = criterias.map {
      criteria => if (criteria.isInstanceOf[OrCriteria]) "(" + criteria.toPrepare._1 + ")" else criteria.toPrepare._1
    }.mkString(" AND ")

    val _params = criterias.map {
      criteria => criteria.toPrepare._2
    }

    val params = (List[Any]() /: _params)((a, b) => a ::: b)

    (stat, params)
  }
}

class OrCriteria(val criterias: Criteria*) extends CompositeCriteria {
  override
  def toSQL(implicit adapter: Adapter) = {
    criterias.map {
      criteria => if (criteria.isInstanceOf[AndCriteria]) "(" + criteria.toSQL + ")" else criteria.toSQL
    }.mkString(" OR ")
  }

  override
  def toPrepare(implicit adapter: Adapter) = {
    val stat = criterias.map {
      criteria => if (criteria.isInstanceOf[AndCriteria]) "(" + criteria.toPrepare._1 + ")" else criteria.toPrepare._1
    }.mkString(" OR ")

    val _params = criterias.map {
      criteria => criteria.toPrepare._2
    }

    val params = (List[Any]() /: _params)((a, b) => a ::: b)

    (stat, params)
  }
}

class RawCriteria(val sqlTemplate: String) extends SimpleCriteria {
  override def toSQL(implicit adapter:Adapter) = sqlTemplate
}

class ListRawCriteria(override val sqlTemplate: String, val args: List[_]) extends RawCriteria(sqlTemplate) {
  override def toSQL(implicit adapter:Adapter) = sqlTemplate

  override def toPrepare(implicit adapter: Adapter) = {
    (sqlTemplate, args)
  }
}

//class MapRawCriteria(override val sqlTemplate: String, val args: Map[String, _]) extends RawCriteria(sqlTemplate) {
//
//}


