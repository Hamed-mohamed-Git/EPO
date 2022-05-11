package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel

import java.io.Serializable

data class CoursesModel(
    val name:String?,
    val id:String?,
    val image:Int?,
    var percentage:Float?,
    val Color:Int?,
    var Days:Int?,
    var SubscriptionDays:Int?,
    var Finished:Boolean?,
    val modulesCount:Int,
    val lessonsCount:Int?,
    val videosCount:Int?,
    val introducitonURL:String?,
    var ModulesDetails:ArrayList<courseModulesModel>?
): Serializable{
    constructor():this(null,null,null,null,null,null,null,null,0,null,null,null,null)
}