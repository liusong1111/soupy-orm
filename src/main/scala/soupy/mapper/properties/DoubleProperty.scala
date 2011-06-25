package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import soupy.mapper._

class DoubleProperty[M:ClassManifest:Table](override val name: String, override val index: Int) extends Property[Double, M](name, index) {
  override def read(rs: ResultSet): Double = {
    rs.getDouble(index)
  }

  override def write(ps: PreparedStatement, value: Double) = {
    ps.setDouble(index, value)
  }
}

class DoublePropertyBuilder[M:ClassManifest:Table] extends PropertyBuilder[Double, DoubleProperty[M]] {
  override def apply(name: String, index: Int): DoubleProperty[M] = {
    new DoubleProperty[M](name, index)
  }
}

class DoubleValueBuilder[M:ClassManifest] extends ValueBuilder[Double] {
  def defaultValue:Double = 0.0
}