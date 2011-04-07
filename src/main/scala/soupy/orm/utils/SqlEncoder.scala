package soupy.orm.utils

object SqlEncoder{
 val singleQuoteRegexp = "'".r

  def encode(value: Any) = {
    value match{
      case s:String => "'" + singleQuoteRegexp.replaceAllIn(s, "''") + "'"
      case _ => value.toString
    }
  }

}