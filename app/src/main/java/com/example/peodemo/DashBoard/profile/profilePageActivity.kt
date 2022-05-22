package com.example.peodemo.DashBoard.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.peodemo.DashBoard.glide.GlideApp
import com.example.peodemo.DashBoard.mainDashBoardActivity
import com.example.peodemo.R
import com.example.peodemo.home.introduction.introductionActivity
import com.example.peodemo.logPages.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main_dash_board2.*
import kotlinx.android.synthetic.main.activity_profile_page.*
import kotlinx.android.synthetic.main.login_details_alert_dialog.view.*
import kotlinx.android.synthetic.main.quiz_alert_dialog.view.*
import java.io.ByteArrayOutputStream
import java.util.*

class profilePageActivity : AppCompatActivity() {

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

    private val mStorage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    private val currentUserStorageRef: StorageReference
        get() = mStorage.reference.child(mAuth.currentUser?.uid.toString())



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        val window = this.window
        window?.statusBarColor = this.resources.getColor(R.color.profilePageStatusBar)
        window?.navigationBarColor = this.resources.getColor(R.color.white)
        window?.decorView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        loginDetailLayout.setOnClickListener {
            getUserInformation {
                logDetailAlertDialog(it.name,it.email,it.password)
            }
        }

        helpLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:01558653011")
            startActivity(intent)
            this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
        }
        legalInformationLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =  Uri.parse("https://www.termsfeed.com/live/025ebc70-ea3c-43f7-a3e9-1b834c3447eb")
            startActivity(intent)
            this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
        }
        FaceDetailsLayout.setOnClickListener {
            Toast.makeText(this,"this service is not available now",Toast.LENGTH_LONG).show()
        }

        profileLogout_button.setOnClickListener {
            finish()
        }

        getUserInformation { User ->
            if (User.profileImageURI.isNotEmpty() && User.logWithState){

                GlideApp.with(this@profilePageActivity)
                    .load(User.profileImageURI)
                    .placeholder(R.drawable.user).into(profileImage)

            }
            else if (User.profileImageURI.isNotEmpty() && !User.logWithState){
                GlideApp.with(this@profilePageActivity)
                    .load(mStorage.getReference(User.profileImageURI))
                    .placeholder(R.drawable.user)
                    .into(profileImage)
            }
            userName.text = User.name
        }

        addPhoto_button.setOnClickListener {
            val getImageFromGallery = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg","image/png"))
            }
            startActivityForResult(Intent.createChooser(getImageFromGallery,"select the best image you have"), RC_S_IMAGE)
        }

        logOut.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, introductionActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            onBoardingSignOutFinished()
        }

    }

    private fun getUserInformation(onComplete:(User) -> Unit){
        currentUserDocRef.get().addOnSuccessListener {
            onComplete(it.toObject(User::class.java)!!)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_S_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            profileImage.setImageURI(data.data)

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
                    userDataByHashMap["name"] = it.name
                    userDataByHashMap["password"] = it.password
                    userDataByHashMap["courseDetails"] = ""
                    userDataByHashMap["courseCheckedOut"] = it.courseCheckedOut
                    currentUserDocRef.update(userDataByHashMap)
                }
                Toast.makeText(this@profilePageActivity, "The image is uploaded successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@profilePageActivity, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
            }
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

    private fun logDetailAlertDialog(userName:String,email:String,password:String){
        val builder = AlertDialog.Builder(this)
        // Get the layout inflater
        val inflater = this.layoutInflater
        val inflation = inflater.inflate(R.layout.login_details_alert_dialog, null)

        inflation.userNameDialog.text = userName
        inflation.emailDialog.text = email
        inflation.passwordDialog.text = password


        builder.setView(inflation)
        // Add action buttons
        val dialog = builder.create()

        inflation.dialogBackButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window!!.setBackgroundDrawableResource(R.drawable.profile_dialog_background)
        dialog.show()
    }

}