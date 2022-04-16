package com.example.peodemo.logPages.model

data class User(val email:String,val password:String,val name:String,val lastName:String,val profileImageURI:String, val logWithState:Boolean,var CourseDetails:String){
    constructor():this("","","","","",false,"")
}
