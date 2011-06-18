package soupy.mapper

trait AccessorBuilder[T, +R] {
  def apply(name: String, index: Int): R
}