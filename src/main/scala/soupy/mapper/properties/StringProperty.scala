package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import soupy.mapper._

object StringPropertyAccessor extends PropertyAccessor[String] {
  override def read(rs: ResultSet, index:Int): String = {
    rs.getString(index)
  }

  override def write(ps: PreparedStatement, index:Int, value: String) = {
    ps.setString(index, value)
  }
}
