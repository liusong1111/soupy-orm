package config

import soupy.orm.mappers.ReflectMapper
import soupy.orm.{SoupyConfig, Adapter, Repository, Mapper}

object Setting extends SoupyConfig {
  val mode = config.getString("mode", "development")

  implicit def getDefaultMapper[A: ClassManifest]: Mapper[A] = new ReflectMapper[A]

  implicit val repository: Repository = {
    val db = config.getConfigMap("db").get.getConfigMap(mode).get

    db.getString("repository").getOrElse("default") match {
      case _ => new soupy.orm.repositories.DefaultRepository(Adapter(db.getString("adapter", "mysql")),
        Map("database" -> db.getString("database").get,
          "user" -> db.getString("user", "root"),
          "password" -> db.getString("password", "")
        ))
    }
  }
}