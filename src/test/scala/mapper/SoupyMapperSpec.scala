package mapper

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import soupy.mapper.{Table, Model, TableDef}

class SoupyMapperSpec extends Spec with ShouldMatchers {
  it("define tables correctly") {
    trait UserDef extends TableDef {
      var age = property[Int]("age")
    }

    class User extends Model with UserDef

    object users extends Table with UserDef

    val user = new User
    user.age = 32
    user.age should equal(32)

    users.age.name should equal("age")
  }
}