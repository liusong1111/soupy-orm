package soupy.mapper

import java.sql.{PreparedStatement, ResultSet}
import soupy.orm.parts.{InCriteria, IsNotNullCriteria, IsNullCriteria, NormalCriteria}
abstract class Property[T: ClassManifest](val name: String, val index: Int) {
  def read(rs: ResultSet): T

  def write(ps: PreparedStatement, value: T)

  //TODO: use scalaj-reflect library to recognize the correct setter/getter, instead of database field name
  def get[M: ClassManifest](m: M): T = {
    lazy val getter = {
      val clazz = implicitly[ClassManifest[M]].erasure
      clazz.getDeclaredMethod(name)
    }
    getter.invoke(m).asInstanceOf[T]
  }

  def set[M: ClassManifest](m: M, v: T) = {
    lazy val setter = {
      val clazz = implicitly[ClassManifest[M]].erasure
      clazz.getDeclaredMethods.filter(_.getName == name + "_$eq").head
    }

    setter.invoke(m, v.asInstanceOf[Object])
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

  //order
  def asc = this.name + " ASC"

  def desc = this.name + " DESC"
}