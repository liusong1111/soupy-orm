import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import reflect.BeanInfo
import soupy.orm.{Repository, Env, Query}

@BeanInfo
class User {
  var name: String = ""
  var age: Int = 0
}

class MapperSpec extends Spec with ShouldMatchers {
  describe("mappers") {
    describe("DefaultMapper") {
      it("should executeQuery correctly") {
        val query = new Query().from("users").where("name like '%liu'")
        println(query.toString)
        println(Env.repository.adapter.toSQL(query))
        Env.repository.adapter.toSQL(query) should equal("select *\nfrom users\nwhere name like '%liu'")

//        Repository.executeQuery("select *\nfrom users\nwhere name like '%liu%'") {
//          rs =>
////            val u = Env.mapper.map[User](rs).get
////            println("-------")
////            println(u.name)
////            println(u.age)
//        }
      }
    }
  }
}