package soupy.mapper

import soupy.orm.utils.SqlEncoder
import java.util.Date
import java.math.BigDecimal
import soupy.orm.{Delete, Update, Insert, Repository}

trait Model extends TableDef {
  type R[T] = T
  type Builder[T] = ValueBuilder[T]

  var isNew: Boolean = true

  //  override
  implicit val IntBuilder = new ValueBuilder[Int](0)
  implicit val DoubleBuilder = new ValueBuilder[Double](0.0)
  implicit val StringBuilder = new ValueBuilder[String]("")
  implicit val DateBuilder = new ValueBuilder[Date](new Date())
  implicit val BigDecimalBuilder = new ValueBuilder[BigDecimal](new BigDecimal("0.0"))

  def property[T](columnName: String, title: Option[String] = None, primary: Boolean = false)(implicit builder: Builder[T]): T = {
    builder.defaultValue
  }

  def insert[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    val table = t.asInstanceOf[Table[this.type]]
    val pairs = table.properties.map(p => p.columnName -> p.get(this))
    val insert = Insert(table.tableName, pairs: _*)

    val result = insert.executeUpdate

    this.isNew = false

    result
  }

  def update[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    val table = t.asInstanceOf[Table[this.type]]
    val pairs = table.properties.map(p => p.columnName -> p.get(this))
    val sets = (for ((propName, propValue) <- pairs) yield (propName + "=" + SqlEncoder.encode(propValue))).mkString(",")
    assert(!table.primaryProperties.isEmpty)
    val where = table.primaryProperties.map(property => property.columnName + " = " + SqlEncoder.encode(property.get(this))).mkString(" AND ")
    val insert = Update(table.tableName, sets, Some(where))

    val result = insert.executeUpdate
    this.isNew = false

    result
  }

  def destroy[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    val table = t.asInstanceOf[Table[this.type]]
    assert(!table.primaryProperties.isEmpty)
    val where = table.primaryProperties.map(property => property.columnName + " = " + SqlEncoder.encode(property.get(this))).mkString(" AND ")
    val delete = Delete(table.tableName, Some(where))

    delete.executeUpdate
  }

  def save[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    if(this.isNew){
      this.insert(t, repository)
    }else{
      this.update(t, repository)
    }
  }

  //----
  def fillByMap[M >: this.type](map: Map[String, String], _properties:Option[List[Property[_, _]]] = None)(implicit t: Table[M]) = {
      t.fillByMap(this, map, _properties)
  }
}