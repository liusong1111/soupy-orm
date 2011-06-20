package soupy.mapper

import java.util.Date

trait TableDef {
  type R[T]

  //  implicit val StringBuilder: PropertyBuilder[String, P[String]]
  implicit val IntBuilder: AccessorBuilder[Int, R[Int]]
  implicit val StringBuilder: AccessorBuilder[String, R[String]]
  implicit val DateBuilder: AccessorBuilder[Date, R[Date]]

  def property[T](name: String)(implicit builder: AccessorBuilder[T, R[T]]): R[T]
}