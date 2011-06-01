package soupy.orm.repositories

import java.sql.{DriverManager, Connection}
import soupy.orm.{Adapter, Repository}
import net.lag.configgy.ConfigMap

class DefaultRepository(override val adapter: Adapter, override val settings: Map[String, String]) extends Repository(adapter, settings) {
  def within(callback: Connection => Unit) {
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