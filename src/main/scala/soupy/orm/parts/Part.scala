package soupy.orm.parts

trait Part{
  def toSQL:String

  override def toString = toSQL
}



