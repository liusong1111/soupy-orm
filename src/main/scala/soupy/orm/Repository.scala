package soupy.orm

trait Repository {
  val adapter: Adapter

  val connectionProvider: ConnectionProvider

  def executeQuery[A: ClassManifest : Mapper](sql: String): List[A]

  def executeUpdate(sql: String): Int
}

