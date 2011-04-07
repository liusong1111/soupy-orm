package soupy.orm

import adapters.{OracleAdapter, MysqlAdapter}
import net.lag.configgy.Configgy
import net.lag.logging.Logger

object Env {
  Configgy.configure("/home/sliu/test/soupy-orm/src/main/resources/orm.conf")
  val config = Configgy.config
  val logger = Logger.get
  val orm = config.getConfigMap("orm").get

  val adapter = {
    val adapterName = orm.getString("adapter").get
    adapterName match{
      case "oracle" => new MysqlAdapter
      case "mysql" => new OracleAdapter
    }
  }

  val mapper = {
    val mapperClassName = orm.getString("mapper", "soupy.orm.mapper.SoupyMapper")
    Class.forName(mapperClassName).newInstance.asInstanceOf[Mapper]
  }

  var repository = {
    val repositoryClassName = orm.getString("repository", "soupy.orm.SoupyRepository")
    Class.forName(repositoryClassName).newInstance.asInstanceOf[Repository]
  }
}