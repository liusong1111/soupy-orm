package soupy.orm

import utils.SqlEncoder

trait Modify {
  def toSQL(implicit adapter:Adapter): String

  def executeUpdate(implicit repository: Repository): Int = {
    SQL(toSQL(repository.adapter)).executeUpdate(repository)
  }
}


case class Update(from: String, sets: String, where: Option[String] = None) extends Modify {
  override def toSQL(implicit adapter:Adapter) = {
    ("update " + from + " set " + sets) + (if (where.isEmpty) "" else (" where " + where.get))
  }
}

case class Delete(from: String, where: Option[String] = None) extends Modify {
  override def toSQL(implicit adapter:Adapter) = {
    "delete from " + from + (if (where.isEmpty) "" else (" where " + where.get))
  }
}

case class Insert(from: String, pairs: Pair[String, Any]*) extends Modify {
  //  var generatedId: Long = -1L

  override def toSQL(implicit adapter:Adapter) = {
    val fields = pairs.map(_._1).mkString(", ")
    val values = pairs.map(_._2).map(SqlEncoder.encode(_)).mkString(", ")

    "insert into " + from + "(" + fields + ") values(" + values + ")"
  }

  //  override def executeUpdate(implicit repository: Repository): Int = {
  //    val sql = toSQL
  //    logger.debug(sql)
  //    var result = false
  //    repository.withinConnection {
  //      conn =>
  //        var st: PreparedStatement = null
  //        try {
  //          st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
  //          result = st.executeUpdate != 0
  //          val ret = st.getGeneratedKeys
  //          if (ret.next) {
  //            generatedId = ret.getLong(1)
  //          } else {
  //            result = false
  //          }
  //        } finally {
  //          try {
  //            st.close
  //          } catch {
  //            case _ => None
  //          }
  //        }
  //    }
  //
  //    generatedId.toInt
  //  }
}