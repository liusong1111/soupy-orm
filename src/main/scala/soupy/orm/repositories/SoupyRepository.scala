package soupy.orm.repositories

import java.sql.{DriverManager, Connection}
import soupy.orm.{Env, Repository}

class SoupyRepository extends Repository {
  def getConnection: Connection = {
    Class.forName("com.mysql.jdbc.Driver").newInstance
    val database = Env.orm.getString("database").get
    val user = Env.orm.getString("user").get
    val password = Env.orm.getString("password").get
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