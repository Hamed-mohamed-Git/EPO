package com.example.peodemo.welcomeFragments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.peodemo.R
import com.example.peodemo.home.introduction.introductionActivity



class WelcomeActivity : AppCompatActivity() {
    private val EXTRA_LOGOUT = "clearBackstack"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.extras?.getBoolean(EXTRA_LOGOUT) == true) {
            clearBackstack()
        }else{
            setContentView(R.layout.activity_welcome)
        }
        //create a navHostFragment property to can use nav graph by class called nav host
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        //create a navController to control the nav graph by this property in each fragment by findNavController
        val navController = navHostFragment.navController


    }
    private fun clearBackstack() {
        startActivity(Intent(this, introductionActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        })

        finish()
    }


}