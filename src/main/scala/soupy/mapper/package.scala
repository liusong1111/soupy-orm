package soupy

package object mapper{
  implicit val BigDecimalPropertyAccessor = properties.BigDecimalPropertyAccessor
  implicit val DatePropertyAccessor = properties.DatePropertyAccessor
  implicit val DoublePropertyAccessor = properties.DoublePropertyAccessor
  implicit val IntPropertyAccessor = properties.IntPropertyAccessor
  implicit val StringPropertyAccessor = properties.StringPropertyAccessor
}