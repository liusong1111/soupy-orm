package soupy.orm

import mappers.DefaultMapper
import net.lag.configgy.Configgy
import net.lag.logging.Logger

object Env {
  Configgy.configure(getClass.getResource("/env.conf").getPath)
  val config = Configgy.config

  val mode = config.getString("mode", "development")

  def logger:Logger = Logger.get

  implicit def getDefaultMapper[A:Manifest]:Mapper[A] = new DefaultMapper[A]
  
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