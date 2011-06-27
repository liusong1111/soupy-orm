package orm

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import soupy.orm.adapters.{OracleAdapter, MysqlAdapter}
import soupy.orm.Query

class QuerySpec extends Spec with ShouldMatchers {
  it("generic toSQL should be correct") {
    val query = new Query().from("users").where("name like ?", "%liu")
    val sql1 = MysqlAdapter.toSQL(query)
    val sql2 = OracleAdapter.toSQL(query)
    sql1 should equal(sql2)
    sql1 should equal("select * from users where name like ?")
  }

  it("mysql adapter should generate right sql with pagination") {
    val q = new Query("users").where("name like ?", "%liu").limit(20).offset(100)
    MysqlAdapter.toSQL(q) should equal("select * from users where name like ? limit 20 offset 100")
  }

  it("oracle adapter should generate right sql with pagination") {
    val q = new Query("users").where("name like ?", "%liu").limit(20).offset(100)
    OracleAdapter.toSQL(q) should equal("select * from (select A.*, ROWNUM rn from (select * from users where name like ?) A where ROWNUM <= 120) where rn >= 20")
  }

  it("toCountSql should be right") {
    val q1 = new Query("users").where("name like ?", "%liu")
    val q2 = new Query("users").where("name like ?", "%liu").limit(20).offset(100)
    val sql1 = MysqlAdapter.toCountSQL(q1)
    val sql2 = MysqlAdapter.toCountSQL(q2)
    sql1 should equal(sql2)
    sql1 should equal("select count(1) from users where name like ?")
  }
}