package soupy.mapper

import properties.{DateValueBuilder, StringValueBuilder, IntValueBuilder}
import java.util.Date

trait Model extends TableDef {
  type R[T] = T
  //  override
  implicit val IntBuilder: AccessorBuilder[Int, Int] = IntValueBuilder
  implicit val StringBuilder: AccessorBuilder[String, String] = StringValueBuilder
  implicit val DateBuilder: AccessorBuilder[Date, Date] = DateValueBuilder

  private var _indexCounter = 0

  def property[T](name: String)(implicit builder: AccessorBuilder[T, T]): T = {
    _indexCounter += 1
    builder(name, _indexCounter)
  }
}