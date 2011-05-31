package soupy.orm.repositories

import java.sql.{DriverManager, Connection}
import soupy.orm.{Adapter, Env, Repository}
import net.lag.configgy.ConfigMap

class DefaultRepository(override val adapter:Adapter, override val configMap:ConfigMap) extends Repository(adapter, configMap) {
  //TODO: use a connection pool or container to emit connection
  def getConnection: Connection = {
    val database = configMap.getString("database").get
    val user = configMap.getString("user", "root")
    val password = configMap.getString("password", "")
    val conn = DriverManager.getConnection("jdbc:mysql:///" + database,
      user, password)

    conn
  }

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
}