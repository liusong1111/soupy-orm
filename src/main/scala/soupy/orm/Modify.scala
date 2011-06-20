package soupy.orm

import parts.Criteria
import java.sql.{Statement, PreparedStatement}
import utils.SqlEncoder

import Env._

trait Modify {
  def toSQL: String

  def executeUpdate(implicit repository: Repository): Int = {
    val sql = toSQL
    logger.debug(sql)
    val result = repository.executeUpdate(sql)
    result
  }
}


case class Update(from: String, sets: String, criteria: Option[Criteria] = None) extends Modify {
  override def toSQL = {
    ("update " + from + " set " + sets) + (if (criteria.isEmpty) "" else (" where " + criteria.get.toSQL))
  }
}

case class Delete(from: String, criteria: Option[Criteria] = None) extends Modify {
  override def toSQL = {
    "delete " + from + (if (criteria.isEmpty) "" else (" where " + criteria.get.toSQL))
  }
}

case class Insert(from: String, pairs: Pair[String, Any]*) extends Modify {
  var generatedId: Long = -1L

  override def toSQL = {
    val fields = pairs.map(_._1).mkString(", ")
    val values = pairs.map(_._2).map(SqlEncoder.encode(_)).mkString(", ")

    "insert into " + from + "(" + fields + ") values(" + values + ")"
  }

  override def executeUpdate(implicit repository: Repository): Int = {
    val sql = toSQL
    logger.debug(sql)
    var result = false
    repository.withinConnection {
      conn =>
        var st: PreparedStatement = null
        try {
          st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
          result = st.executeUpdate != 0
          val ret = st.getGeneratedKeys
          if (ret.next) {
            generatedId = ret.getLong(1)
          } else {
            result = false
          }
        } finally {
          try {
            st.close
          } catch {
            case _ => None
          }
        }
    }

    generatedId.toInt
  }
}