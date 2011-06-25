package view

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import soupy.mapper.{Table, Model, TableDef}
import config.Setting
import reflect.BeanInfo
import java.util.Date
import soupy.view.TableFor

//users
trait UserDef extends TableDef {
  var name = property[String]("name", Some("姓名"))
  var age1 = property[Int]("age")
  var createdAt = property[Date]("created_at")
}

@BeanInfo
class User extends Model with UserDef {
}


object `package` {

  @BeanInfo implicit object User extends Table[User]("users") with UserDef

}

class TableForSpec extends Spec with ShouldMatchers {
  it("insert correctly") {
    implicit val repository = Setting.repository

    val user = new User
    user.age1 = 33
    val beforeCount = User.q.count
    user.insert
    val afterCount = User.q.count
    (afterCount - beforeCount) should be(1)

    val users = User.q.limit(5).all[User]
    object UserTable extends TableFor(users){
      column(User.name)
      column("年龄", u => u.age1)
      column("操作", u => <div><a href="#">OK</a></div>)

      data(User.name)
    }

    println(UserTable.toHTML.toString)
    UserTable.toHTML.toString.indexOf("年龄") != -1 should be(true)
    UserTable.toHTML.toString.indexOf("姓名") != -1 should be(true)


  }
}