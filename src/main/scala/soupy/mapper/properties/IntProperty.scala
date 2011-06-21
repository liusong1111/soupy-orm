package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import soupy.mapper.{Table, AccessorBuilder, Property}

class IntProperty[M:ClassManifest:Table](override val name: String, override val index: Int) extends Property[Int, M](name, index) {
  override def read(rs: ResultSet): Int = {
    rs.getInt(index)
  }

  override def write(ps: PreparedStatement, value: Int) = {
    ps.setInt(index, value)
  }
}

class IntPropertyBuilder[M:ClassManifest:Table] extends AccessorBuilder[Int, IntProperty[M]] {
  override def apply(name: String, index: Int): IntProperty[M] = {
    new IntProperty[M](name, index)
  }
}

class IntValueBuilder[M:ClassManifest] extends AccessorBuilder[Int, Int] {
  override def apply(name: String, index: Int): Int = {
    0
  }
}