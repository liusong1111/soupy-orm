package soupy.orm.mappers

import soupy.orm.Mapper
import java.sql.ResultSet

object IntMapper extends Mapper[Int] {
  def map(rs: ResultSet): Int = {
    rs.getInt(1)
  }
}