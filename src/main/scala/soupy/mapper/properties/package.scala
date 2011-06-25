package soupy.mapper

package object properties{
  implicit val _BigDecimalPropertyAccessor = BigDecimalPropertyAccessor
  implicit val _DatePropertyAccessor = DatePropertyAccessor
  implicit val _DoublePropertyAccessor = DoublePropertyAccessor
  implicit val _IntPropertyAccessor = IntPropertyAccessor
  implicit val _StringPropertyAccessor = StringPropertyAccessor
}