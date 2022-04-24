package com.example.peodemo.logPages

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.peodemo.DashBoard.mainDashBoardActivity
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CoursesModel
import com.example.peodemo.logPages.model.CourseDetails
import com.example.peodemo.logPages.model.User
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_sign_in.*
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayList


class signInActivity : AppCompatActivity(), TextWatcher {
    private var DataFromOurCourses = ""
    private lateinit var courseDetails: CoursesModel
    companion object{
        private const val RC_SIGN_IN = 120
    }
    private val mAuth:FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private lateinit var googleSignInClient:GoogleSignInClient

    private lateinit var loginBtn: ImageView
    private lateinit var githubEdit: EditText
    private val provider = OAuthProvider.newBuilder("github.com")

    private var callBackManger:CallbackManager? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        textView14.setOnClickListener {
            val intent = Intent(this,signUpActivity::class.java)
            startActivity(intent)
        }
        window.navigationBarColor = getColor(R.color.theSubIOSDesignCourse)
        DataFromOurCourses = intent.getStringExtra("CourseID") as String
        if (DataFromOurCourses != ""){
            courseDetails = intent.getSerializableExtra("DataOfCourse") as CoursesModel
        }



        editTextTextEmailAddress.addTextChangedListener(this@signInActivity)
        editTextTextPassword.addTextChangedListener(this@signInActivity)
        

        githubEdit = editTextTextEmailAddress
        loginBtn = signInWithGithub

        provider.addCustomParameter("login", githubEdit.text.toString())

        val scopes: ArrayList<String?> = object : ArrayList<String?>() {
            init {
                add("user:email")
            }
        }
        provider.scopes = scopes

        // call signInWithGithubProvider() method
        // after clicking login Button
        loginBtn.setOnClickListener {
                signInWithGithubProvider()
        }





        FacebookSdk.sdkInitialize(applicationContext)
        callBackManger = CallbackManager.Factory.create()
        signWithFacebook.setReadPermissions("email", "public_profile")
        signWithFacebook.registerCallback(callBackManger, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d("mainDashBoardActivity", "facebook:onSuccess:$result")
                handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {
                Log.d("mainDashBoardActivity", "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d("mainDashBoardActivity", "facebook:onError", error)
            }
        })


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        signInWithGoogle.setOnClickListener {
            signIn()
        }


        signInButton.setOnClickListener {
            //assigning a email property to store to the date that be in edit Text
            val email = editTextTextEmailAddress.text.trim()
            //assigning a password property to store to the date that be in edit Text
            val password = editTextTextPassword.text.trim()

            if (password.length < 6){
                editTextTextPassword.error = "Please Enter 6 character"
                editTextTextPassword.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()){
                editTextTextEmailAddress.error = "Please Enter Your Email"
                editTextTextEmailAddress.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editTextTextEmailAddress.error = "This Email is wrong"
                editTextTextEmailAddress.requestFocus()
                return@setOnClickListener
            }
            signInGif.visibility = View.VISIBLE
            signIn(email.toString(),password.toString())
        }

        //assigning a sign in property to use the view2 to make animations
        val signInView =this.findViewById<View>(R.id.view2)
        val signInProfileImage = this.findViewById<ImageView>(R.id.signInProfile)
        //move the view from down to top by 900 degrees at 6 minutes
        signInView.animate().translationY(-900f).duration = 5000
        signInProfileImage.animate().translationY(262f).duration = 5000
        //assigning this property to context the activity on it
        val window = this.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.signInActivityColor)
        //window?.decorView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    private fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
            if (task.isSuccessful){
                onBoardingFinished()
                var intent = Intent(this@signInActivity,mainDashBoardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                if (DataFromOurCourses != ""){
                    intent.putExtra("anotherCourse",DataFromOurCourses)
                    intent.putExtra("DataOfCourse",courseDetails)
                }
                else{
                    intent.putExtra("anotherCourse","")

                }
                startActivity(intent)
            } else {
                Toast.makeText(this@signInActivity,task.exception?.message,Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callBackManger?.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("mainDashBoardActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("mainDashBoardActivity", "Google sign in failed", e)
                }
            }else{
                Log.w("mainDashBoardActivity", exception?.message.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("mainDashBoardActivity", "signInWithCredential:success")
                    onBoardingFinished()
                    val user = mAuth.currentUser
                    var intent = Intent(this@signInActivity,mainDashBoardActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    if (DataFromOurCourses != ""){
                        intent.putExtra("anotherCourse",DataFromOurCourses)
                        intent.putExtra("DataOfCourse",courseDetails)
                    }
                    else{
                        intent.putExtra("anotherCourse","")

                    }
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("mainDashBoardActivity", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("mainDashBoardActivity", "handleFacebookAccessToken:$token")
        val credential = FacebookAuthProvider.getCredential(token!!.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("mainDashBoardActivity", "signInWithCredential:success")
                    onBoardingFinished()
                    var intent = Intent(this@signInActivity,mainDashBoardActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    if (DataFromOurCourses != ""){
                        intent.putExtra("anotherCourse",DataFromOurCourses)
                        intent.putExtra("DataOfCourse",courseDetails)
                    }
                    else{
                        intent.putExtra("anotherCourse","")
                    }
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("mainDashBoardActivity", "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun signInWithGithubProvider() {

        // There's something already here! Finish the sign-in for your user.
        val pendingResultTask: Task<AuthResult>? = mAuth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener {
                    // User is signed in.
                    Toast.makeText(this, "User exist", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    // Handle failure.
                    Toast.makeText(this, "Error : $it", Toast.LENGTH_LONG).show()
                }
        } else {

            mAuth.startActivityForSignInWithProvider( /* activity= */this, provider.build())
                .addOnSuccessListener(
                    OnSuccessListener<AuthResult?> {
                        // User is signed in.
                        // retrieve the current user
                        onBoardingFinished()
                        // navigate to HomePageActivity after successful login
                        var intent = Intent(this@signInActivity,mainDashBoardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        // send github user name from MainActivity to HomePageActivity
                        if (DataFromOurCourses != ""){
                            intent.putExtra("anotherCourse",DataFromOurCourses)
                            intent.putExtra("DataOfCourse",courseDetails)
                        }
                        else{
                            intent.putExtra("anotherCourse","")
                        }
                        this.startActivity(intent)
                        Toast.makeText(this, "Login Successfully", Toast.LENGTH_LONG).show()

                    })
                .addOnFailureListener(
                    OnFailureListener {
                        // Handle failure.
                        Toast.makeText(this, "Error : $it", Toast.LENGTH_LONG).show()
                    })
        }

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        signInButton.isEnabled = editTextTextEmailAddress.text.trim().isNotEmpty() && editTextTextPassword.text.trim().isNotEmpty()
    }

    override fun afterTextChanged(s: Editable?) {

    }
    //create a method to return the splash fragment a boolean value if the user click on nextButton send to splash fragment true
    //if true the app does not launch the welcome fragments
    private fun onBoardingFinished() {
        //assigning a sharedPref property to create a key and get a context of this fragment
        val sharedPref = this.getSharedPreferences("onSignUpBoarding", Context.MODE_PRIVATE)
        //assigning a editor property to create set a key and boolean value to use in splash fragment
        val editor = sharedPref.edit()
        //set those values
        editor.putBoolean("signProcessFinished", true)
        //apply it if the method called
        editor.apply()

    }

}