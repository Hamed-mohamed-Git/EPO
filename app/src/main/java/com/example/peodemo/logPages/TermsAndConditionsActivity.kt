package com.example.peodemo.logPages

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CoursesModel
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*

class TermsAndConditionsActivity : AppCompatActivity() {
    private var mTermsAndConditionsFragment = TermsAndPoliciesFragment()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)
        //assigning this property to context the activity on it
        val window = this.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.termsAndConditions)
        //addition the Home item to tapList array to access it in adapter class
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setFragment(mTermsAndConditionsFragment)



        termsAndConditionAgreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =  Uri.parse("https://www.termsfeed.com/live/025ebc70-ea3c-43f7-a3e9-1b834c3447eb")
            startActivity(intent)
        }

        PrivacyPolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =  Uri.parse("https://www.privacypolicies.com/live/98fdac54-2371-44cd-8a94-f7e0cacacfbf")
            startActivity(intent)
        }

        accept.setOnClickListener {
            val intent = Intent()
            intent.putExtra("accept",1)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        decline.setOnClickListener {
            val intent = Intent()
            intent.putExtra("accept",2)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }

    //declare a private SetFragment method to control in the fragments when the user press on the items in recycle view
    private fun setFragment(fragment: Fragment) {
        //declare an instance from beginTransaction class to call the method in it and store it in fr property
        val fr = supportFragmentManager.beginTransaction()
        //calling the replace method to trans the fragments
        fr.replace(R.id.termsCoordinate,fragment)
        //and commit it
        fr.commit()

    }
}