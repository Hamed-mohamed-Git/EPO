package com.example.peodemo.logPages.model

data class User(val Email:String,val Password:String,val name:String,val lastName:String,val profileImageURI:String, val logWithState:Boolean){
    constructor():this("","","","","",false)
}
