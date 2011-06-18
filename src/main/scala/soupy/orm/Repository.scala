package soupy.orm

import java.sql.{ResultSet, Connection}

abstract class Repository(val adapter: Adapter, val settings: Map[String, String]) {
  def withinConnection(callback: Connection => Unit)

  def executeQuery(sql: String)(callback: ResultSet => Unit): Unit

  def executeUpdate(sql: String): Int
}