package soupy.orm

import net.lag.configgy.Configgy
import net.lag.logging.Logger

object Env {
  Configgy.configure(getClass.getResource("/env.conf").getPath)
  val config = Configgy.config 

  val mode = config.getString("mode", "development")
  
  val logger = Logger.get

  val db = config.getConfigMap("db").get.getConfigMap(mode).get

  implicit val repository:Repository = {
    db.getString("repository").getOrElse("default") match{
      case _ => new soupy.orm.repositories.DefaultRepository(Adapter(db.getString("adapter", "mysql")), db)
    }
  }
}