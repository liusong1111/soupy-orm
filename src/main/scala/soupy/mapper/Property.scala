package soupy.mapper

import java.sql.{PreparedStatement, ResultSet}
import soupy.orm.parts._
import java.beans.Introspector
import java.lang.RuntimeException

trait PropertyAccessor[T] {
  def read(rs: ResultSet, index: Int): T

  def write(ps: PreparedStatement, index: Int, value: T)

  def encode(value: T): String

  def decode(value: String): T
}

class Property[T: ClassManifest : PropertyAccessor, M: ClassManifest : Table](val index: Int,
                                                                              val columnName: String,
                                                                              val title: Option[String] = None,
                                                                              val primary: Boolean = false
                                                                               ) {
  val table = implicitly[Table[M]]
  private val propertyAccessor = implicitly[PropertyAccessor[T]]

  def read(rs: ResultSet): T = {
    propertyAccessor.read(rs, index)
  }

  def write(ps: PreparedStatement, value: T): Unit = {
    propertyAccessor.write(ps, index, value)
  }

  def encode(value: T): String = propertyAccessor.encode(value)

  def decode(value: String): T = propertyAccessor.decode(value)

  lazy val propertyName = getPropertyName
  lazy val propertyDescriptor = getPropertyDescriptor

  lazy val getter = propertyDescriptor.getReadMethod
  lazy val setter = propertyDescriptor.getWriteMethod

  def get(m: M): T = {
    getter.invoke(m).asInstanceOf[T]
  }

  def set(m: M, v: T) = {
    setter.invoke(m, v.asInstanceOf[Object])
  }

  private def getPropertyName = {
    val t = implicitly[Table[M]]
    val tableClazz = t.getClass
    Introspector.getBeanInfo(tableClazz).getPropertyDescriptors.foreach(p => p.getName)
    val propertyName = Introspector.getBeanInfo(tableClazz).getPropertyDescriptors.flatMap {
      p =>
        val reader = p.getReadMethod.invoke(t)
        if (!reader.isInstanceOf[Property[_, M]]) {
          None
        } else {
          val prop = reader.asInstanceOf[Property[_, M]]
          if (prop.columnName == columnName) {
            Some(p.getName)
          } else {
            None
          }
        }
    }.head

    propertyName
  }

  private def getPropertyDescriptor = {
    val clazz = implicitly[ClassManifest[M]].erasure
    val result = Introspector.getBeanInfo(clazz).getPropertyDescriptors.find(p => p.getName == propertyName).get
    if ((result.getWriteMethod eq null) || (result.getReadMethod eq null)) {
      throw new RuntimeException("please ensure the property[" + propertyName + "] is defined as a var")
    }

    result
  }


  //criteria
  def >(value: T) = new NormalCriteria(this.columnName, ">", value)

  def >=(value: T) = new NormalCriteria(this.columnName, ">=", value)

  def <(value: T) = new NormalCriteria(this.columnName, "<", value)

  def <=(value: T) = new NormalCriteria(this.columnName, "<=", value)

  def ==(value: T) = new NormalCriteria(this.columnName, "=", value)

  def !=(value: T) = new NormalCriteria(this.columnName, "<>", value)

  def isNull = new IsNullCriteria(this.columnName)

  def isNotNull = new IsNotNullCriteria(this.columnName)

  def in(values: List[T]) = new InCriteria(this.columnName, values)

  def like(value: String) = new LikeCriteria(this.columnName, value)

  //order
  def asc = this.columnName + " ASC"

  def desc = this.columnName + " DESC"
}