package com.example.peodemo.logPages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.peodemo.R

class RegisterActivitypage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_activitypage)
        //create a navHostFragment property to can use nav graph by class called nav host
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.regiser_pages_nav_grahp) as NavHostFragment
        //create a navController to control the nav graph by this property in each fragment by findNavController
        val navController = navHostFragment.navController
    }
}