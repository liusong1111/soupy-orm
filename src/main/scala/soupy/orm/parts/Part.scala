package soupy.orm.parts

import soupy.orm.Adapter

trait Part {

  def toSQL(implicit adapter: Adapter): String

  def toPrepare(implicit adapter: Adapter): (String, List[_]) = (toSQL, Nil)

  //  override def toString = toSQL
}



