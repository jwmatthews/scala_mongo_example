import com.mongodb.casbah.Imports._

object Main {
  def simpleWriteRead(mongoColl: MongoCollection) {
    val newObj = MongoDBObject(
      "foo" -> "bar",
      "x" -> "y",
      "pie" -> 3.14,
      "spam" -> "eggs")
    // Save the object to mongo
    println("Write " + newObj + " to Mongo")
    mongoColl += newObj
    val cursor = mongoColl.find()
    println("Found " + cursor.count + " items from collection " + mongoColl.getFullName)
  }

  def main(args: Array[String]) {
    val hostname = "127.0.0.1"
    val port = 27017
    val dbName = "scala_mongo_example"
    val collName = "widgets"
    val mongoConn = MongoConnection(hostname, port)
    val mongoDb = mongoConn(dbName)
    // Ensure we are starting with a fresh collection each time
    mongoDb(collName).dropCollection()
    val mongoColl = mongoDb(collName)
    simpleWriteRead(mongoColl)
    println("Scala mongo example complete")
  }
}
