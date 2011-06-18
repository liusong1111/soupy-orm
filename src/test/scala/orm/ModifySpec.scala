import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import soupy.orm.adapters.{OracleAdapter, MysqlAdapter}
import soupy.orm.parts.{LikeCriteria, NormalCriteria, Criteria}
import soupy.orm.{Delete, Update, Insert, Query}

class ModifySpec extends Spec with ShouldMatchers {
  it("insert should be correct") {
    var s = new Insert("users",  "name" -> "liusong",  "age" -> 22)
    s.toSQL should equal("insert into users(name, age) values('liusong', 22)")
  }

  it("update should be correct") {
    var s = new Update("users", "age = 22")
    s.toSQL should equal("update users set age = 22")
    s  = new Update("users", "age = 22", Some(new LikeCriteria("name", "%liu%")))
    s.toSQL should equal("update users set age = 22 where name like '%liu%'")
  }

  it("delete should be correct") {
    var s = new Delete("users")
    s.toSQL should equal("delete users")
    s  = new Delete("users", Some(new LikeCriteria("name", "%liu%")))
    s.toSQL should equal("delete users where name like '%liu%'")
  }
}