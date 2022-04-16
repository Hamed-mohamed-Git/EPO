package com.example.peodemo.DashBoard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.example.peodemo.DashBoard.fragmentPages.courses.dashBoardCourseFragment
import com.example.peodemo.DashBoard.fragmentPages.dashBoardProfileFragment
import com.example.peodemo.DashBoard.fragmentPages.dashBoardProgressFragment
import com.example.peodemo.DashBoard.fragmentPages.dashboardHomeFragment
import com.example.peodemo.DashBoard.glide.GlideApp
import com.example.peodemo.R
import com.example.peodemo.home.introduction.introductionActivity
import com.example.peodemo.logPages.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main_dash_board2.*
import java.io.ByteArrayOutputStream
import java.util.*

class mainDashBoardActivity : AppCompatActivity() {
    private var DataFromOurCourses = ""

    companion object{
        val RC_S_IMAGE = 2
    }
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val fireStoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val currentUserDocRef: DocumentReference
        get() = fireStoreInstance.document("Users/${mAuth.currentUser?.uid.toString()}")

    private val mStorage:FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }


    private val currentUserStorageRef:StorageReference
    get() = mStorage.reference.child(mAuth.currentUser?.uid.toString())


    private  var mHomeFragment = dashboardHomeFragment()
    private  var mCoursesFragment = dashBoardCourseFragment()
    private  var mProgressFragment =  dashBoardProgressFragment()
    private  var mProfileFragment = dashBoardProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dash_board2)


        //assigning this property to context the activity on it
        val window = this.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.barColor)
        //window?.decorView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        DataFromOurCourses = intent.getStringExtra("anotherCourse") as String




        getUserInformation { User ->
            val userDataByHashMap = mutableMapOf<String, Any>()
            val user = mAuth.currentUser
            if (DataFromOurCourses != ""){
                userDataByHashMap["logWithState"] = false
                userDataByHashMap["profileImageURI"] = User.profileImageURI
                userDataByHashMap["email"] = user?.email.toString()
                userDataByHashMap["lastName"] = " "
                userDataByHashMap["name"] = user?.displayName.toString()
                userDataByHashMap["password"] = " "
                userDataByHashMap["CourseDetails"] = DataFromOurCourses
                currentUserDocRef.update(userDataByHashMap)
                setCourseInformationOnTheServer("Foundation",3,5)
            }
            //setCourseInformationOnTheServer("Foundation",3,5)
            DataFromOurCourses = User.CourseDetails
            if (User.profileImageURI.isNotEmpty() && User.logWithState){
                GlideApp.with(this@mainDashBoardActivity)
                    .load(User.profileImageURI)
                    .placeholder(R.drawable.user)
                    .into(profileCircleImageView)
            }
            else if (User.profileImageURI.isNotEmpty() && !User.logWithState){
                GlideApp.with(this@mainDashBoardActivity)
                    .load(mStorage.getReference(User.profileImageURI))
                    .placeholder(R.drawable.user)
                    .into(profileCircleImageView)
            }
            if (User.lastName.isNotEmpty()){
                dashBoardUserLastName.visibility = View.VISIBLE
                dashBoardUserLastName.text = User.lastName
                dashBoardUserName.text = User.name
            }else{
                dashBoardUserLastName.visibility = View.GONE
                dashBoardUserName.text = User.name
            }

        }
        signOutButton.setOnClickListener {
            mAuth.signOut()
            onBoardingSignOutFinished()
            val intent = Intent(this,introductionActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        setFragment(mHomeFragment)
        dashBoardCourse.setOnClickListener {
            homeFrameLayout.visibility = View.GONE
            progressFrameLayout.visibility = View.GONE
            profileFrameLayout.visibility = View.GONE
            courseFrameLayout.visibility = View.VISIBLE

            setFragment(mCoursesFragment)

            dashBoardHome.setImageResource(R.drawable.dashboard)
            dashBoardProgress.setImageResource(R.drawable.progress)
            dashBoardProfile.setImageResource(R.drawable.profile)
            dashBoardCourse.setImageResource(R.drawable.coursepressed)

            profileCircleImageView.visibility = View.GONE
            helloDashboard.visibility = View.GONE
            dashBoardUserName.visibility = View.GONE
            dashBoardUserLastName.visibility = View.GONE
            frameLayoutSignUp.visibility = View.GONE
            dashBoardActionBar.animate().scaleY(-0.4f).duration = 1500


            dashboardItemDetails.visibility = View.VISIBLE
            dashboardTabsText.text = "Courses Details"



        }
        dashBoardHome.setOnClickListener {
            courseFrameLayout.visibility = View.GONE
            progressFrameLayout.visibility = View.GONE
            profileFrameLayout.visibility = View.GONE
            homeFrameLayout.visibility = View.VISIBLE

            setFragment(mHomeFragment)
            dashBoardHome.setImageResource(R.drawable.dashboardpressed)
            dashBoardProgress.setImageResource(R.drawable.progress)
            dashBoardProfile.setImageResource(R.drawable.profile)
            dashBoardCourse.setImageResource(R.drawable.course)

            profileCircleImageView.visibility = View.VISIBLE
            frameLayoutSignUp.visibility = View.VISIBLE
            helloDashboard.visibility = View.VISIBLE
            dashBoardUserName.visibility = View.VISIBLE

            dashBoardActionBar.animate().scaleY(0.9f).duration = 2000

            dashboardItemDetails.visibility = View.GONE

        }
        dashBoardProgress.setOnClickListener {
            homeFrameLayout.visibility = View.GONE
            progressFrameLayout.visibility = View.VISIBLE
            profileFrameLayout.visibility = View.GONE
            courseFrameLayout.visibility = View.GONE
            setFragment(mProgressFragment)

            dashBoardHome.setImageResource(R.drawable.dashboard)
            dashBoardProgress.setImageResource(R.drawable.progresspressed)
            dashBoardProfile.setImageResource(R.drawable.profile)
            dashBoardCourse.setImageResource(R.drawable.course)

            profileCircleImageView.visibility = View.GONE
            frameLayoutSignUp.visibility = View.GONE
            helloDashboard.visibility = View.GONE
            dashBoardUserName.visibility = View.GONE
            dashBoardUserLastName.visibility = View.GONE

            dashBoardActionBar.animate().scaleY(-0.4f).duration = 1500


            dashboardItemDetails.visibility = View.VISIBLE
            dashboardTabsText.text = "Progress Details"
        }
        dashBoardProfile.setOnClickListener {
            homeFrameLayout.visibility = View.GONE
            progressFrameLayout.visibility = View.GONE
            profileFrameLayout.visibility = View.VISIBLE
            courseFrameLayout.visibility = View.GONE

            setFragment(mProfileFragment)

            dashBoardHome.setImageResource(R.drawable.dashboard)
            dashBoardProgress.setImageResource(R.drawable.progress)
            dashBoardProfile.setImageResource(R.drawable.profilepressed)
            dashBoardCourse.setImageResource(R.drawable.course)

            profileCircleImageView.visibility = View.GONE
            helloDashboard.visibility = View.GONE
            dashBoardUserName.visibility = View.GONE
            frameLayoutSignUp.visibility = View.GONE
            dashBoardUserLastName.visibility = View.GONE

            dashBoardActionBar.animate().scaleY(-0.4f).duration = 1500

            dashboardItemDetails.visibility = View.VISIBLE
            dashboardTabsText.text = "Profile Details"
        }
        framebackLayout.setOnClickListener {
            setFragment(mHomeFragment)

            dashBoardActionBar.animate().scaleY(0.9f).duration = 2000

            courseFrameLayout.visibility = View.GONE
            progressFrameLayout.visibility = View.GONE
            profileFrameLayout.visibility = View.GONE
            homeFrameLayout.visibility = View.VISIBLE


            dashBoardHome.setImageResource(R.drawable.dashboardpressed)
            dashBoardProgress.setImageResource(R.drawable.progress)
            dashBoardProfile.setImageResource(R.drawable.profile)
            dashBoardCourse.setImageResource(R.drawable.course)

            profileCircleImageView.visibility = View.VISIBLE
            frameLayoutSignUp.visibility = View.VISIBLE
            helloDashboard.visibility = View.VISIBLE
            dashBoardUserName.visibility = View.VISIBLE
            dashboardItemDetails.visibility = View.GONE
        }




        profileCircleImageView.setOnClickListener {
            val getImageFromGallery = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg","image/png"))
            }

            startActivityForResult(Intent.createChooser(getImageFromGallery,"select the best image you have"), RC_S_IMAGE)
        }




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_S_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            profileCircleImageView.setImageURI(data.data)

            val imageSelectedURI = data.data
            val imageSelectedBmp = MediaStore.Images.Media.getBitmap(this.contentResolver,imageSelectedURI)
            val outputStream = ByteArrayOutputStream()
            imageSelectedBmp.compress(Bitmap.CompressFormat.JPEG,100,outputStream)
            val imageSelectedByte = outputStream.toByteArray()
            uploadProfileImage(imageSelectedByte)
        }
    }

    private fun uploadProfileImage(imageSelectedByte: ByteArray) {
        val ref = currentUserStorageRef.child("ProfileImage/${UUID.nameUUIDFromBytes(imageSelectedByte)}")
        ref.putBytes(imageSelectedByte).addOnCompleteListener {
            if (it.isSuccessful){
                val userDataByHashMap = mutableMapOf<String, Any>()
                getUserInformation {
                    val user = mAuth.currentUser
                    userDataByHashMap["logWithState"] = false
                    userDataByHashMap["profileImageURI"] = ref.path
                    userDataByHashMap["email"] = user?.email.toString()
                    userDataByHashMap["lastName"] = it.lastName
                    userDataByHashMap["name"] = user?.displayName.toString()
                    userDataByHashMap["password"] = it.password
                    userDataByHashMap["CourseDetails"] = it.CourseDetails
                    currentUserDocRef.update(userDataByHashMap)
                }
                Toast.makeText(this@mainDashBoardActivity, "The image is uploaded successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@mainDashBoardActivity, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getUserInformation(onComplete:(User) -> Unit){
        currentUserDocRef.get().addOnSuccessListener {
            onComplete(it.toObject(User::class.java)!!)
        }


    }

    //create a method to return the splash fragment a boolean value if the user click on nextButton send to splash fragment true
    //if true the app does not launch the welcome fragments
    private fun onBoardingSignOutFinished() {
        //assigning a sharedPref property to create a key and get a context of this fragment
        val sharedPref = this.getSharedPreferences("onSignUpBoarding", Context.MODE_PRIVATE)
        //assigning a editor property to create set a key and boolean value to use in splash fragment
        val editor = sharedPref.edit()
        //set those values
        editor.putBoolean("signProcessFinished", false)
        //apply it if the method called
        editor.apply()

    }

    //declare a private SetFragment method to control in the fragments when the user press on the items in recycle view
    private fun setFragment(fragment: Fragment) {
        //declare an instance from beginTransaction class to call the method in it and store it in fr property
        val fr = supportFragmentManager.beginTransaction()
        //calling the replace method to trans the fragments
        fr.setCustomAnimations(R.anim.side_in_left_dashboard_fragments_transaction,R.anim.side_out_right_to_dashboard_fragments_transaction)
        fr.replace(R.id.dashboardCoordinateLayout,fragment)
        //and commit it
        fr.commit()

    }

    private fun setCourseInformationOnTheServer(CourseName:String,Modules:Int,lessons:Int){
        for (modulesAmount  in 1..Modules){
            for (LessonAmount in 1..lessons){
                fireStoreInstance.collection("Users")
                    .document(mAuth.currentUser!!.uid)
                    .collection("My Courses")
                    .document(CourseName)
                    .collection("Module $modulesAmount")
                    .document("lesson $LessonAmount")
                    .set(mapOf("finished" to false))
            }
        }
    }
}