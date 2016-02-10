package dal

import scala.collection.immutable.IndexedSeq

import org.mongodb.scala._
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model._

import dal.Helpers._
import models._

object UserDao {
	def create(userModel: User) {
		var collection = getUserCollection()
		var doc:Document = Document("name" -> userModel.name, "email" -> userModel.email)
		collection.insertOne(doc).results()
	}
	
	def list():Seq[User] =  {
		var collection = getUserCollection()
		var documents: Seq[Document]  = collection.find().results()
		var users: Seq[User] = documents.map((doc: Document) => {User(doc.toBsonDocument.getString("name").getValue(),
			doc.toBsonDocument.getString("email").getValue)})
		return users
	}
	
	def getUserCollection():MongoCollection[Document] = {
		var mongoClient: MongoClient = MongoClient()
		var db = mongoClient.getDatabase("testDbTwo")
		var collection =  db.getCollection("UserCollection")
		return collection
	}
}