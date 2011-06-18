package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import soupy.mapper.{AccessorBuilder, Property}

class IntProperty(override val name: String, override val index: Int) extends Property[Int](name, index) {
  override def read(rs: ResultSet): Int = {
    rs.getInt(index)
  }

  override def write(ps: PreparedStatement, value: Int) = {
    ps.setInt(index, value)
  }
}

object IntPropertyBuilder extends AccessorBuilder[Int, IntProperty] {
  override def apply(name: String, index: Int): IntProperty = {
    new IntProperty(name, index)
  }
}

object IntValueBuilder extends AccessorBuilder[Int, Int] {
  override def apply(name: String, index: Int): Int = {
    0
  }
}