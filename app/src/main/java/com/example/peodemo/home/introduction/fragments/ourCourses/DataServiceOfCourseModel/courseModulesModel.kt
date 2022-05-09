package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel

import java.io.Serializable

class courseModulesModel(
    val id:String?,
    val name:String?,
    val image:Int?,
    val lessonsCount:Int?,
    val videosCount:Int?,
    val tasksCount:Int?,
    val lessonDetails:ArrayList<CourseLessonsModel>?,
    var finishedLessons:Int?,
    var Finished:Boolean?,
    var description:ArrayList<String>?
): Serializable{
    constructor():this(null,null,null,null,null,null,null,null,null,null)
}