package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import java.util.Date
import soupy.mapper.{Table, AccessorBuilder, Property}

class DateProperty[M:ClassManifest:Table](override val name: String, override val index: Int) extends Property[Date, M](name, index) {
  override def read(rs: ResultSet): Date = {
    rs.getDate(index)
  }

  override def write(ps: PreparedStatement, value: Date) = {
    ps.setDate(index, new java.sql.Date(value.getTime))
  }
}

class DatePropertyBuilder[M:ClassManifest:Table] extends AccessorBuilder[Date, DateProperty[M]] {
  override def apply(name: String, index: Int): DateProperty[M] = {
    new DateProperty[M](name, index)
  }
}

class DateValueBuilder[M:ClassManifest] extends AccessorBuilder[Date, Date] {
  override def apply(name: String, index: Int): Date = {
    new Date()
  }
}