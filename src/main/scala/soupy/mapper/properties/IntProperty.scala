package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import soupy.mapper._

object IntPropertyAccessor extends PropertyAccessor[Int] {
  override def read(rs: ResultSet, index:Int): Int = {
    rs.getInt(index)
  }

  override def write(ps: PreparedStatement, index:Int, value: Int) = {
    ps.setInt(index, value)
  }

  override def encode(value: Int): String = value.toString

  override def decode(value: String): Int = value.toInt
}
