package soupy.mapper

import properties.IntValueBuilder

trait Model extends TableDef {
  type R[T] = T
  //  override
  implicit val IntBuilder: AccessorBuilder[Int, Int] = IntValueBuilder

  private var _indexCounter = 0

  def property[T](name: String)(implicit builder: AccessorBuilder[T, T]): T = {
    _indexCounter += 1
    builder(name, _indexCounter)
  }
}