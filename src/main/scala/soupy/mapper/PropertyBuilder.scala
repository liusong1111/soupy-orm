package soupy.mapper


class PropertyBuilder[T: ClassManifest : PropertyAccessor, M: ClassManifest : Table] extends AccessorBuilder[T, Property[T, M]] {
  override def apply(columnName: String, index: Int): Property[T, M] = {
    new Property[T, M](columnName, index)
  }
}