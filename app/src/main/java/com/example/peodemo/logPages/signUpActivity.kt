package com.example.peodemo.logPages

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.peodemo.DashBoard.mainDashBoardActivity
import com.example.peodemo.R
import com.example.peodemo.logPages.model.User
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class signUpActivity : AppCompatActivity(), TextWatcher {
    companion object{
        private const val RC_SIGN_IN = 120
    }
    private lateinit var googleSignInClient: GoogleSignInClient

    private var checkBoxIsEnabled:Int = 0

    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val fireStoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }


    private lateinit var callBackManger: CallbackManager

    private val currentUserDocRef: DocumentReference
        get() = fireStoreInstance.document("Users/${mAuth.currentUser?.uid.toString()}")

    private lateinit var loginBtn: ImageView
    private lateinit var githubEdit: EditText
    private val provider = OAuthProvider.newBuilder("github.com")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //addition the Home item to tapList array to access it in adapter class
        val window = this.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.signInActivityColor)




        githubEdit = editTextTextPersonName4
        loginBtn = githubSignUp

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


        callBackManger = CallbackManager.Factory.create()
        signWithFacebookSignUpActivity.setReadPermissions("email", "public_profile")
        signWithFacebookSignUpActivity.registerCallback(callBackManger, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d("mainDashBoardActivity", "facebook:onSuccess:$result")
                handleFacebookAccessToken(result.accessToken)
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
        googleSignUp.setOnClickListener {
            signIn()
        }


        editTextTextPersonName.addTextChangedListener(this)
        editTextTextPersonName2.addTextChangedListener(this)
        editTextTextPersonName3.addTextChangedListener(this)
        editTextTextPersonName4.addTextChangedListener(this)
        checkBoxSignUp.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked){
                checkBoxIsEnabled = 1
                signUpButton.isEnabled = editTextTextPersonName.text.trim().isNotEmpty() && editTextTextPersonName2.text.trim().isNotEmpty()
                        && editTextTextPersonName3.text.trim().isNotEmpty() && editTextTextPersonName4.text.trim().toString().length > 5
            }
            else{
                checkBoxIsEnabled = 0
                signUpButton.isEnabled = false
            }
        }



        //assigning a sign in property to use the view2 to make animations
        val signUpScrollview =this.findViewById<NestedScrollView>(R.id.nestedScrollView)
        val viewSignUp = this.findViewById<View>(R.id.view7)
        viewSignUp.animate().translationX(810f).duration = 7000
        signUpScrollview.animate().translationX(810f).duration = 7000




        signInActivitySignUpText.setOnClickListener {
            val intent = Intent(this,signInActivity::class.java)
            startActivity(intent)
        }
        textView22.setOnClickListener {
            val intent  = Intent(this,TermsAndConditionsActivity::class.java)
            startActivityForResult(intent,2)
        }




        signUpButton.setOnClickListener {
            //assigning a email property to store to the date that be in edit Text
            val email = editTextTextPersonName3.text.trim().toString()
            //assigning a password property to store to the date that be in edit Text
            val password = editTextTextPersonName4.text.trim().toString()
            //assigning a name property to store to the date that be in edit Text
            val name = editTextTextPersonName.text.trim().toString()
            //assigning a lastName  property to store to the date that be in edit Text
            val lastName = editTextTextPersonName2.text.trim().toString()
            if (email.isEmpty()){
                editTextTextPersonName3.error = "Please Enter Your Email"
                editTextTextPersonName3.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editTextTextPersonName3.error = "This Email is wrong"
                editTextTextPersonName3.requestFocus()
                return@setOnClickListener
            }
            if (password.length < 6){
                editTextTextPersonName4.error = "Please Enter 6 character"
                editTextTextPersonName4.requestFocus()
                return@setOnClickListener
            }
            addNewAccount(email,password,name,lastName)
            onBoardingFinished()
        }


    }




    private fun addNewAccount(email: String, password: String, name: String, lastName: String) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            val userInformation = User(email,password,name, lastName)
            if (task.isSuccessful){
                currentUserDocRef.set(userInformation)
                var intent = Intent(this@signUpActivity, mainDashBoardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                onBoardingFinished()
                Toast.makeText(this, "signUP Successfully", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this,task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }




    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK){
            var intent = data!!.extras!!.getInt("accept")
            if (intent == 1){
                checkBoxIsEnabled = 1
                checkBoxSignUp.isChecked = true
            }
            else if (intent == 2){
                checkBoxIsEnabled = 0
                checkBoxSignUp.isChecked = false
            }
        }
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


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (checkBoxIsEnabled == 1){
            signUpButton.isEnabled = editTextTextPersonName.text.trim().isNotEmpty() && editTextTextPersonName2.text.trim().isNotEmpty()
                    && editTextTextPersonName3.text.trim().isNotEmpty() && editTextTextPersonName4.text.trim().toString().length > 5
        }
        else {
            signUpButton.isEnabled = false
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("mainDashBoardActivity", "signInWithCredential:success")

                    val user = mAuth.currentUser
                    val userInformation = User(user?.email.toString(),"",user?.displayName.toString(),"")
                    currentUserDocRef.set(userInformation)
                    onBoardingFinished()
                    var intent = Intent(this@signUpActivity, mainDashBoardActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    Toast.makeText(this, "signUP Successfully", Toast.LENGTH_LONG).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("mainDashBoardActivity", "signInWithCredential:failure", task.exception)
                }
            }
    }



    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("mainDashBoardActivity", "handleFacebookAccessToken:$token")
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("mainDashBoardActivity", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    val userInformation = User(user?.email.toString(),"",user?.displayName.toString(),"")
                    currentUserDocRef.set(userInformation)
                    onBoardingFinished()
                    var intent = Intent(this@signUpActivity,mainDashBoardActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    Toast.makeText(this, "signUP Successfully", Toast.LENGTH_LONG).show()
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
                        val user = mAuth.currentUser
                        val userInformation = User(user?.email.toString(),"",user?.displayName.toString(),"")
                        currentUserDocRef.set(userInformation)
                        // navigate to HomePageActivity after successful login
                        var intent = Intent(this@signUpActivity,mainDashBoardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        // send github user name from MainActivity to HomePageActivity
                        this.startActivity(intent)
                        Toast.makeText(this, "signUP Successfully", Toast.LENGTH_LONG).show()
                        onBoardingFinished()
                    })
                .addOnFailureListener(
                    OnFailureListener {
                        // Handle failure.
                        Toast.makeText(this, "Error : $it", Toast.LENGTH_LONG).show()
                    })
        }

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