package soupy.orm.adapters

import soupy.orm.{Adapter}

class OracleAdapter extends Adapter {
  def paginate(sql: String, limit: Option[Int], offset: Option[Int]): String = {
    val min = limit.get
    val max = min + offset.get
    """select * from
        (select A.*, ROWNUM rn
        from (""" + sql + """)
        where ROWNUM <= """ + max + """)
        where rn >= """ + min + """)
     """
  }
}