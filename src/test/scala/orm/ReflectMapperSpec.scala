package orm

import java.sql.ResultSet
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import reflect.BeanInfo
import soupy.orm.{Mapper, Query}

@BeanInfo
class User {
  var name: String = ""
  var age: Int = 0
}

class ReflectMapperSpec extends Spec with ShouldMatchers {
  describe("mappers") {
    import config.Setting._

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
        val _firstUser = query.first[User](UserMapper, repository)
        _firstUser.isEmpty should be(false)
        val firstUser = _firstUser.get
        firstUser.name should equal("liusong")

        val list = query.all[User](UserMapper, repository)
        list.size should not be (0)

        query.count should not be (0)
        //        list.foreach(user => println(user.name + "-" + user.age))
      }

    }

    describe("ReflectMapper") {
      it("executeQuery with implicit repository and ReflectMapper should work correctly") {
        val query = new Query().from("users").where("name = 'liusong'")
        val _firstUser = query.first[User]
        _firstUser.isEmpty should be(false)
        val firstUser = _firstUser.get
        firstUser.name should equal("liusong")

        val list = query.all[User]
        list.size should not be (0)
        //        list.foreach(user => println(user.name + "-" + user.age))
      }
    }
  }
}