package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import soupy.mapper.{AccessorBuilder, Property}
import java.math.BigDecimal

class BigDecimalProperty(override val name: String, override val index: Int) extends Property[BigDecimal](name, index) {
  override def read(rs: ResultSet): BigDecimal = {
    rs.getBigDecimal(index)
  }

  override def write(ps: PreparedStatement, value: BigDecimal) = {
    ps.setBigDecimal(index, value)
  }
}

object BigDecimalPropertyBuilder extends AccessorBuilder[BigDecimal, BigDecimalProperty] {
  override def apply(name: String, index: Int): BigDecimalProperty = {
    new BigDecimalProperty(name, index)
  }
}

object BigDecimalValueBuilder extends AccessorBuilder[BigDecimal, BigDecimal] {
  override def apply(name: String, index: Int): BigDecimal = {
    new BigDecimal("0.0")
  }
}