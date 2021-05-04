package models

case class Student(
   id: Int,
   name : String ,
   course : String,
   subjects: Set[String],
   marks : Map[String,Int]
)

case class StudentId(id :Int)






