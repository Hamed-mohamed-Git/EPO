package com.example.peodemo.DashBoard.categoriesTabs.services

import com.example.peodemo.BuildConfig
import com.example.peodemo.DashBoard.categoriesTabs.models.Photo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

public interface ApiInterface {

//?collections=islamic-school&orientation=portrait&client_id=${BuildConfig.ACCESS_KEY}&count=30
    @GET("photos/random")
    fun getRecentPhotos(
    @Query("collections") collections: String,
    @Query("topics") topics: String,
    @Query("orientation") orientation: String,
    @Query("count") count: Int,
    ) : Call<MutableList<Photo>>

}