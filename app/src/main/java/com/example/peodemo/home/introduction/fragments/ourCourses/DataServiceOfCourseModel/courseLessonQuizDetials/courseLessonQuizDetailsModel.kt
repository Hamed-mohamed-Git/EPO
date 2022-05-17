package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials

import java.io.Serializable

data class courseLessonQuizDetailsModel(
    var name:String?,
    var questions:ArrayList<QuestionsModel>?,
    var finished:Boolean?
): Serializable{
    constructor():this(null,null,null)
}
