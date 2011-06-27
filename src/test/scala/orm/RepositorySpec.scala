package orm

import java.sql.ResultSet
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import soupy.orm.adapters.MysqlAdapter
import soupy.orm.repositories.{DefaultConnectionProvider, DefaultRepository}

class RepositorySpec extends Spec with ShouldMatchers {
  describe("repository") {
    describe("DefaultRepository") {
      val repository = new DefaultRepository(MysqlAdapter, DefaultConnectionProvider(Map("database" -> "abc")))

      it("withinConnection should work correctly") {
        repository.connectionProvider.withinConnection {
          conn =>
            val stat = conn.createStatement
            val rs = stat.executeQuery("select 1 from users")
            rs.next
            val u = rs.getInt(1)
            u should equal(1)
            rs.close
            stat.close
        }
      }

      it("executeUpdate and executeQuery should both work correctly") {
        val insertedCount = repository.executeUpdate("insert into users(name, age) values('liusong', 31)")
        insertedCount should equal(1)
        repository.connectionProvider.executeQuery("select * from users where name = 'liusong'") {
          rs: ResultSet =>
            val name = rs.getString("name")
            name should equal("liusong")
        }
      }
    }

  }
}