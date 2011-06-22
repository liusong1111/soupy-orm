package soupy.mapper

import java.util.Date
import java.math.BigDecimal

trait TableDef {
  type R[T]

  implicit val IntBuilder: AccessorBuilder[Int, R[Int]]
  implicit val StringBuilder: AccessorBuilder[String, R[String]]
  implicit val DateBuilder: AccessorBuilder[Date, R[Date]]
  implicit val DoubleBuilder: AccessorBuilder[Double, R[Double]]
  implicit val BigDecimalBuilder: AccessorBuilder[BigDecimal, R[BigDecimal]]

  def property[T](name: String)(implicit builder: AccessorBuilder[T, R[T]]): R[T]
}