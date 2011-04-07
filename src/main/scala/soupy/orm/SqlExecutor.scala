package soupy.orm

import java.sql.{PreparedStatement, ResultSet}
import net.lag.configgy.Configgy
import net.lag.logging.Logger

object SqlExecutor{
  def main(args: Array[String]) {
    Configgy.configure("/home/sliu/test/soupy-orm/src/main/resources/orm.conf")
    val config = Configgy.config
    val logger = Logger.get
    val host = config.getString("host")
    val m = config.getConfigMap("orm").get

    logger.debug("adapter is:" + m.getString("adapter"))
  }
}