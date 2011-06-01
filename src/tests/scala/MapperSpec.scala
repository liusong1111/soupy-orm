import java.sql.ResultSet
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import reflect.BeanInfo
import soupy.orm.adapters.MysqlAdapter
import soupy.orm.repositories.DefaultRepository
import soupy.orm.{Mapper, Repository, Env, Query}

@BeanInfo
class User {
  var name: String = ""
  var age: Int = 0
}

class MapperSpec extends Spec with ShouldMatchers {
  describe("mappers") {
    val repository = new DefaultRepository(MysqlAdapter, Map("database" -> "abc"))

    describe("customized mapper") {
      it("customized mapper should work correctly") {
        object UserMapper extends Mapper[User] {
          def map(rs: ResultSet): User = {
            val user = new User
            user.name = rs.getString("name")
            user.age = rs.getInt("age")
            user
          }
        }
        val query = new Query().from("users").where("name = 'liusong'")
        val _firstUser = query.first[User](repository, UserMapper)
        _firstUser.isEmpty should be(false)
        val firstUser = _firstUser.get
        firstUser.name should equal("liusong")
      }

    }

    describe("DefaultMapper") {
      it("should executeQuery correctly") {

      }
    }
  }
}