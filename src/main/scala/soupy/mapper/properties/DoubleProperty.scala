package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import soupy.mapper.{AccessorBuilder, Property}

class DoubleProperty(override val name: String, override val index: Int) extends Property[Double](name, index) {
  override def read(rs: ResultSet): Double = {
    rs.getDouble(index)
  }

  override def write(ps: PreparedStatement, value: Double) = {
    ps.setDouble(index, value)
  }
}

object DoublePropertyBuilder extends AccessorBuilder[Double, DoubleProperty] {
  override def apply(name: String, index: Int): DoubleProperty = {
    new DoubleProperty(name, index)
  }
}

object DoubleValueBuilder extends AccessorBuilder[Double, Double] {
  override def apply(name: String, index: Int): Double = {
    0.0
  }
}