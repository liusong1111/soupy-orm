package soupy.orm

import java.sql.ResultSet

case class SQL(sql: String) {


  def all[A](implicit mapper: Mapper[A], repository: Repository): List[A] = {
    var result = List[A]()
    repository.executeQuery(sql) {
      rs =>
        result = mapper.map(rs) :: result
    }

    result
  }

  def first[A](implicit mapper: Mapper[A], repository: Repository): Option[A] = {
    var result: Option[A] = None
    repository.executeQuery(sql) {
      rs =>
        result = Some(mapper.map(rs))
    }

    result
  }

  def executeQuery(callback: ResultSet => Unit)(implicit repository: Repository): Unit = {
    repository.executeQuery(sql)(callback)
  }

  def executeUpdate(implicit repository: Repository): Int = {
    repository.executeUpdate(sql)
  }
}
