package soupy.mapper

import java.util.Date
import java.math.BigDecimal
import properties._

trait Model extends TableDef {
  type R[T] = T
  //  override
  implicit val IntBuilder: AccessorBuilder[Int, Int] = IntValueBuilder
  implicit val DoubleBuilder: AccessorBuilder[Double, Double] = DoubleValueBuilder
  implicit val StringBuilder: AccessorBuilder[String, String] = StringValueBuilder
  implicit val DateBuilder: AccessorBuilder[Date, Date] = DateValueBuilder
  implicit val BigDecimalBuilder: AccessorBuilder[BigDecimal, BigDecimal] = BigDecimalValueBuilder

  private var _indexCounter = 0

  def property[T](name: String)(implicit builder: AccessorBuilder[T, T]): T = {
    _indexCounter += 1
    builder(name, _indexCounter)
  }
}