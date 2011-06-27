package soupy.orm

case class SQL(sql: String) {
  def all[A](implicit mapper: Mapper[A], repository: Repository): List[A] = {
    repository.executeQuery(sql)(mapper)
  }

  def first[A](implicit mapper: Mapper[A], repository: Repository): Option[A] = {
    repository.executeQuery(sql)(mapper).headOption
  }

  def executeUpdate(implicit repository: Repository): Int = {
    repository.executeUpdate(sql)
  }
}
