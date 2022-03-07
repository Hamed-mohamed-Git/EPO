package com.example.peodemo.DashBoard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.peodemo.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_dash_board2.*

class mainDashBoardActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dash_board2)
        //assigning this property to context the activity on it
        val window = this.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.barColor)
        //window?.decorView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}