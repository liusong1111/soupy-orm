package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import soupy.mapper._
import java.util.Date

class DateProperty[M:ClassManifest:Table](override val name: String, override val index: Int) extends Property[Date, M](name, index) {
  override def read(rs: ResultSet): Date = {
    rs.getDate(index)
  }

  override def write(ps: PreparedStatement, value: Date) = {
    ps.setDate(index, new java.sql.Date(value.getTime))
  }
}

class DatePropertyBuilder[M:ClassManifest:Table] extends PropertyBuilder[Date, DateProperty[M]] {
  override def apply(name: String, index: Int): DateProperty[M] = {
    new DateProperty[M](name, index)
  }
}

class DateValueBuilder[M:ClassManifest] extends ValueBuilder[Date] {
  def defaultValue : Date = new Date()
}