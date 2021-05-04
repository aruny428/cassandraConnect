package controllers

import models.{Student, StudentId, StudentUtils}
import play.api.libs.json._
import play.api.mvc._

import javax.inject._

@Singleton
class StudentController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  implicit val StudentFormat = Json.format[Student]
  implicit val StudentIdFormat = Json.format[StudentId]

      val st = new StudentUtils
      def getSt()=Action{request =>
        val students = st.getAll()
        Ok(Json.toJson(students))
      }

  def delete(id : Int) = Action{
    st.delete(id)
    Ok(s"Deleted student with id : $id")
  }

  def update(course :String,id : Int) = Action{
    st.update(course,id)
    Ok(s"Updated student with id : $id")
  }

  def add() = Action { request =>
    val content = request.body
    val jsonObject = content.asJson

    val studentData =
      jsonObject.flatMap(Json.fromJson[Student](_).asOpt)

    studentData match {
      case Some(newStudent) =>
        st.add(newStudent.id,newStudent.name,newStudent.course,newStudent.subjects,newStudent.marks)
        Created(Json.toJson(newStudent))
      case None =>
        BadRequest
    }
  }

  def getWithId (id :Int) = Action{
    val data=st.getWithId(id)
    Ok(Json.toJson(data))
  }

}

