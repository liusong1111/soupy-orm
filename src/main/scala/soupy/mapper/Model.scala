package soupy.mapper

import soupy.orm.utils.SqlEncoder
import soupy.orm.{Update, Insert, Repository}
import java.util.Date
import java.math.BigDecimal
import soupy.orm.parts.{EqualCriteria, Criteria}

trait Model extends TableDef {
  type R[T] = T
  type Builder[T] = ValueBuilder[T]

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
    val id = insert.executeUpdate

    id
  }

  def update[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    val table = t.asInstanceOf[Table[this.type]]
    val pairs = table.properties.map(p => p.columnName -> p.get(this))
    val sets = (for ((propName, propValue) <- pairs) yield (propName + "=" + SqlEncoder.encode(propValue))).mkString(",")
    val criteria = table.primaryProperties.map(property => property.columnName + " = " + SqlEncoder.encode(property.get(this)))
    var where = if(criteria.isEmpty) None else Some(criteria.mkString(" AND "))
    val insert = Update(table.tableName, sets, where)

    insert.executeUpdate
  }

  def destroy[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    val table = t.asInstanceOf[Table[this.type]]
    val pairs = table.properties.map(p => p.columnName -> p.get(this))
    val insert = Insert(table.tableName, pairs: _*)
    val id = insert.executeUpdate

    id
  }

  def save[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    val table = t.asInstanceOf[Table[this.type]]
    val pairs = table.properties.map(p => p.columnName -> p.get(this))
    val insert = Insert(table.tableName, pairs: _*)
    val id = insert.executeUpdate

    id
  }
}