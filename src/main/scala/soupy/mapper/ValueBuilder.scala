package soupy.mapper

class ValueBuilder[T](theDefaultValue: => T){
  def defaultValue = theDefaultValue
}
