package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel

import java.io.Serializable

data class courseLessonDetailsModel (
    val name:String?,
    val uri:String,
    var finished:Boolean?,
    var description:ArrayList<String>?,
    val resources:ArrayList<String>?
    ): Serializable{
        constructor():this("","",null,null,null)
    }