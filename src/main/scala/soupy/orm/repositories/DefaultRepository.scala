package soupy.orm.repositories

import java.sql.{ResultSet, PreparedStatement, DriverManager, Connection}
import soupy.orm.{Env, Adapter, Repository}

class DefaultRepository(override val adapter: Adapter, override val settings: Map[String, String]) extends Repository(adapter, settings) {
  val logger = Env.logger

  def withinConnection(callback: Connection => Unit) {
    val conn = getConnection
    try {
      callback(conn)
    } finally {
      try {
        conn.close()
      } catch {
        case _ => None
      }
    }
  }

  def executeQuery(sql: String)(callback: ResultSet => Unit): Unit = {
    withinConnection {
      conn =>
        var st: PreparedStatement = null
        var rs: ResultSet = null
        try {
          logger.debug(sql)
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

  def executeUpdate(sql: String): Int = {
    var rowCount = 0

    withinConnection {
      conn =>
        var st: PreparedStatement = null
        try {
          logger.debug(sql)
          st = conn.prepareStatement(sql)
          rowCount = st.executeUpdate
        } finally {
          try {
            st.close
          } catch {
            case _ => None
          }
        }
    }

    rowCount
  }

  //TODO: use a connection pool or container to emit connection
  private def getConnection: Connection = {
    val database = settings("database")
    val user = settings.getOrElse("user", "root")
    val password = settings.getOrElse("password", "")
    val conn = DriverManager.getConnection("jdbc:mysql:///" + database,
      user, password)

    conn
  }

}