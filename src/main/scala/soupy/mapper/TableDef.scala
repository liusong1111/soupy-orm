package soupy.mapper

trait TableDef {
  type R[T]

  //  implicit val StringBuilder: PropertyBuilder[String, P[String]]
  implicit val IntBuilder: AccessorBuilder[Int, R[Int]]

  def property[T](name: String)(implicit builder: AccessorBuilder[T, R[T]]): R[T]
}