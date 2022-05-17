package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials

import java.io.Serializable

data class QuestionsModel (
    val question:String?,
    val answers:ArrayList<String>?,
    val correctAnswer:String?,
    val correctIndex:Int?
    ): Serializable {
    constructor():this(null,null,null,null)
}