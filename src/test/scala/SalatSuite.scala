import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import org.scalatest.{FunSuite, BeforeAndAfterEach}

// Note Salat wants this case class definition to be outside of any other class defs
// When I had this under the SalatSuite definition I saw an error running the "simple serialization test"
case class Alpha(x: String)

case class Basset(name:String, buddy:String)

class SalatSuite extends FunSuite with BeforeAndAfterEach {

  val hostname = "127.0.0.1"
  val port = 27017
  val dbName = "scala_mongo_example"
  val collName = "bassets"
  var mongoConn: MongoConnection = null
  var mongoDB: MongoDB = null
  var mongoColl: MongoCollection = null

  override def beforeEach() {
    mongoConn = MongoConnection(hostname, port)
    mongoConn.dropDatabase(dbName)
    mongoDB = mongoConn(dbName)
    mongoColl = mongoDB(collName)
    mongoConn.setWriteConcern(WriteConcern.Safe)
  }

  override def afterEach() {
    mongoConn.dropDatabase(dbName)
    mongoConn.close()
  }

  test("Simple serialization") {
    val a = Alpha(x = "Hello World")
    val dbo = grater[Alpha].asDBObject(a)
    val a_* = grater[Alpha].asObject(dbo)
    assert(a === a_*)
  }

  test("Simple Save to MongoDB") {
    assert(mongoColl.find().count === 0)
    val fred = Basset("Fred", "Coby")
    mongoColl += grater[Basset].asDBObject(fred)
    assert(mongoColl.find().count === 1)

    val found = mongoColl.findOne(MongoDBObject("name"->fred.name))
    println(found)
    assert(found != None)

    // found is an Option, so we need to extract out it's value
    val fred_* = found.map(grater[Basset].asObject(_)).getOrElse(None)
    assert(fred === fred_*)
  }


}
