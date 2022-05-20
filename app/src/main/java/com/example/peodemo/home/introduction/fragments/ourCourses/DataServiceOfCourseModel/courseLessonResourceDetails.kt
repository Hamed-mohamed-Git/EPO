package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel

import java.io.Serializable

data class courseLessonResourceDetails(
    val finished:Boolean?,
    val uri:String?
): Serializable {
    constructor():this(null,null)
}
