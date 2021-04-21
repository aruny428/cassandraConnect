package controllers

import com.datastax.driver.core.Cluster

import javax.inject._
import scala.collection.mutable.ListBuffer
import play.api.mvc._
import models.Student
import play.api.libs.json._
import utils.CassandraClient

@Singleton
class StudentController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val session = CassandraClient.session
  implicit val StudentFormat = Json.format[Student]

  //Testing my changes
  // testing changes 2nd time
  private val studentList = new ListBuffer[Student]()
  studentList += Student(1, "a", "maths",Set("math","hist"),Map("math"->10,"hist"->20))
  studentList += Student(2, "b", "physics",Set("math","hist"),Map("math"->13,"hist"->22))
  studentList += Student(3, "c", "chem",Set("math","hist"),Map("math"->11,"hist"->50))

  def students() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(studentList)).as("application/json")
  }

  def getStudent(userId: Int) = Action { implicit request: Request[AnyContent] =>

    val foundStudent = studentList.find(_.id == userId)
    foundStudent match {
      case Some(item) => Ok(Json.toJson(item)).as("application/json")
      case None => Ok("Student with this Id doesn't exist.")
    }
  }

  def addStudent() = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson

    val studentData: Option[Student] =
          jsonObject.flatMap(
            Json.fromJson[Student](_).asOpt
          )

    studentData match {
      case Some(newItem) =>
              val toBeAdded = Student(newItem.id,newItem.name,newItem.course,newItem.subjects,newItem.marks)
              studentList += toBeAdded
              Created(Json.toJson(toBeAdded))
      case None =>
        BadRequest
    }
       //Ok("added")

  }

  def updateStudent(userId : Int) = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson

    val foundStudent = studentList.find(_.id == userId)
    val index = studentList.indexOf(foundStudent.get)

    val studentData: Option[Student] =
      jsonObject.flatMap(
        Json.fromJson[Student](_).asOpt
      )

    studentData match {
      case Some(newItem) =>
        val toBeAdded = Student(newItem.id,newItem.name,newItem.course,newItem.subjects,newItem.marks)

        studentList.update(index,toBeAdded)
        Created(Json.toJson(toBeAdded))
      case None =>
        BadRequest
    }
  }

  def removeStudent(userId : Int)= Action {

    val foundStudent = studentList.find(_.id == userId)
    val index = studentList.indexOf(foundStudent.get)
    studentList.remove(index)
    foundStudent match {
      case Some(item) =>
        Ok(Json.toJson(item))
      case None => Ok("Student with this Id doesn't exist.")
    }
  }


    def getValueFromCassandraTable() = Action { request =>
      session.execute("SELECT * FROM arun.students")
      Ok("All Set")
    }

  def ping = Action {
    println(s"Ping API called")
    Ok(<response><status>Success</status><message>pong</message></response>).withHeaders()
  }
}
