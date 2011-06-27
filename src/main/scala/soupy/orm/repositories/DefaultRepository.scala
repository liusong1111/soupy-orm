package soupy.orm.repositories

import soupy.orm.{ConnectionProvider, Adapter, Mapper, Repository}

class DefaultRepository(val adapter: Adapter, val connectionProvider: ConnectionProvider) extends Repository {
  def executeUpdate(sql: String): Int = {
    connectionProvider.executeUpdate(sql)
  }

  def executeQuery[A: ClassManifest : Mapper](sql: String) = {
    val mapper = implicitly[Mapper[A]]
    var result = List[A]()
    connectionProvider.executeQuery(sql) {
      rs =>
        result = result ::: List(mapper.map(rs))
    }

    result
  }
}