package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import soupy.mapper._

class StringProperty[M:ClassManifest:Table](override val name: String, override val index: Int) extends Property[String, M](name, index) {
  override def read(rs: ResultSet): String = {
    rs.getString(index)
  }

  override def write(ps: PreparedStatement, value: String) = {
    ps.setString(index, value)
  }
}

class StringPropertyBuilder[M:ClassManifest:Table] extends PropertyBuilder[String, StringProperty[M]] {
  override def apply(name: String, index: Int): StringProperty[M] = {
    new StringProperty[M](name, index)
  }
}

class StringValueBuilder[M:ClassManifest] extends ValueBuilder[String] {
  def defaultValue = ""
}