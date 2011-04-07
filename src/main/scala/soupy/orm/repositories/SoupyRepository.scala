package soupy.orm.repositories

import soupy.orm.Repository
import java.sql.{DriverManager, Connection}

class SoupyRepository(val name: String, val setting: Map[String, String])  extends Repository {
  //TODO:hard code to mysql for now.
  def getConnection: Connection = {
    Class.forName("com.mysql.jdbc.Driver").newInstance
    val conn = DriverManager.getConnection("jdbc:mysql:///" + setting("database"),
      setting("user"), setting("password"))

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