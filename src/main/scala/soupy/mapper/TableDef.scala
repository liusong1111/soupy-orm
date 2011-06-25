package soupy.mapper

import java.util.Date
import java.math.BigDecimal

trait TableDef {
  type R[T]
  type Builder[T] <: AccessorBuilder[T, R[T]]

  implicit val IntBuilder: Builder[Int]
  implicit val StringBuilder: Builder[String]
  implicit val DateBuilder: Builder[Date]
  implicit val DoubleBuilder: Builder[Double]
  implicit val BigDecimalBuilder: Builder[BigDecimal]

  def property[T](name: String)(implicit builder: Builder[T]): R[T]
}