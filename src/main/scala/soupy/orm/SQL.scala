package soupy.orm

case class SQL(sql: String) {
  def all[A](implicit mapper: Mapper[A], repository: Repository, manifest: ClassManifest[A]): List[A] = {
    repository.executeQuery[A](sql)
  }

  def first[A](implicit mapper: Mapper[A], repository: Repository, manifest: ClassManifest[A]): Option[A] = {
    repository.executeQuery[A](sql).headOption
  }

  def executeUpdate(implicit repository: Repository): Int = {
    repository.executeUpdate(sql)
  }
}
