import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import soupy.orm.adapters.{OracleAdapter, MysqlAdapter}
import soupy.orm.{Env, Query}

class QuerySpec extends Spec with ShouldMatchers {
  it("generic toSQL should be correct") {
    val query = new Query().from("users").where("name like ?", "%liu")
    val sql1 = MysqlAdapter.toSQL(query)
    val sql2 = OracleAdapter.toSQL(query)
    sql1 should equal(sql2)
    sql1 should equal("select *\nfrom users\nwhere name like ?")
  }

  it("mysql adapter should generate right sql with pagination"){
    val q = new Query("users").where("name like ?", "%liu").limit(20).offset(100)
    MysqlAdapter.toSQL(q) should equal("select *\nfrom users\nwhere name like ?\nlimit 20\noffset 100")
  }

  it("oracle adapter should generate right sql with pagination"){
    val q = new Query("users").where("name like ?", "%liu").limit(20).offset(100)
    OracleAdapter.toSQL(q) should equal("select * from (select A.*, ROWNUM rn from (select *\nfrom users\nwhere name like ?) where ROWNUM <= 120) where rn >= 20)")
  }
}