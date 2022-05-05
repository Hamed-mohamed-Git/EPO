package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials

data class QuestionsModel (
    val question:String?,
    val Answers:ArrayList<String>?,
    val CorrectAnswer:String?,
    val correctIndex:Int?
    ){
    constructor():this(null,null,null,null)
}