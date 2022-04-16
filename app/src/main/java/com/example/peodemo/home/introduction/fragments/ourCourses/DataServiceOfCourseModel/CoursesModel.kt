package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel

import java.io.Serializable

data class CoursesModel(
    val name:String,
    val id:String?,
    val image:Int,
    var percentage:Float,
    val Color:Int,
    var Days:Int,
    var SubscriptionDays:String,
    var Finished:Boolean,
    val ModulesCount:Int,
    val ModulesDetails:ArrayList<courseModulesModel>,
    var description:ArrayList<String>
): Serializable