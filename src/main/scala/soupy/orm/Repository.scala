package soupy.orm

import java.sql.{PreparedStatement, ResultSet, Connection}

trait Repository {
  def within(executor: Connection => Unit)
}

object Repository {
  def executeQuery(repository:Repository, query:Query, callback: ResultSet => Unit): Unit = {
    repository.within {
      conn =>
        var st: PreparedStatement = null
        var rs: ResultSet = null
        try {
          val sql = query.toSQL
//          soupy.persistence.logger.debug(sql)
          st = conn.prepareStatement(sql)
          rs = st.executeQuery
          while (rs.next) {
            callback(rs)
          }
        } finally {
          try {
            rs.close()
          } catch {
            case _ => None
          }
          try {
            st.close()
          } catch {
            case _ => None
          }
        }
    }
  }
}