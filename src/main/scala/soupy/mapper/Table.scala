package soupy.mapper

import java.math.BigDecimal
import properties._
import soupy.orm.{Mapper, Query}
import java.sql.ResultSet
import java.util.Date

class Table[M: ClassManifest](val tableName: String) extends Mapper[M] with TableDef {
  implicit val self:Table[M] = this.asInstanceOf[Table[M]]
  
  type R[T] = Property[T, M]

  implicit val IntBuilder: AccessorBuilder[Int, Property[Int, M]] = new IntPropertyBuilder[M]
  implicit val DoubleBuilder: AccessorBuilder[Double, Property[Double, M]] = new DoublePropertyBuilder[M]
  implicit val StringBuilder: AccessorBuilder[String, Property[String, M]] = new StringPropertyBuilder[M]
  implicit val DateBuilder: AccessorBuilder[Date, Property[Date, M]] = new DatePropertyBuilder[M]
  implicit val BigDecimalBuilder: AccessorBuilder[BigDecimal, Property[BigDecimal, M]] = new BigDecimalPropertyBuilder[M]

  var properties = List[Property[Any, M]]()

  def property[T](name: String)(implicit builder: AccessorBuilder[T, Property[T, M]]): Property[T, M] = {
    val index = (properties.length + 1)
    val prop = builder(name, index)
    properties = properties ::: List(prop.asInstanceOf[Property[Any, M]])
    prop
  }

  lazy val columnsString = properties.map(_.name).mkString(",")

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