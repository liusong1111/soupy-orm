package mapper

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import soupy.mapper.{Table, Model, TableDef}
import soupy.orm.adapters.MysqlAdapter
import config.Setting
import reflect.BeanInfo
import java.util.Date

//users
trait UserDef extends TableDef {
  var name = property[String]("name", Some("姓名"))
  var age1 = property[Int]("age", primary = true)
  var createdAt = property[Date]("created_at")
}

@BeanInfo
class User extends Model with UserDef {
}

//departments
trait IDepartment extends TableDef {
  var name = property[String]("name")
}

@BeanInfo class Department extends Model with IDepartment

object `package` {

  @BeanInfo implicit object User extends Table[User]("users") with UserDef

  @BeanInfo implicit object Department extends Table[Department]("departments") with IDepartment

}

class SoupyMapperSpec extends Spec with ShouldMatchers {
  it("define tables correctly") {

    val user = new User
    user.age1 = 32
    user.age1 should equal(32)

    User.age1.columnName should equal("age")

    User.name.title should equal(Some("姓名"))
    User.age1.title should be(None)

    val query = User.q.where(User.age1 > 1)
    MysqlAdapter.toSQL(query) should equal("select name,age,created_at from users where age > 1")

    implicit val repository = Setting.repository


    query.first[User].get.age1 > 1 should be(true)
  }

  it("insert correctly") {
    implicit val repository = Setting.repository

    val user = new User
    user.age1 = 33
    val beforeCount = User.q.count
    user.insert
    val afterCount = User.q.count
    (afterCount - beforeCount) should be(1)
  }

  it("update correctly") {
    implicit val repository = Setting.repository

    var id = new Date().getTime.toInt

    //insert
    val user = new User
    user.age1 = id
    user.name = "test update"
    user.isNew should be(true)
    var beforeCount = User.q.count
    user.insert
    var afterCount = User.q.count
    (afterCount - beforeCount) should be(1)
    user.isNew should be(false)

    //update
    val u1 = User.q.where(User.age1 == id).first[User].get
    u1.name = "after update"
    u1.update

    val u2 = User.q.where(User.age1 == id).first[User].get
    u2.name should equal("after update")

    //destroy
    u2.destroy

    val u3 = User.q.where(User.age1 == id).first[User]
    u3.isEmpty should be(true)

    //save
    beforeCount = User.q.count
    id = new Date().getTime.toInt
    val u4 = new User
    u4.age1 = id
    u4.name = "test save"
    u4.save
    afterCount = User.q.count
    (afterCount - beforeCount) should be(1)
    u4.isNew should be(false)

    val u5 = User.q.where(User.age1 == id).first[User].get
    u5.name = "after save"
    beforeCount = User.q.count
    u5.save
    afterCount = User.q.count

    val u6 = User.q.where(User.age1 == id).first[User].get
    u6.name should equal("after save")
    u6.isNew should be(false)
    (afterCount - beforeCount) should be(0)
  }
}