package orm

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import soupy.orm.{Delete, Update, Insert}
import soupy.orm.adapters.{OracleAdapter, MysqlAdapter}
import java.text.SimpleDateFormat

class ModifySpec extends Spec with ShouldMatchers {
  describe("MysqlAdapter") {
    implicit val adapter = MysqlAdapter

    it("insert should be correct") {
      var s = new Insert("users", "name" -> "liusong", "age" -> 22)
      s.toSQL should equal("insert into users(name, age) values('liusong', 22)")

      val sdf = new SimpleDateFormat("yyyy-MM-dd")
      var birth = sdf.parse("2011-06-28")
      s = new Insert("users", "name" -> "liusong", "age" -> 22, "birth" -> birth)
      s.toSQL should equal("insert into users(name, age, birth) values('liusong', 22, '2011-06-28')")

    }

    it("update should be correct") {
      var s = new Update("users", "age = 22")
      s.toSQL should equal("update users set age = 22")
      s = new Update("users", "age = 22", Some("name like '%liu%'"))
      s.toSQL should equal("update users set age = 22 where name like '%liu%'")
    }

    it("delete should be correct") {
      var s = new Delete("users")
      s.toSQL should equal("delete from users")
      s = new Delete("users", Some("name like '%liu%'"))
      s.toSQL should equal("delete from users where name like '%liu%'")
    }
  }

  describe("OracleAdapter") {
    implicit val adapter = OracleAdapter
    it("insert should be correct") {
      val sdf = new SimpleDateFormat("yyyy-MM-dd")
      var birth = sdf.parse("2011-06-28")
      var s = new Insert("users", "name" -> "liusong", "age" -> 22, "birth" -> birth)
      s.toSQL should equal("insert into users(name, age, birth) values('liusong', 22, to_date('2011-06-28','yyyy-mm-dd'))")
    }
  }
}