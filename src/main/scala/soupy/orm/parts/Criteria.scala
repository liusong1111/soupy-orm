package soupy.orm.parts

import soupy.orm.utils.SqlEncoder

trait Criteria extends Part{
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
  override def toSQL = prop + " " + op + " " + SqlEncoder.encode(value)
}

class EqualCriteria[A](override val prop:String, override val value:A) extends NormalCriteria(prop, "=", value)
class NotEqualCriteria[A](override val prop:String, override val value:A) extends NormalCriteria(prop, "<>", value)
class GreatThanCriteria[A](override val prop:String, override val value:A) extends NormalCriteria(prop, ">", value)
class GreatEqualCriteria[A](override val prop:String, override val value:A) extends NormalCriteria(prop, ">=", value)
class LessThanCriteria[A](override val prop:String, override val value:A) extends NormalCriteria(prop, "<", value)
class LessEqualCriteria[A](override val prop:String, override val value:A) extends NormalCriteria(prop, "<=", value)

class LikeCriteria(val prop: String, val value: String) extends SimpleCriteria {
  override def toSQL = prop + " like " + SqlEncoder.encode(value)
}

class IsNullCriteria(val prop: String) extends SimpleCriteria {
  override def toSQL = prop + " is null"
}

class IsNotNullCriteria(val prop: String) extends SimpleCriteria {
  override def toSQL = prop + " is not null"
}

class InCriteria[A](val prop: String, val values: List[A]) extends SimpleCriteria {
  override def toSQL = {
    if (!values.isEmpty) {
      prop + " in (" + values.map {
        value => SqlEncoder.encode(value)
      }.mkString(",") + ")"
    } else {
      " 1=1 "
    }
  }
}

trait CompositeCriteria extends Criteria

class AndCriteria(val criterias: Criteria*) extends CompositeCriteria {
  override
  def toSQL = {
    criterias.map {
      criteria => if (criteria.isInstanceOf[OrCriteria]) "(" + criteria.toSQL + ")" else criteria.toSQL
    }.mkString(" AND ")
  }
}

class OrCriteria(val criterias: Criteria*) extends CompositeCriteria {
  override
  def toSQL = {
    criterias.map {
      criteria => if (criteria.isInstanceOf[AndCriteria]) "(" + criteria.toSQL + ")" else criteria.toSQL
    }.mkString(" OR ")
  }
}


