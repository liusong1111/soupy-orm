package soupy.mapper

import properties._
import soupy.orm.utils.SqlEncoder
import soupy.orm.{Update, Insert, Repository}

trait Model extends TableDef {
  type R[T] = T
  override type Builder[T] = ValueBuilder[T]

  //  override
  implicit val IntBuilder = new IntValueBuilder[this.type]
  implicit val DoubleBuilder = new DoubleValueBuilder[this.type]
  implicit val StringBuilder = new StringValueBuilder[this.type]
  implicit val DateBuilder = new DateValueBuilder[this.type]
  implicit val BigDecimalBuilder = new BigDecimalValueBuilder[this.type]

  private var _indexCounter = 0

  def property[T](name: String)(implicit builder: Builder[T]): T = {
    _indexCounter += 1
    builder(name, _indexCounter)
  }

  def insert[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    val table = t.asInstanceOf[Table[this.type]]
    val pairs = table.properties.map(p => p.name -> p.get(this))
    val insert = Insert(table.tableName, pairs: _*)
    val id = insert.executeUpdate

    id
  }

  def update[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    val table = t.asInstanceOf[Table[this.type]]
    val pairs = table.properties.map(p => p.name -> p.get(this))
    val sets = (for ((propName, propValue) <- pairs) yield (propName + "=" + SqlEncoder.encode(propValue))).mkString(",")
    val insert = Update(table.tableName, sets)

    val fields = pairs.map(_._1).mkString(", ")
    val values = pairs.map(_._2).map(SqlEncoder.encode(_)).mkString(", ")


    val id = insert.executeUpdate

    id
  }

  def destroy[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    val table = t.asInstanceOf[Table[this.type]]
    val pairs = table.properties.map(p => p.name -> p.get(this))
    val insert = Insert(table.tableName, pairs: _*)
    val id = insert.executeUpdate

    id
  }

  def save[M >: this.type](implicit t: Table[M], repository: Repository): Int = {
    val table = t.asInstanceOf[Table[this.type]]
    val pairs = table.properties.map(p => p.name -> p.get(this))
    val insert = Insert(table.tableName, pairs: _*)
    val id = insert.executeUpdate

    id
  }
}