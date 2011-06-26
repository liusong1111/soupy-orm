package soupy.mapper

class PropertyBuilder[T: ClassManifest : PropertyAccessor, M: ClassManifest : Table] {
  def apply(index: Int, columnName: String, title: Option[String] = None, primary: Boolean = false): Property[T, M] = {
    new Property[T, M](index, columnName, title, primary)
  }
}

class ValueBuilder[T](theDefaultValue: => T) {
  def defaultValue = theDefaultValue
}

