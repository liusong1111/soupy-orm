package soupy.orm

import mappers.ReflectMapper
import net.lag.configgy.Configgy
import net.lag.logging.Logger

object SoupyConfig extends SoupyConfig

class SoupyConfig {
  Configgy.configure(getClass.getResource("/env.conf").getPath)
  val config = Configgy.config

  def logger: Logger = Logger.get

//  val mode = config.getString("mode", "development")
//
//  implicit def getDefaultMapper[A: ClassManifest]: Mapper[A] = new ReflectMapper[A]
//
//  implicit val repository: Repository = {
//    val db = config.getConfigMap("db").get.getConfigMap(mode).get
//
//    db.getString("repository").getOrElse("default") match {
//      case _ => new soupy.orm.repositories.DefaultRepository(Adapter(db.getString("adapter", "mysql")),
//        Map("database" -> db.getString("database").get,
//          "user" -> db.getString("user", "root"),
//          "password" -> db.getString("password", "")
//        ))
//    }
//  }
}

