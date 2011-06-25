package soupy.view

import xml.{Attribute, Text, Null}
import soupy.mapper.Property

class TableFor[T](list: Seq[T], tableId: String = "", showRule: Boolean = true) {
  var columns = List[Column[T, Any]]()

  def column[V](title: String, content: T => V, options: Map[String, String] = Map[String, String]()) {
    columns = columns ::: List(Column(title, content, options))
  }

  def column[PT](property: Property[PT, T]) {
    val options = Map[String, String]()
    column(property, options)
  }

  def column[PT](property: Property[PT, T], options: Map[String, String]) {
    val title = property.title.getOrElse("")
    def content(m: T): PT = property.get(m)
    columns = columns ::: List(Column(title, content, options))
  }

  var carriedProperties = List[Property[_, T]]()

  def data[PT](properties: Property[PT, T]*) = {
    carriedProperties = properties.toList
  }

  override def toString = toHTML.toString

  def toHTML = {
    var elem = <table class="tableFor">
      <thead>
        {renderTitleRow}
      </thead>
      <tbody>
        {list.zipWithIndex.collect {
        case (item, index) => renderDataRow(item, index)
      }}
      </tbody>
    </table>

    if ((tableId ne null) && (tableId != "")) {
      elem = elem % Attribute("id", Text(tableId), Null)
    }

    elem
  }

  def renderTitleRow = {
    <tr>
      {if (showRule) {
      <th>序号</th>
    }}{columns.map(column => <th>
      {column.title}
    </th>)}
    </tr>
  }

  def renderDataRow(item: T, index: Int) = {
    var row = <tr>
      {if (showRule) {
        <td>
          {index + 1}
        </td>
      }}{columns.map(column => renderDataCell(item, column))}
    </tr>

    for (carriedProperty <- carriedProperties) {
      val key = "data-" + carriedProperty.propertyName
      val value = carriedProperty.get(item)
      if (value != null) {
        row = row % Attribute(key, Text(value.toString), Null)
      }
    }

    row
  }

  def renderDataCell[V](item: T, column: Column[T, V]) = {
    var cell = <td>
      {if (column.options.getOrElse("editable", "") != "")
        <div class="editableWrapper">
          {column.content(item)}
        </div>
      else
        column.content(item)}
    </td>

    for ((key, value) <- column.options) {
      cell = cell % Attribute(key, Text(value), Null)
    }

    cell
  }


}

case class Column[T, +V](title: String,
                         content: T => V,
                         options: Map[String, String] = Map[String, String]())