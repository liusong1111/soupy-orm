package soupy.orm

import java.sql.ResultSet

abstract class Mapper[A:Manifest]{
  def map(rs:ResultSet):A
}