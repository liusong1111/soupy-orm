package soupy.orm

import java.sql.ResultSet

abstract class Mapper[A: ClassManifest] {
  def map(rs: ResultSet): A
}