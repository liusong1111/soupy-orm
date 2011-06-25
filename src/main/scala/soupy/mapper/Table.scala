package soupy.mapper

import java.math.BigDecimal
import soupy.orm.{Mapper, Query}
import java.sql.ResultSet
import java.util.Date

class Table[M: ClassManifest](val tableName: String) extends Mapper[M] with TableDef {
  import soupy.mapper.properties._
  
  implicit val self: Table[M] = this.asInstanceOf[Table[M]]

  type R[T] = Property[T, M]
  type Builder[T] = PropertyBuilder[T, M]

  implicit val IntBuilder: Builder[Int] = new PropertyBuilder[Int, M]
  implicit val DoubleBuilder: Builder[Double] = new PropertyBuilder[Double, M]
  implicit val StringBuilder: Builder[String] = new PropertyBuilder[String, M]
  implicit val DateBuilder: Builder[Date] = new PropertyBuilder[Date, M]
  implicit val BigDecimalBuilder: Builder[BigDecimal] = new PropertyBuilder[BigDecimal, M]

  var properties = List[Property[Any, M]]()

  def property[T](columnName: String)(implicit builder: Builder[T]): Property[T, M] = {
    val index = (properties.length + 1)
    val prop = builder(columnName, index)
    properties = properties ::: List(prop.asInstanceOf[Property[Any, M]])
    prop
  }

  lazy val columnsString = properties.map(_.columnName).mkString(",")

  //mapper
  def map(rs: ResultSet): M = {
    val clazz = implicitly[ClassManifest[M]]
    val instance = clazz.erasure.newInstance.asInstanceOf[M]
    properties.foreach {
      prop =>
        val v = prop.read(rs)
        prop.set(instance, v)
    }

    instance
  }

  //for convenience
  def q = {
    Query(tableName).select("select " + columnsString)
  }
}