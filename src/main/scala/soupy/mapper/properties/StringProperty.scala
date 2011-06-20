package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import soupy.mapper.{AccessorBuilder, Property}
import soupy.orm.parts.LikeCriteria

class StringProperty(override val name: String, override val index: Int) extends Property[String](name, index) {
  override def read(rs: ResultSet): String = {
    rs.getString(index)
  }

  override def write(ps: PreparedStatement, value: String) = {
    ps.setString(index, value)
  }

  def like(value: String) = new LikeCriteria(this.name, value)
}

object StringPropertyBuilder extends AccessorBuilder[String, StringProperty] {
  override def apply(name: String, index: Int): StringProperty = {
    new StringProperty(name, index)
  }
}

object StringValueBuilder extends AccessorBuilder[String, String] {
  override def apply(name: String, index: Int): String = {
    ""
  }
}