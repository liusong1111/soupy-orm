package mapper

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import soupy.mapper.{Table, Model, TableDef}
import soupy.orm.adapters.MysqlAdapter
import config.Setting
import reflect.BeanInfo

trait UserDef extends TableDef {
  var age1 = property[Int]("age")
}

@BeanInfo
class User extends Model with UserDef

@BeanInfo
object User extends Table[User]("users") with UserDef

class SoupyMapperSpec extends Spec with ShouldMatchers {
  it("define tables correctly") {

    val user = new User
    user.age1 = 32
    user.age1 should equal(32)

    User.age1.name should equal("age")

    val query = User.q.where(User.age1 > 1)
    MysqlAdapter.toSQL(query) should equal("select age from users where age > 1")

    implicit val repository = Setting.repository
    implicit val U = User

    query.first[User].get.age1 > 1 should be(true)
  }
}