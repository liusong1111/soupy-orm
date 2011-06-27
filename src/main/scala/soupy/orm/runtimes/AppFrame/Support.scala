package soupy.orm.runtimes.AppFrame

import soupy.mapper.TableRegistry
import soupy.orm._
import adapters.OracleAdapter

trait AppFrameService {
  protected def connectionProvider: ConnectionProvider

  def executeUpdate(sql: String): Int = {
    connectionProvider.executeUpdate(sql)
  }

  def executeQuery(sql: String, modelClassName: String): Array[Object] = {
    val table = TableRegistry.peek(modelClassName).get

    var result = List[Object]()
    connectionProvider.executeQuery(sql) {
      rs =>
        result = result ::: List(table.map(rs).asInstanceOf[Object])
    }

    result.toArray[Object]
  }
}

abstract class AppFrameRepository extends Repository {
  val service: AppFrameService

  val adapter: Adapter = OracleAdapter

  val connectionProvider: ConnectionProvider

  def executeQuery[A: ClassManifest : Mapper](sql: String): List[A] = {
    val modelClassName = implicitly[ClassManifest[A]].erasure.getName
    service.executeQuery(sql, modelClassName).map {
      obj => obj.asInstanceOf[A]
    }.toList
  }

  def executeUpdate(sql: String): Int = {
    service.executeUpdate(sql)
  }
}