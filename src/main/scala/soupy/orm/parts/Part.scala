package soupy.orm.parts

trait Part {
  def toSQL: String

  def toPrepare: (String, List[_]) = (toSQL, Nil)

  override def toString = toSQL
}



