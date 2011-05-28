import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import soupy.orm.{Env, Query}

class QuerySpec extends Spec with ShouldMatchers {
  it("should toSQL correctly") {
    val query = new Query().from("users").where("name like ?", "%liu")
    println(query.toString)
    println(Env.adapter.toSQL(query))

    Env.adapter.toSQL(query) should equal("select *\nfrom users\nwhere name like ?")
  }
}