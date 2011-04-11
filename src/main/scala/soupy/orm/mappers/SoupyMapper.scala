package soupy.orm.mappers

import soupy.orm.Mapper
import java.sql.ResultSet
import java.beans.Introspector

class SoupyMapper extends Mapper{
  def map[A](rs: ResultSet)(implicit manifest: Manifest[A]) = {
    val clazz = manifest.erasure
    val instance = clazz.newInstance
    val beanInfo = Introspector.getBeanInfo(clazz)
    for(val f <- beanInfo.getPropertyDescriptors){
      val writter = f.getWriteMethod
      writter.invoke(instance, rs.getObject(f.getName))
    }
    Some(instance.asInstanceOf[A])
  }
}