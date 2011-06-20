package soupy.mapper.properties

import soupy.mapper.{AccessorBuilder, Property}
import java.sql.{PreparedStatement, ResultSet}
import java.util.Date

class DateProperty(override val name: String, override val index: Int) extends Property[Date](name, index) {
  override def read(rs: ResultSet): Date = {
    rs.getDate(index)
  }

  override def write(ps: PreparedStatement, value: Date) = {
    ps.setDate(index, new java.sql.Date(value.getTime))
  }
}

object DatePropertyBuilder extends AccessorBuilder[Date, DateProperty] {
  override def apply(name: String, index: Int): DateProperty = {
    new DateProperty(name, index)
  }
}

object DateValueBuilder extends AccessorBuilder[Date, Date] {
  override def apply(name: String, index: Int): Date = {
    new Date()
  }
}