package mapper

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import soupy.mapper.{Table, Model, TableDef}
import soupy.orm.adapters.MysqlAdapter

import soupy.orm.Env

trait UserDef extends TableDef {
  var age = property[Int]("age")
}

class User extends Model with UserDef

object User extends Table[User]("users") with UserDef

class SoupyMapperSpec extends Spec with ShouldMatchers {
  it("define tables correctly") {

    val user = new User
    user.age = 32
    user.age should equal(32)

    User.age.name should equal("age")

    val query = User.q.where(User.age > 1)
    MysqlAdapter.toSQL(query) should equal("select age from users where age > 1")

    implicit val repository = Env.repository
    implicit val U = User

    query.first[User].get.age > 1 should be(true)
  }
}