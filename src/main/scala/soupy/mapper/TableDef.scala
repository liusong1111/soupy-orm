package soupy.mapper

import java.util.Date
import java.math.BigDecimal

trait TableDef {
  type R[T]
  type Builder[T]

  implicit val IntBuilder: Builder[Int]
  implicit val StringBuilder: Builder[String]
  implicit val DateBuilder: Builder[Date]
  implicit val DoubleBuilder: Builder[Double]
  implicit val BigDecimalBuilder: Builder[BigDecimal]

  def property[T](columnName: String, title:Option[String] = None)(implicit builder: Builder[T]): R[T]
}