package soupy.orm.mappers

import soupy.orm.Mapper
import java.sql.ResultSet

class SoupyMapper extends Mapper{
  def map[A](rs: ResultSet)(implicit manifest: Manifest[A]) = {
    val instance = manifest.erasure.newInstance
    instance.asInstanceOf[A]
  }
}