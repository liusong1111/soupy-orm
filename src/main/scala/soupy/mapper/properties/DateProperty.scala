package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import soupy.mapper._
import java.util.Date
import java.text.SimpleDateFormat

object DatePropertyAccessor extends PropertyAccessor[Date] {
  override def read(rs: ResultSet, index: Int): Date = {
    rs.getDate(index)
  }

  override def write(ps: PreparedStatement, index: Int, value: Date) = {
    ps.setDate(index, new java.sql.Date(value.getTime))
  }

  override def encode(value: Date): String = value.toString

  override def decode(value: String): Date = new SimpleDateFormat("yyyy-MM-dd").parse(value)
}
