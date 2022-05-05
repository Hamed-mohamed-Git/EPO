package com.example.peodemo.DashBoard.categoriesTabs.qoutes

data class quoteModel(
    val name:String,
    var Quotes:ArrayList<String>?
){
    constructor():this("",null)
}
