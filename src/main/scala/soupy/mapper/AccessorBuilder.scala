package soupy.mapper

class PropertyBuilder[T: ClassManifest : PropertyAccessor, M: ClassManifest : Table]{
  def apply(columnName: String, title:Option[String], index: Int): Property[T, M] = {
    new Property[T, M](columnName, title, index)
  }
}

class ValueBuilder[T](theDefaultValue: => T){
  def defaultValue = theDefaultValue
}

