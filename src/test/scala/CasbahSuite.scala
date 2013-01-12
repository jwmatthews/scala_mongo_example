import com.mongodb.casbah.Imports._
import org.scalatest.{FunSuite, BeforeAndAfterEach}

class CasbahSuite extends FunSuite with BeforeAndAfterEach {
  val hostname = "127.0.0.1"
  val port = 27017
  val dbName = "scala_mongo_example"
  val collName = "widgets"
  var mongoConn: MongoConnection = null
  var mongoDB: MongoDB = null
  var mongoColl: MongoCollection = null

  override def beforeEach() {
    mongoConn = MongoConnection(hostname, port)
    mongoConn.dropDatabase(dbName)
    mongoDB = mongoConn(dbName)
    mongoColl = mongoDB(collName)
    mongoConn.setWriteConcern(WriteConcern.FsyncSafe)
  }

  override def afterEach() {
    mongoConn.dropDatabase(dbName)
    mongoConn.close()
  }

  test("unique index prevents duplicates") {
    // Add the unique index to the collection
    val index = MongoDBObject("id" -> 1)
    val options = MongoDBObject("unique" -> true)
    mongoColl.ensureIndex(index, options)

    val builder1 = MongoDBObject.newBuilder
    builder1 += "id" -> 1
    builder1 += "name" -> "item1"
    val item1 = builder1.result().asDBObject
    mongoColl += item1
    assert(mongoColl.find().count === 1)

    val builder2 = MongoDBObject.newBuilder
    builder2 += "id" -> 2
    builder2 += "name" -> "item2"
    val item2 = builder2.result().asDBObject
    mongoColl += item2
    assert(mongoColl.find().count === 2)

    // This should be a duplicate since it shares same "id" as 'item2'
    val item3 = MongoDBObject("id" -> 2, "name" -> "item2a")

    // Safe version throws an exception
    intercept[MongoException]{
      mongoColl += item3
    }
    assert(mongoColl.find().count === 2)

    // Default save won't save the object, yet no exception is thrown
    mongoConn.setWriteConcern(WriteConcern.None)
    mongoColl += item3
    assert(mongoColl.find().count === 2)
    mongoConn.setWriteConcern(WriteConcern.FsyncSafe)

    // Verify that the object with "id"->2 matches item2

    // Approach #1 using a map on the Option
    val found = mongoColl.findOne(MongoDBObject("id"->2))
    assert(found != None)
    found.map( item => assert(item.getAs[String]("name") === item2.getAs[String]("name")))

    // Approach #2 using a pattern match
    mongoColl.findOne(MongoDBObject("id"->2)) match {
      case None => assert(false == true)
      case Some(item) => assert(item.getAs[String]("name") === item2.getAs[String]("name"))
    }
  }

  test("simple write then read of an object") {
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
