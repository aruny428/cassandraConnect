package models

import com.datastax.driver.core.Row
import utils.CassandraClient

import scala.collection.JavaConverters.setAsJavaSet
import scala.collection.JavaConverters.mapAsJavaMap
import scala.jdk.CollectionConverters.{IterableHasAsScala, MapHasAsScala}


class StudentUtils {
  private val session = CassandraClient.session
  private val keyspace = "sample"
  private val table= "Students"

  private val addQ = session.prepare(s"INSERT INTO $keyspace.$table (id,name,course,subjects,marks) values (?,?,?,?,?)")
  private val deleteQ = session.prepare(s"DELETE FROM $keyspace.$table WHERE id = ?")
  private val updateQ = session.prepare(s"UPDATE $keyspace.$table SET course = ? WHERE id = ?")
  private val getAllQ = session.prepare(s"SELECT * FROM $keyspace.$table")
  private val getWithIdQ = session.prepare((s"SELECT * FROM $keyspace.$table WHERE id = ?"))

  private def toStudents(r: Row) : Student = {
    val preMarks = r.getMap[String,Integer]("marks",classOf[String],classOf[Integer]).asScala

    Student(r.getInt("id"),r.getString("name"),r.getString("course"),
      r.getSet("subjects",classOf[String]).asScala.toSet,preMarks.toMap.asInstanceOf[Map[String,Int]])
  }

  def getAll() ={
    session.execute(getAllQ.bind()).asScala.map(toStudents).toSeq
  }

  def delete(id : Int) ={
    session.execute(deleteQ.bind(id))
  }

  def update(course : String,id :Int)={
    session.execute(updateQ.bind(course,id))
  }

  def add(id:Int,name:String,course:String,subjects: Set[String],marks:Map[String,Int])={
    val javaSet=setAsJavaSet(subjects)
    val javaMap=mapAsJavaMap(marks)
    session.execute(addQ.bind(id,name,course,javaSet,javaMap))
  }

  def getWithId (id:Int)={
    session.execute(getWithIdQ.bind(id)).asScala.map(toStudents).toSeq
  }
}
