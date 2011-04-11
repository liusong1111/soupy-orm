package soupy.orm

import java.sql.{PreparedStatement, ResultSet, Connection}

trait Repository {
  def within(executor: Connection => Unit)
}

object Repository {
  def executeQuery(sql:String)(callback: ResultSet => Unit): Unit = {
    Env.repository.within {
      conn =>
        var st: PreparedStatement = null
        var rs: ResultSet = null
        try {
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