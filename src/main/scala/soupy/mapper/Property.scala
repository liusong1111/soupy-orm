package soupy.mapper

import java.sql.{PreparedStatement, ResultSet}

abstract class Property[T](val name: String, val index: Int) {
  def read(rs: ResultSet): T

  def write(ps: PreparedStatement, value: T)
}