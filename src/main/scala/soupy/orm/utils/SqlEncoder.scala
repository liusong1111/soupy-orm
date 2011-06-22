package soupy.orm.utils

import java.util.Date
import java.text.SimpleDateFormat

object SqlEncoder {
  val singleQuoteRegexp = "'".r
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
  
  def encode(value: Any) = {
    value match {
      case s: String => "'" + singleQuoteRegexp.replaceAllIn(s, "''") + "'"
      case d: Date => "'" + dateFormat.format(d) + "'"
      case _ => value.toString
    }
  }

}