package soupy.mapper

import properties.IntPropertyBuilder

trait Table extends TableDef {
  type R[T] = Property[T]

  //  implicit val StringBuilder: PropertyBuilder[String, P[String]]
  override implicit val IntBuilder: AccessorBuilder[Int, Property[Int]] = IntPropertyBuilder

  var properties = List[Property[Any]]()

  def property[T](name: String)(implicit builder: AccessorBuilder[T, Property[T]]): Property[T] = {
    val index = (properties.length + 1)
    val prop = builder(name, index)
    properties = properties ::: List(prop.asInstanceOf[Property[Any]])
    prop
  }
}