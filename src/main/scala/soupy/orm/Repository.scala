package soupy.orm

import java.sql.{PreparedStatement, ResultSet, Connection}

abstract class Repository(val adapter: Adapter, val settings: Map[String, String] = Map()) {
  val logger = Env.logger

  // Repository is alias of ConnectionProvider
  def getConnection: Connection

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
}