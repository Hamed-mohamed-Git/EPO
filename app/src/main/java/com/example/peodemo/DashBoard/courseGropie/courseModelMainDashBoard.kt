package com.example.peodemo.DashBoard.courseGropie

import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseModulesModel

data class courseModelMainDashBoard(
    val name:String,
    val id:String,
    val image:Int,
    var percentage:Float,
    val color:Int,
    var days:Int,
    var subscriptionDays:Int,
    var finished:Boolean,
    val modulesCount:Int,
    val modulesDetails:ArrayList<courseModulesModel>?
){
    constructor():this("","",0 ,0f,0,0,0,false,0,null)
}
