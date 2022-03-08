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
import com.example.peodemo.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main_dash_board2.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.Result.Companion.success

class mainDashBoardActivity : AppCompatActivity() {

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dash_board2)
        //assigning this property to context the activity on it
        val window = this.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.barColor)
        //window?.decorView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        dashBoardCourse.setOnClickListener {
            homeFrameLayout.visibility = View.GONE
            progressFrameLayout.visibility = View.GONE
            profileFrameLayout.visibility = View.GONE
            courseFrameLayout.visibility = View.VISIBLE
            dashBoardHome.setImageResource(R.drawable.home)
            dashBoardProgress.setImageResource(R.drawable.progress)
            dashBoardProfile.setImageResource(R.drawable.profile)
            dashBoardCourse.setImageResource(R.drawable.coursepressed)
        }
        dashBoardHome.setOnClickListener {
            courseFrameLayout.visibility = View.GONE
            progressFrameLayout.visibility = View.GONE
            profileFrameLayout.visibility = View.GONE
            homeFrameLayout.visibility = View.VISIBLE
            dashBoardHome.setImageResource(R.drawable.homepressed)
            dashBoardProgress.setImageResource(R.drawable.progress)
            dashBoardProfile.setImageResource(R.drawable.profile)
            dashBoardCourse.setImageResource(R.drawable.course)
        }
        dashBoardProgress.setOnClickListener {
            homeFrameLayout.visibility = View.GONE
            progressFrameLayout.visibility = View.VISIBLE
            profileFrameLayout.visibility = View.GONE
            courseFrameLayout.visibility = View.GONE
            dashBoardHome.setImageResource(R.drawable.home)
            dashBoardProgress.setImageResource(R.drawable.progresspressed)
            dashBoardProfile.setImageResource(R.drawable.profile)
            dashBoardCourse.setImageResource(R.drawable.course)
        }
        dashBoardProfile.setOnClickListener {
            homeFrameLayout.visibility = View.GONE
            progressFrameLayout.visibility = View.GONE
            profileFrameLayout.visibility = View.VISIBLE
            courseFrameLayout.visibility = View.GONE
            dashBoardHome.setImageResource(R.drawable.home)
            dashBoardProgress.setImageResource(R.drawable.progress)
            dashBoardProfile.setImageResource(R.drawable.profilepressed)
            dashBoardCourse.setImageResource(R.drawable.course)
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
                val user = mAuth.currentUser
                userDataByHashMap["email"] = user?.email.toString()
                userDataByHashMap["lastName"] = " "
                userDataByHashMap["name"] = user?.displayName.toString()
                userDataByHashMap["password"] = " "
                userDataByHashMap["ImageProfileURI"] = ref.path
                currentUserDocRef.update(userDataByHashMap)
                Toast.makeText(this@mainDashBoardActivity, "The image is uploaded successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@mainDashBoardActivity, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }


}