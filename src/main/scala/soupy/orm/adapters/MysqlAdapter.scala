package soupy.orm.adapters

import soupy.orm.{Adapter}

class MysqlAdapter extends Adapter {
  def paginate(sql: String, limit: Option[Int], offset: Option[Int]): String = {
    List[Option[String]](Some(sql),
      limit.map("limit " + _.toString),
      offset.map("offset " + _.toString)).filter(!_.isEmpty).mkString("\n")
  }
}