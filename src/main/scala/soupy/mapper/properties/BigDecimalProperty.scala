package soupy.mapper.properties

import java.sql.{PreparedStatement, ResultSet}
import java.math.BigDecimal
import soupy.mapper._

object BigDecimalPropertyAccessor extends PropertyAccessor[BigDecimal]{
  override def read(rs: ResultSet, index:Int): BigDecimal = {
    rs.getBigDecimal(index)
  }

  override def write(ps: PreparedStatement, index:Int, value: BigDecimal) = {
    ps.setBigDecimal(index, value)
  }
}
