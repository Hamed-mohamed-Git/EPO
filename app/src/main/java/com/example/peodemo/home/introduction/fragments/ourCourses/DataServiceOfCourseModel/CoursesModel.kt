package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel

import java.io.Serializable

data class CoursesModel(
    val name:String?,
    val id:String?,
    val image:Int?,
    var percentage:Float?,
    val color:Int?,
    var days:Int?,
    var subscriptionDays:Int?,
    var finished:Boolean?,
    val modulesCount:Int,
    val lessonsCount:Int?,
    val videosCount:Int?,
    val introducitonURL:String?,
    var modulesDetails:ArrayList<courseModulesModel>?
): Serializable{
    constructor():this(null,null,null,null,null,null,null,null,0,null,null,null,null)
}