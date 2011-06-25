package soupy.mapper

trait ValueBuilder[T] extends AccessorBuilder[T, T]{
  def defaultValue:T
  def apply(name: String, index: Int): T = defaultValue
}