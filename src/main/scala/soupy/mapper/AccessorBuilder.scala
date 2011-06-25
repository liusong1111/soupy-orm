package soupy.mapper

class PropertyBuilder[T: ClassManifest : PropertyAccessor, M: ClassManifest : Table]{
  def apply(columnName: String, index: Int): Property[T, M] = {
    new Property[T, M](columnName, index)
  }
}

class ValueBuilder[T](theDefaultValue: => T){
  def defaultValue = theDefaultValue
}

