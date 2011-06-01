import java.sql.ResultSet
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import reflect.BeanInfo
import soupy.orm.{Mapper, Repository, Env, Query}

@BeanInfo
class User {
  var name: String = ""
  var age: Int = 0
}

class MapperSpec extends Spec with ShouldMatchers {
  describe("mappers") {
    describe("customized mapper") {
      it("customized mapper should work") {
        object UserMapper extends Mapper[User] {
          def map(rs: ResultSet): User = {
            val user = new User
            user.name = rs.getString("name")
            user.age = rs.getInt("age")
            user
          }
        }
      }

      
    }

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