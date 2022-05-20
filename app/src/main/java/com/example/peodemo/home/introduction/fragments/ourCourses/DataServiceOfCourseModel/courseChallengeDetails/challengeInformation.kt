package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseChallengeDetails

import java.io.Serializable

data class challengeInformation(
    val paragraphs : ArrayList<String>?,
    val uriVideo:String?,
    val pdfUri:String?
): Serializable {
    constructor() : this(null,null,null)
}
