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
import soupy.orm.{Env, Query}

class StackSpec extends FlatSpec with ShouldMatchers {

  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    stack.pop() should equal (2)
    stack.pop() should equal (1)
    val query = new Query().where("name like ?", "%liu")
    println(query.toString)
    println(Env.adapter.toSQL(query))
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new Stack[String]
    evaluating { emptyStack.pop() } should produce [EmptyStackException]
  }
}