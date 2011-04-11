//import org.scalatest.matchers.ShouldMatchers
//import org.scalatest.{Spec, FlatSpec}
//import soupy.orm.Query
//
//class OrmSpec  extends Spec with StackFixtureCreationMethods with StackBehaviors  {
//  "orm" should "work" in{
//    new Query().where("name like ?", "%liu")
//  }
//}

import java.util.{EmptyStackException, Stack}
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import reflect.BeanInfo
import soupy.orm.{Repository, Env, Query}

@BeanInfo
class User {
  var name: String = ""
  var age: Int = 0
}

class StackSpec extends FlatSpec with ShouldMatchers {
  "Query" should "toSQL correctly" in {
    val query = new Query().from("users").where("name like ?", "%liu")
    println(query.toString)
    println(Env.adapter.toSQL(query))

    Env.adapter.toSQL(query) should equal("select *\nfrom users\nwhere name like ?")
  }

  "Repository" should "executeQuery correctly" in {
    val query = new Query().from("users").where("name like '%liu'")
    println(query.toString)
    println(Env.adapter.toSQL(query))
    Env.adapter.toSQL(query) should equal("select *\nfrom users\nwhere name like '%liu'")

    Repository.executeQuery("select *\nfrom users\nwhere name like '%liu%'") {
      rs =>
        val u = Env.mapper.map[User](rs).get
        println("-------")
        println(u.name)
        println(u.age)
    }
  }


  //  it should "throw NoSuchElementException if an empty stack is popped" in {
  //    val emptyStack = new Stack[String]
  //    evaluating { emptyStack.pop() } should produce [EmptyStackException]
  //  }
}