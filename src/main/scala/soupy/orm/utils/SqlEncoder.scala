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
//      case d: Date => "to_date('" + dateFormat.format(d) + "','yyyy-mm-dd')"
      case _ => value.toString
    }
  }

}