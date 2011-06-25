package soupy.mapper

trait AccessorBuilder[T, +R] {
  def apply(columnName: String, index: Int): R
}