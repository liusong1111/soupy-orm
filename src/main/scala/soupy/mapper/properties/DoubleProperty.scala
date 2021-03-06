package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import soupy.mapper._

object DoublePropertyAccessor extends PropertyAccessor[Double] {
  override def read(rs: ResultSet, index: Int): Double = {
    rs.getDouble(index)
  }

  override def write(ps: PreparedStatement, index: Int, value: Double) = {
    ps.setDouble(index, value)
  }

  override def encode(value: Double): String = value.toString

  override def decode(value: String): Double = value.toDouble
}
