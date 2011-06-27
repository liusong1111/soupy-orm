package soupy.orm

import java.sql.{PreparedStatement, ResultSet, Connection}

trait Repository {
  val adapter: Adapter

  val connectionProvider: ConnectionProvider

  def executeQuery[A](sql: String)(implicit mapper: Mapper[A]): List[A]

  def executeUpdate(sql: String): Int
}

