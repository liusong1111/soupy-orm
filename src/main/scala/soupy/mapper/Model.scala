package soupy.mapper

import java.util.Date
import java.math.BigDecimal
import properties._
import soupy.orm.{Insert, Repository}

trait Model extends TableDef {
  type R[T] = T

  //  override
  implicit val IntBuilder: AccessorBuilder[Int, Int] = new IntValueBuilder[this.type]
  implicit val DoubleBuilder: AccessorBuilder[Double, Double] = new DoubleValueBuilder[this.type]
  implicit val StringBuilder: AccessorBuilder[String, String] = new StringValueBuilder[this.type]
  implicit val DateBuilder: AccessorBuilder[Date, Date] = new DateValueBuilder[this.type]
  implicit val BigDecimalBuilder: AccessorBuilder[BigDecimal, BigDecimal] = new BigDecimalValueBuilder[this.type]

  private var _indexCounter = 0

  def property[T](name: String)(implicit builder: AccessorBuilder[T, T]): T = {
    _indexCounter += 1
    builder(name, _indexCounter)
  }

  def insert[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    val table = t.asInstanceOf[Table[this.type]]
    val pairs = table.properties.map(p => p.name -> p.get(this))
    val insert = Insert(table.tableName, pairs:_*)
    val id = insert.executeUpdate

    id
  }

  def update[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    val table = t.asInstanceOf[Table[this.type]]
    val pairs = table.properties.map(p => p.name -> p.get(this))
    val insert = Insert(table.tableName, pairs:_*)
    val id = insert.executeUpdate

    id
  }

  def destroy[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    val table = t.asInstanceOf[Table[this.type]]
    val pairs = table.properties.map(p => p.name -> p.get(this))
    val insert = Insert(table.tableName, pairs:_*)
    val id = insert.executeUpdate

    id
  }

  def save[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    val table = t.asInstanceOf[Table[this.type]]
    val pairs = table.properties.map(p => p.name -> p.get(this))
    val insert = Insert(table.tableName, pairs:_*)
    val id = insert.executeUpdate

    id
  }
}