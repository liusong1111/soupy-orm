package soupy.orm.mappers

import soupy.orm.Mapper
import java.sql.ResultSet
import java.beans.Introspector

class DefaultMapper[A:Manifest] extends Mapper[A]{
  def map(rs: ResultSet):A = {
    val manifest = implicitly[Manifest[A]]
    val clazz = manifest.erasure
    val instance = clazz.newInstance
    val beanInfo = Introspector.getBeanInfo(clazz)
    for(val f <- beanInfo.getPropertyDescriptors){
      val writer = f.getWriteMethod
      writer.invoke(instance, rs.getObject(f.getName))
    }

    instance.asInstanceOf[A]
  }
}