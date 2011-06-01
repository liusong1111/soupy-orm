import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import soupy.orm.adapters.MysqlAdapter
import soupy.orm.repositories.DefaultRepository

class RepositorySpec extends Spec with ShouldMatchers {
  describe("repository") {
    describe("DefaultRepository") {
      it("should work correctly") {
        val repository = new DefaultRepository(MysqlAdapter, Map("database" -> "abc"))
        repository.within{conn =>
          val stat = conn.createStatement
          val rs = stat.executeQuery("select 1 from users")
          rs.next
          val u = rs.getInt(1)
          u should equal(1)
          rs.close
          stat.close
        }
      }
    }

  }
}