package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import soupy.mapper.{Table, AccessorBuilder, Property}

class StringProperty[M:ClassManifest:Table](override val name: String, override val index: Int) extends Property[String, M](name, index) {
  override def read(rs: ResultSet): String = {
    rs.getString(index)
  }

  override def write(ps: PreparedStatement, value: String) = {
    ps.setString(index, value)
  }
}

class StringPropertyBuilder[M:ClassManifest:Table] extends AccessorBuilder[String, StringProperty[M]] {
  override def apply(name: String, index: Int): StringProperty[M] = {
    new StringProperty[M](name, index)
  }
}

class StringValueBuilder[M:ClassManifest] extends AccessorBuilder[String, String] {
  override def apply(name: String, index: Int): String = {
    ""
  }
}