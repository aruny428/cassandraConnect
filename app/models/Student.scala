package models

//import play.api.libs.json.{Format, Json}


case class Student(
   id: Int,
   name : String ,
   course : String,
   subjects: Set[String],
   marks : Map[String,Int]
)

