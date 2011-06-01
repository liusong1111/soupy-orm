package soupy.orm

import java.sql.{PreparedStatement, ResultSet, Connection}

abstract class Repository(val adapter:Adapter, val settings:Map[String, String]) {
  def within(executor: Connection => Unit)
}

object Repository {
  import soupy.orm.Env._
  def executeQuery(sql:String)(callback: ResultSet => Unit)(implicit repository:Repository) : Unit = {
    repository.within {
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