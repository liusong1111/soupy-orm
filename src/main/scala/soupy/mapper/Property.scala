package soupy.mapper

import java.sql.{PreparedStatement, ResultSet}
import soupy.orm.parts._
import java.beans.Introspector

abstract class Property[T: ClassManifest, M:ClassManifest:Table](val name: String, val index: Int) {
  def read(rs: ResultSet): T

  def write(ps: PreparedStatement, value: T)

  def get(m: M): T = {
    lazy val getter = {
      val propertyDescriptor = getPropertyDescriptor(m)
      propertyDescriptor.getReadMethod
    }
    getter.invoke(m).asInstanceOf[T]
  }

  def set(m: M, v: T) = {
    lazy val setter = {
      val propertyDescriptor = getPropertyDescriptor(m)
      propertyDescriptor.getWriteMethod
    }

    if(v == null){
      setter.invoke(m, null)
    }else{
      setter.invoke(m, v.asInstanceOf[Object])
    }
  }

  private def getPropertyDescriptor[M](m: M)(implicit c: ClassManifest[M], t: Table[M]) = {
    val clazz = c.erasure
    val tableClazz = t.getClass
    Introspector.getBeanInfo(tableClazz).getPropertyDescriptors.foreach(p => p.getName)
    val propertyName = Introspector.getBeanInfo(tableClazz).getPropertyDescriptors.flatMap {
      p =>
        val reader = p.getReadMethod.invoke(t)
        if (!reader.isInstanceOf[Property[_, M]]) {
          None
        } else {
          val prop = reader.asInstanceOf[Property[_, M]]
          if(prop.name == name){
            Some(p.getName)
          }else{
            None
          }
        }
    }.head

    Introspector.getBeanInfo(clazz).getPropertyDescriptors.find(p => p.getName == propertyName).get
  }


  //criteria
  def >(value: T) = new NormalCriteria(this.name, ">", value)

  def >=(value: T) = new NormalCriteria(this.name, ">=", value)

  def <(value: T) = new NormalCriteria(this.name, "<", value)

  def <=(value: T) = new NormalCriteria(this.name, "<=", value)

  def ==(value: T) = new NormalCriteria(this.name, "=", value)

  def !=(value: T) = new NormalCriteria(this.name, "<>", value)

  def isNull = new IsNullCriteria(this.name)

  def isNotNull = new IsNotNullCriteria(this.name)

  def in(values: List[T]) = new InCriteria(this.name, values)

  def like(value: String) = new LikeCriteria(this.name, value)

  //order
  def asc = this.name + " ASC"

  def desc = this.name + " DESC"
}