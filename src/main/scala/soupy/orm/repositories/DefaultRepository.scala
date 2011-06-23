package soupy.orm.repositories

import java.sql.{DriverManager, Connection}
import soupy.orm.{Adapter, Repository}

class DefaultRepository(override val adapter: Adapter, override val settings: Map[String, String]) extends Repository(adapter, settings) {

  //TODO: use a connection pool or container to emit connection
  override def getConnection: Connection = {
    val database = settings("database")
    val user = settings.getOrElse("user", "root")
    val password = settings.getOrElse("password", "")
    val conn = DriverManager.getConnection("jdbc:mysql:///" + database,
      user, password)

    conn
  }

}