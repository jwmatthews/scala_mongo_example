import com.mongodb.casbah.Imports._
import org.scalatest.{FunSuite, BeforeAndAfterEach}


class CasbahSuite extends FunSuite with BeforeAndAfterEach {
  val hostname = "127.0.0.1"
  val port = 27017
  val dbName = "scala_mongo_example"
  val collName = "widgets"
  val mongoConn = MongoConnection(hostname, port)
  val mongoDB = mongoConn(dbName)

  override def beforeEach() {
    mongoDB(collName).dropCollection()
  }

  override def afterEach() {
    mongoDB(collName).dropCollection()
  }

  test("simple write then read of an object") {
    val mongoColl = mongoDB(collName)
    assert(mongoColl.find().count === 0)
    val newObj = MongoDBObject(
      "foo" -> "bar",
      "x" -> "y",
      "pie" -> 3.14,
      "spam" -> "eggs")
    // Save the object to mongo
    mongoColl += newObj
    assert(mongoColl.find().count === 1)
  }
}
