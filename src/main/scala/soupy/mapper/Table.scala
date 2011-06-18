package soupy.mapper

import properties.IntPropertyBuilder
import soupy.orm.{Mapper, Query}
import java.sql.ResultSet

class Table[M: ClassManifest](val tableName: String) extends Mapper[M] with TableDef {
  type R[T] = Property[T]

  //  implicit val StringBuilder: PropertyBuilder[String, P[String]]
  //  override
  implicit val IntBuilder: AccessorBuilder[Int, Property[Int]] = IntPropertyBuilder

  var properties = List[Property[Any]]()

  def property[T](name: String)(implicit builder: AccessorBuilder[T, Property[T]]): Property[T] = {
    val index = (properties.length + 1)
    val prop = builder(name, index)
    properties = properties ::: List(prop.asInstanceOf[Property[Any]])
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