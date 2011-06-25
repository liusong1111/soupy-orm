package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import java.math.BigDecimal
import soupy.mapper._

class BigDecimalProperty[M:ClassManifest:Table](override val name: String, override val index: Int) extends Property[BigDecimal, M](name, index) {
  override def read(rs: ResultSet): BigDecimal = {
    rs.getBigDecimal(index)
  }

  override def write(ps: PreparedStatement, value: BigDecimal) = {
    ps.setBigDecimal(index, value)
  }
}

class BigDecimalPropertyBuilder[M:ClassManifest:Table] extends PropertyBuilder[BigDecimal, BigDecimalProperty[M]] {
  override def apply(name: String, index: Int): BigDecimalProperty[M] = {
    new BigDecimalProperty[M](name, index)
  }
}

class BigDecimalValueBuilder[M:ClassManifest] extends ValueBuilder[BigDecimal] {
  def defaultValue: BigDecimal = new BigDecimal("0.0")
}