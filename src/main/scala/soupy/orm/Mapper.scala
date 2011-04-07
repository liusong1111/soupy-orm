package soupy.orm

import java.sql.ResultSet

trait Mapper{
  def map[A](rs:ResultSet)(implicit manifest:Manifest[A]):Option[A]
}