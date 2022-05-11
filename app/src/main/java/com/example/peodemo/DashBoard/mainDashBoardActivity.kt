package com.example.peodemo.DashBoard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peodemo.DashBoard.categoriesTabs.qoutes.quoteAlertDialog
import com.example.peodemo.DashBoard.categoriesTabs.qoutes.quoteModel
import com.example.peodemo.DashBoard.categoriesTabs.tabsModel
import com.example.peodemo.DashBoard.categoriesTabs.tabsViewModel
import com.example.peodemo.DashBoard.courseGropie.ModulesPage.courseModulesPageActivity
import com.example.peodemo.DashBoard.courseGropie.courseItems
import com.example.peodemo.DashBoard.courseGropie.courseModelMainDashBoard
import com.example.peodemo.DashBoard.glide.GlideApp
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.*
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials.QuestionsModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials.courseLessonQuizDetailsModel
import com.example.peodemo.home.introduction.introductionActivity
import com.example.peodemo.logPages.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_main_dash_board2.*
import kotlinx.android.synthetic.main.quotes_alert_dialog.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class mainDashBoardActivity : AppCompatActivity(),tabsViewModel.OnItemClickListener {


    private lateinit var courseItemSection: Section

    private var DataFromOurCourses = ""
    private lateinit var courseDetails: CoursesModel
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



    private val QuoteDocRef: CollectionReference
        get() = fireStoreInstance.collection("quotes")

    private var personHasQuotes = ArrayList<String>()
    private var qouteIndex = 0
    private var quotesInformation = mutableListOf<quoteModel>()
    private var MyCoursesitems = mutableListOf<Item>()

    private val mStorage:FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    private var modules = ArrayList<courseModulesModel>()
    private var Lessons = ArrayList<CourseLessonsModel>()
    private var LessonDetailsDescription = ArrayList<String>()



    private val currentUserStorageRef:StorageReference
        get() = mStorage.reference.child(mAuth.currentUser?.uid.toString())

    //assigning a tapList array property to store the recycle view in it
    private val  tapList = java.util.ArrayList<tabsModel>()
    private var Position = 0
    //assigning a adapter property to store the tapView instance in ti
    private val adapter = tabsViewModel(tapList,this)
    private lateinit var timer:CountDownTimer
    private var courseProcessChecked = false


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dash_board2)

        timer =  object : CountDownTimer(60000, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {

            }

            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                getQuotesFromServer()
                timer.start()
                Position++
                if (Position >= tapList.size){
                    Position = 0
                }
                selectTabOnCategories(Position)
            }

        }.start()


        tapList.add(tabsModel("Quotes",1))
        tapList.add(tabsModel("Design",0))
        tapList.add(tabsModel("backEnd",0))
        tapList.add(tabsModel("Swift UI",0))
        dashboardTabsRecycleView.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL,false)
        dashboardTabsRecycleView.adapter = adapter
        //assigning this property to context the activity on it
        val window = this.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.white)
        window?.decorView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        DataFromOurCourses = intent.getStringExtra("anotherCourse") as String

        getUserInformation { User ->

            //setCourseInformationOnTheServer("Foundation",3,5)
            //DataFromOurCourses = User.CourseDetails
            if (User.profileImageURI.isNotEmpty() && User.logWithState){

                GlideApp.with(this@mainDashBoardActivity).load(User.profileImageURI).placeholder(R.drawable.user).into(profileCircleImageView)

            }
            else if (User.profileImageURI.isNotEmpty() && !User.logWithState){
                GlideApp.with(this@mainDashBoardActivity)
                    .load(mStorage.getReference(User.profileImageURI))
                    .placeholder(R.drawable.user)
                    .into(profileCircleImageView)
            }
            if (User.lastName.isNotEmpty()){
                dashBoardUserName.text = User.name
            }else{
                dashBoardUserName.text = User.name
            }

        }
        logout_button.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this,introductionActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            onBoardingSignOutFinished()
        }
        profileButton.setOnClickListener {

        }

        right.setOnClickListener {
            if(Position >= tapList.size - 1){

            }else{
                Position++
            }
            selectTabOnCategories(Position)
        }
        left.setOnClickListener {
            if(Position <= 0){
            }else {
                Position--
            }
            selectTabOnCategories(Position)

        }



        cardLayout.setOnClickListener {
            setQuoteDialog()
        }


        profileCircleImageView.setOnClickListener {
            val getImageFromGallery = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg","image/png"))
            }

            startActivityForResult(Intent.createChooser(getImageFromGallery,"select the best image you have"), RC_S_IMAGE)
        }
        GlobalScope.launch {
            getQuotesFromServer()
            if (DataFromOurCourses != ""){
                courseProcessChecked = true
                getUserInformation { User ->
                    val user = mAuth.currentUser
                    val userDataByHashMap = mutableMapOf<String, Any>()
                    userDataByHashMap["logWithState"] = User.logWithState
                    userDataByHashMap["profileImageURI"] = User.profileImageURI
                    userDataByHashMap["email"] = user?.email.toString()
                    userDataByHashMap["lastName"] = User.lastName
                    userDataByHashMap["name"] = User.name
                    userDataByHashMap["password"] = User.password
                    userDataByHashMap["courseCheckedOut"] = courseProcessChecked
                    currentUserDocRef.update(userDataByHashMap)
                }
                courseDetails = intent.getSerializableExtra("DataOfCourse") as CoursesModel
                setCourseInformationOnTheServer(courseDetails.name!!)
                delay(25000L)
            }

            getCourseInformation(:: initRecycleView)
        }




    }

    private fun getCourseInformation(onListen : (List<Item>) -> Unit):ListenerRegistration {
        return currentUserDocRef.collection("My Courses").addSnapshotListener { value, error ->
            if (error != null){
                return@addSnapshotListener
            }
            MyCoursesitems.clear()
            value!!.documents.forEach {
                MyCoursesitems.add(courseItems(it.toObject(CoursesModel::class.java)!!,this))
            }
            onListen(MyCoursesitems)
        }
    }

    @SuppressLint("WrongConstant")
    private fun initRecycleView(item: List<Item>){
        mainDashboardCourseRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@mainDashBoardActivity, LinearLayout.HORIZONTAL,false)
            adapter = GroupAdapter<ViewHolder>().apply {
                courseItemSection =  Section(item)
                add(courseItemSection)
                setOnItemClickListener(onItemClick)
            }
        }
    }
    val onItemClick = OnItemClickListener {Item,View ->
        if (Item is courseItems){
            if (Item.courseInfo.id == null){

            }else{
                val intent = Intent(this,courseModulesPageActivity::class.java)
                intent.putExtra("courseInfo",Item.courseInfo.name)
                intent.putExtra("dashboardPassBoarding",true)
                intent.putExtra("courseInfourl",Item.courseInfo.introducitonURL)
                intent.putExtra("courseModule",Item.courseInfo.modulesCount)
                intent.putExtra("courseLessons",Item.courseInfo.lessonsCount)
                intent.putExtra("courseVideos",Item.courseInfo.videosCount)
                startActivity(intent)
                this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
            }
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
                    userDataByHashMap["name"] = it.name
                    userDataByHashMap["password"] = it.password
                    userDataByHashMap["CourseDetails"] = ""
                    userDataByHashMap["courseCheckedOut"] = it.courseCheckedOut
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


    private fun setCourseInformationOnTheServer(courseName: String){
        val newCourse = CoursesModel("New",null,R.drawable.newcoursegif,null,null,null,null,null,0,null,null,null,null)
        if (courseProcessChecked){
            setDataIntoCourseModel("iosFC1")
            courseDetails.ModulesDetails = modules
            fireStoreInstance.collection("Users")
                .document(mAuth.currentUser!!.uid)
                .collection("My Courses")
                .document(courseName).set(courseDetails)
            fireStoreInstance.collection("Users")
                .document(mAuth.currentUser!!.uid)
                .collection("My Courses")
                .document(newCourse.name!!).set(newCourse)
            for (item in 0 until modules.size){
                val module = modules[item]
                fireStoreInstance.collection("Users")
                    .document(mAuth.currentUser!!.uid)
                    .collection("My Courses")
                    .document(courseName).collection("Modules").document(module.name!!).set(module)
                for (listOfLessons in 0 until module.lessonsCount!!){
                    val lessons = module.lessonDetails!!
                    if (lessons.isNotEmpty()){
                        val lesson = lessons[listOfLessons]
                        val lessonDetails = lesson.lessonDetails
                        fireStoreInstance.collection("Users")
                            .document(mAuth.currentUser!!.uid)
                            .collection("My Courses")
                            .document(courseName).collection("Modules").document(module.name).collection("Lessons").document("lesson ${listOfLessons + 1}").set(lesson)
                        fireStoreInstance.collection("Users")
                            .document(mAuth.currentUser!!.uid)
                            .collection("My Courses")
                            .document(courseName).collection("Modules").document(module.name).collection("Lessons").document("lesson ${listOfLessons + 1}")
                            .collection("lesson Details").document("lesson info")
                            .set(lessonDetails!!)
                        fireStoreInstance.collection("Users")
                            .document(mAuth.currentUser!!.uid)
                            .collection("My Courses")
                            .document(courseName).collection("Modules").document(module.name).collection("Lessons").document("lesson ${listOfLessons + 1}")
                            .collection("lesson Details").document("challenge info").set(lesson.lessonChallengeDetails!!)
                        fireStoreInstance.collection("Users")
                            .document(mAuth.currentUser!!.uid)
                            .collection("My Courses")
                            .document(courseName).collection("Modules").document(module.name).collection("Lessons").document("lesson ${listOfLessons + 1}")
                            .collection("lesson Details").document("Quiz info").set(lesson.lessonQUIZDetails!!)
                    }

                }
            }
        }
    }


    override fun onItemClick(position: Int) {
        selectTabOnCategories(position)
    }

    private fun getQuotesFromServer(){
        personHasQuotes.clear()
        quotesInformation.clear()
        QuoteDocRef.addSnapshotListener { value, error ->
            if (error != null){
                return@addSnapshotListener
            }
            var index = 0
            value!!.documents.forEach {
                quotesInformation.add(it.toObject(quoteModel::class.java)!!)
                personHasQuotes.add(quotesInformation[index].name)
                index++
            }
            qouteIndex = (0 until personHasQuotes.size).random()
            cardHint.text = personHasQuotes[qouteIndex]
        }

    }

    fun setQuoteDialog(){
        quoteAlertDialog(quotesInformation[qouteIndex]).show(supportFragmentManager,"mydialog")

    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }


    override fun onResume() {
        super.onResume()
        timer.start()
    }
    private fun selectTabOnCategories(position: Int){
        Position = position
        when (position){
            // if the user tap on the first item
            0 ->{
                getQuotesFromServer()
                //cardHint.text = personHasQuotes[qouteIndex]
                //call this item and put it in tap property
                var taps = tapList[position]
                taps.pressed = 1
                //And set 0 to other items
                tapList[1].pressed = 0
                tapList[2].pressed = 0
                tapList[3].pressed = 0
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(1)
                adapter.notifyItemChanged(2)
                adapter.notifyItemChanged(3)
                dashboardTabsRecycleView.smoothScrollToPosition(position)


            }
            // if the user tap on the second item
            1 ->{
                //call this item and put it in tap property
                var taps = tapList[position]
                //and set a last var in this item to 1, if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                tapList[0].pressed = 0
                tapList[2].pressed = 0
                tapList[3].pressed = 0
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(2)
                adapter.notifyItemChanged(3)
                dashboardTabsRecycleView.smoothScrollToPosition(position)

            }
            // if the user tap on the third item
            2 ->{
                //call this item and put it in tap property
                var taps = tapList[position]
                //and set a last var in this item to 1, if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                tapList[0].pressed = 0
                tapList[1].pressed = 0
                tapList[3].pressed = 0
                //And change the item in the tap array
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(1)
                adapter.notifyItemChanged(3)
                dashboardTabsRecycleView.smoothScrollToPosition(position)

            }
            // if the user tap on the third item
            3 ->{
                //call this item and put it in tap property
                var taps = tapList[position]
                //and set a last var in this item to 1, if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                tapList[0].pressed = 0
                tapList[1].pressed = 0
                tapList[2].pressed = 0
                //And change the item in the tap array
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(1)
                adapter.notifyItemChanged(2)
                dashboardTabsRecycleView.smoothScrollToPosition(position)

            }

        }
    }



    private fun setDataIntoCourseModel(CourseName:String){

        if (CourseName == "iosFC1"){
            for (index in 1..13){
                if (index == 1){
                    var lessonQuizQuestions = ArrayList<QuestionsModel>()
                    for (QuestionNumber in 1..3){

                        if (QuestionNumber == 1){
                            var Answers = ArrayList<String>()
                            Answers.add("SwiftUI")
                            Answers.add("Xcode")
                            Answers.add("Swift")
                            lessonQuizQuestions.add(QuestionsModel("What is the programming language we use to write code?",Answers,"Swift is the programming language whereas SwiftUI is the UI (user interface) framework used to construct the app interface. Xcode is the development environment where we write Swift code and use SwiftUI to build the interface.",
                                2))
                        }
                        else if (QuestionNumber == 2){
                            var Answers = ArrayList<String>()
                            Answers.add("A web portal to get provisioning credentials for your app.")
                            Answers.add("A web portal to customize your app store listings, check analytics and view reports.")
                            Answers.add("A web portal to download beta software and additional tools.")
                            lessonQuizQuestions.add(QuestionsModel("What is App Store Connect?",Answers,"App Store Connect is the portal where you create app listings, upload your app, submit your apps for review, manage your listings, manage in app purchases, submit banking and tax information, view sales and \u0000 financial reports and more! It's essentially your connection to the Apple App Store.",
                                1))

                        }
                        else if (QuestionNumber == 3){
                            var Answers = ArrayList<String>()
                            Answers.add("N0")
                            Answers.add("Yes")
                            lessonQuizQuestions.add(QuestionsModel("Will you still be able to build apps without the Apple Developer Program?",Answers,"Yes, you can build and deploy apps to your device even if you don't join the Apple Developer Program. However, you won't be able to submit apps into the App Store and you won't be able to send your app to other people for testing.",
                                1))
                        }
                    }
                    val courseDetails =  courseLessonDetailsModel("The Apple Developer Ecosystem","https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%201%2Flesson%2Fthe%20Apple%20Developer%20Ecosystem.mp4?alt=media&token=fbd6722a-9dab-4908-a73f-d45c6fe7a5f4",
                        false,LessonDetailsDescription,null)
                    val lessonQuiz = courseLessonQuizDetailsModel("Lesson 1 Quiz",lessonQuizQuestions,false)
                    val ChallengeDetails = courseLessonChellengeDetailsModel()
                    setLessonsOFModules("iosFC1M1L1","The Apple Developer Ecosystem",1,1,1,0,0,false,false,true,courseDetails,lessonQuiz,ChallengeDetails,null)
                }
                else if (index == 2){
                    var lessonQuizQuestions = ArrayList<QuestionsModel>()
                    for (QuestionNumber in 1..3){
                        if (QuestionNumber == 1){
                            var Answers = ArrayList<String>()
                            Answers.add("For editing code when a Swift file is selected in the Navigator.")
                            Answers.add("For configuring project properties when the project file is selected in the Navigator.")
                            Answers.add("For managing the assets for the project when the Asset Library is selected in the Navigator.")
                            Answers.add("All of the above.")
                            lessonQuizQuestions.add(QuestionsModel("What is the Editor area used for?",Answers,"The Editor area will adapt to the type of \u0000le you select in the Navigator panel to\n" +
                                    "its left and allow you to edit the contents of the file.",
                                3))
                        }
                        else if (QuestionNumber == 2){
                            var Answers = ArrayList<String>()
                            Answers.add("For diagnosing problems with your user interface.")
                            Answers.add("For looking at details or customizing the attributes of what you select in the Editor area.")
                            Answers.add("For inspecting the status of your Xcode project.")
                            lessonQuizQuestions.add(QuestionsModel("What is the Inspector Panel used for?",Answers,"The Inspector panel adapts to what you've selected in the Editor area. It contains several panels which displays information and lets you configure attributes and properties of what you've selected in the Editor area.",
                                1))

                        }
                        else if (QuestionNumber == 3){
                            var Answers = ArrayList<String>()
                            Answers.add("It's an auto generated ID for your app bundle which cannot be modified.")
                            Answers.add("It's the unique identifier for your app that you specify when you create a new Xcode project.")
                            Answers.add("All of the above.")
                            lessonQuizQuestions.add(QuestionsModel("What is the bundle identifier?",Answers,"The bundle identifier is a combination of the product name and organization identi\u0000er that you specify when you create a new Xcode project. It uniquely identifies your app in the App Store. You can change the bundle identifier for your project at any time by going into the project properties.",
                                1))
                        }
                    }
                    val lessonQuiz = courseLessonQuizDetailsModel("Lesson 2 Quiz",lessonQuizQuestions,false)
                    val courseDetails =  courseLessonDetailsModel("Introduction to Xcode","https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%202%2Flesson%2Fintroduction%20to%20xcode.mp4?alt=media&token=4aa6c239-fb69-4d8f-839a-fd54914ac796",
                        false,LessonDetailsDescription,null)
                    val ChallengeDetails = courseLessonChellengeDetailsModel()
                    setLessonsOFModules("iosFC1M1L2","Introduction to Xcode",2,1,3,0,0,false,false,false,courseDetails,lessonQuiz,ChallengeDetails,null)
                }
                else if (index == 3){
                    var lessonQuizQuestions = ArrayList<QuestionsModel>()
                    for (QuestionNumber in 1..3){
                        if (QuestionNumber == 1) {
                            var Answers = ArrayList<String>()
                            Answers.add("Modifies the behavior of the app")
                            Answers.add("Changes the look and behavior of a view element")
                            Answers.add("Changes the preview")
                            lessonQuizQuestions.add(QuestionsModel("What's the purpose of a modifier?",Answers,"A modifier is specified after a view element to modify its look and/or behavior. Multiple modifiers can be chained one after another to manipulate the view element.\n" +
                                    "Modifiers are applied sequentially to the view element so in some cases, varying the order that they're applied in will yield different results.",
                                1))
                        }
                        else if (QuestionNumber == 2){
                            var Answers = ArrayList<String>()
                            Answers.add("Run it in the simulator")
                            Answers.add("Using the Preview canvas")
                            Answers.add("All of the above")
                            lessonQuizQuestions.add(QuestionsModel("What are some of the ways we can preview the UI?",Answers,"You can launch the iOS simulator to run your app (and see what the UI looks like) or you can use the Preview canvas to see a preview of your UI.\n" +
                                    "The Preview canvas also has a live mode where you can interact with the app right within the canvas. This live mode button looks like a triangular \"play\" icon right above the UI in the canvas.",
                                2))
                        }
                        else if (QuestionNumber == 3){
                            var Answers = ArrayList<String>()
                            Answers.add("Writing code")
                            Answers.add("Inserting elements via the Library panel")
                            Answers.add("Modifying the UI through the preview canvas")
                            Answers.add("Configuring elements through the Inspector panel")
                            Answers.add("All of the above")
                            lessonQuizQuestions.add(QuestionsModel("What are some ways to build your UI?",Answers,"The UI is generated from code so you can simply write code to build your UI, however, Xcode provides many ways to visually build and con\u0000gure your UI as well.\n" +
                                    "Some of these way include using the Preview canvas, Inspector panel or Library panel.",
                                4))
                        }
                    }
                    val lessonQuiz = courseLessonQuizDetailsModel("Lesson 3 Quiz",lessonQuizQuestions,false)
                    val ChallengeDetails = courseLessonChellengeDetailsModel("Lesson 3 Challenge",ArrayList<String>(),"https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%203%2Fchallenge%2FLesson%203%20Challenge.mp4?alt=media&token=dd0f46f4-8471-4c48-9685-5d197b560244",false)
                    val courseDetails =  courseLessonDetailsModel("How To Build User Interfaces","https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%203%2Flesson%2Flesson%203%20How%20To%20Build%20User%20Interfaces.mp4?alt=media&token=2e45d1aa-86c7-414b-9516-6a605316caf6",
                        false,LessonDetailsDescription,null)
                    setLessonsOFModules("iosFC1M1L3","How To Build User Interfaces",1,2,1,0,0,false,false,false,courseDetails,lessonQuiz,ChallengeDetails,null)
                }
                else if (index == 4){
                    var lessonQuizQuestions = ArrayList<QuestionsModel>()
                    for (QuestionNumber in 1..3){
                        if (QuestionNumber == 1){
                            var Answers = ArrayList<String>()
                            Answers.add("Takes up all the available space")
                            Answers.add("Shares the space with other Spacer elements in the same container")
                            Answers.add("All of the above")
                            lessonQuizQuestions.add(QuestionsModel("What does a spacer element do?",Answers,"A spacer element will expand to take up all the available space within the container. However, if there are multiple spacer elements in the same container, they will each get an equal amount of space.",
                                2))
                        }
                        else if (QuestionNumber == 2){
                            var Answers = ArrayList<String>()
                            Answers.add("Specify the image filename in between the parenthesis of your Image element")
                            Answers.add("Add the image to the asset library and specify the asset name in between the parenthesis of your Image element")
                            Answers.add("Drag and drop your image file into the Image element of the code editor")
                            lessonQuizQuestions.add(QuestionsModel("How do you specify the image to display for an Image element?",Answers,"The asset library of your Xcode project is where you add your images to use them in your app. Once added, each image will have an asset name that you use to reference it in-between the parenthesis of the Image view element.",
                                1))

                        }
                        else if (QuestionNumber == 3){
                            var Answers = ArrayList<String>()
                            Answers.add("Spacer")
                            Answers.add("ZStack")
                            Answers.add("Image")
                            lessonQuizQuestions.add(QuestionsModel("Which element below is a container element?",Answers,"The ZStack (also the HStack and VStack) is a container element that can hold up to 10 elements within it.",
                                1))
                        }
                    }
                    val lessonQuiz = courseLessonQuizDetailsModel("Lesson 4 Quiz",lessonQuizQuestions,false)
                    val ChallengeDetails = courseLessonChellengeDetailsModel("Lesson 4 Challenge",ArrayList<String>(),"https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%204%2Fchallenge%2FLesson%204%20Challenge.mp4?alt=media&token=7c25065c-5a8e-4c9c-a301-d14a210ded52",false)
                    val courseDetails =  courseLessonDetailsModel("SwiftUI Views and Containers","https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%204%2Flesson%2FSwiftUI%20Views%20and%20Containers.mp4?alt=media&token=5a26317f-7ff5-43c1-9b89-0c1ceeea499f",
                        false,LessonDetailsDescription,null)
                    setLessonsOFModules("iosFC1M1L4","SwiftUI Views and Containers",4,2,3,0,0,false,false,false,courseDetails,lessonQuiz,ChallengeDetails,null)
                }
                else if (index == 5){
                    val lessonQuiz = courseLessonQuizDetailsModel()
                    val ChallengeDetails = courseLessonChellengeDetailsModel()
                    val courseDetails =  courseLessonDetailsModel("Build The War Card Game UI","https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%205%2Flesson%2FLesson%205%20Build%20The%20War%20Card%20Game%20UI.mp4?alt=media&token=7ca6b0d3-c893-45cb-87c4-456b9df34b1c",
                        false,LessonDetailsDescription,null)
                    setLessonsOFModules("iosFC1M1L5","Build The War Card Game UI",5,1,0,0,0,false,false,false,courseDetails,lessonQuiz,ChallengeDetails,null)
                }
                else if (index == 6){
                    var lessonQuizQuestions = ArrayList<QuestionsModel>()
                    for (QuestionNumber in 1..3){
                        if (QuestionNumber == 1){
                            var Answers = ArrayList<String>()
                            Answers.add("Use two forward slashes (//)")
                            Answers.add("Use two backslashes (\\)")
                            Answers.add("Use one number sign (#)")
                            lessonQuizQuestions.add(QuestionsModel("How do we add a comment to our Swift code?",Answers,"Two forward slashes (//) are used to indicate a comment. Xcode will ignore this entire line.",
                                0))
                        }
                        else if (QuestionNumber == 2){
                            var Answers = ArrayList<String>()
                            Answers.add("We can't reassign data to a variable after the \u0000rst assignment of data to it.")
                            Answers.add("We can't reassign data to a constant after the \u0000rst assignment of data to it.")
                            Answers.add("A constant must initially be assigned data when it's declared.")
                            lessonQuizQuestions.add(QuestionsModel("What’s the difference between a variable and a constant?",Answers,"A constant doesn't allow you to reassign data to it once it has been assigned data the \u0000rst time.",
                                1))

                        }
                        else if (QuestionNumber == 3){
                            var Answers = ArrayList<String>()
                            Answers.add("The variable will be able to store any data type.")
                            Answers.add("The variable will infer the data type from the \u0000rst piece of data you assign to it.")
                            Answers.add("The variable will be unusable.")
                            lessonQuizQuestions.add(QuestionsModel("What happens when you don’t specify a data type for your variable when you declare it?",Answers,"Xcode infer what type of data your variable will track from the \u0000rst piece of data you assign to it.",
                                1))
                        }
                    }
                    val lessonQuiz = courseLessonQuizDetailsModel("Lesson 6 Quiz",lessonQuizQuestions,false)
                    val ChallengeDetails = courseLessonChellengeDetailsModel()
                    val courseDetails =  courseLessonDetailsModel("Swift Variables and Constants","https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%206%2FLesson%206%20%20Swift%20Variables%20and%20Constants.mp4?alt=media&token=72fbf12c-945a-489d-8c64-c1bdd7dd0210",
                        false,LessonDetailsDescription,null)
                    setLessonsOFModules("iosFC1M1L6","Swift Variables and Constants",6,1,3,0,0,false,false,false,courseDetails,lessonQuiz,ChallengeDetails,null)

                }
                else if (index == 7){
                    var lessonQuizQuestions = ArrayList<QuestionsModel>()
                    for (QuestionNumber in 1..3){
                        if (QuestionNumber == 1){
                            var Answers = ArrayList<String>()
                            Answers.add("A function is a mathematical operation performed on numbers.")
                            Answers.add("A function is a block of code with a name that can be executed when called by name.")
                            Answers.add("A function is for storing values in a class.")
                            lessonQuizQuestions.add(QuestionsModel("In Swift, what is a function?",Answers,"Functions are used to group together a set of code statements that work together to perform a task. Whenever you want to execute that block of code and perform that task, simply call the function by name.",
                                1))
                        }
                        else if (QuestionNumber == 2){
                            var Answers = ArrayList<String>()
                            Answers.add("Declaring a function is writing the code that you want to be executed when the function is called. Calling a function is running the code that is inside of the function.")
                            Answers.add("There is no difference.")
                            Answers.add("Declaring a function is the same as declaring a variable. Calling a function is the same as declaring a constant.")
                            lessonQuizQuestions.add(QuestionsModel("What is the difference between declaring a function and calling a function?",Answers,"Declaring a function is simply the act of de\u0000ning/creating it while calling a\n" +
                                    "function is the act of using it and putting it into action.",
                                0))

                        }
                        else if (QuestionNumber == 3){
                            var Answers = ArrayList<String>()
                            Answers.add("True")
                            Answers.add("False")
                            lessonQuizQuestions.add(QuestionsModel("Variables declared in a function won’t exist outside of the function. This is called variable scope. True or false?",Answers,"",
                                0))
                        }
                    }
                    val lessonQuiz = courseLessonQuizDetailsModel("Lesson 7 Quiz",lessonQuizQuestions,false)
                    val ChallengeDetails = courseLessonChellengeDetailsModel("Lesson 7 Challenge",null,null,false)
                    val courseDetails =  courseLessonDetailsModel("Swift Functions","https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%207%2FLesson%207%20Swift%20Functions.mp4?alt=media&token=dc9472ef-ef1e-4c9e-8b73-fb84cf530d2c",
                        false,LessonDetailsDescription,null)
                    setLessonsOFModules("iosFC1M1L7","Swift Functions",7,1,3,0,0,false,false,false,courseDetails,lessonQuiz,ChallengeDetails,null)

                }
                else if (index == 8){
                    var lessonQuizQuestions = ArrayList<QuestionsModel>()
                    for (QuestionNumber in 1..3){
                        if (QuestionNumber == 1){
                            var Answers = ArrayList<String>()
                            Answers.add("A structure is a way to organize your code to model or represent something.")
                            Answers.add("A structure is a category of code that is used for user interfaces.")
                            Answers.add("A structure is a way for you to execute a block of code by calling its name.")
                            lessonQuizQuestions.add(QuestionsModel("What is a structure?",Answers,"A structure is another form of organization that serves as a building block of your app and is used to represent something (a view, a role, a component etc) in your app.",
                                0))
                        }
                        else if (QuestionNumber == 2){
                            var Answers = ArrayList<String>()
                            Answers.add("A property is another name for a function inside of a structure.")
                            Answers.add("A property is another name for a variable inside of a structure.")
                            Answers.add("A property stores a value in a function.")
                            lessonQuizQuestions.add(QuestionsModel("What is a property?",Answers,"",
                                1))

                        }
                        else if (QuestionNumber == 3){
                            var Answers = ArrayList<String>()
                            Answers.add("The variable will be able to store any data type.")
                            Answers.add("The variable will infer the data type from the \u0000rst piece of data you assign to it.")
                            Answers.add("The variable will be unusable.")
                            lessonQuizQuestions.add(QuestionsModel("What happens when you don’t specify a data type for your variable when you declare it?",Answers,"Xcode infer what type of data your variable will track from the \u0000rst piece of data you assign to it.",
                                1))
                        }
                    }
                    val lessonQuiz = courseLessonQuizDetailsModel("Lesson 8 Quiz",lessonQuizQuestions,false)
                    val ChallengeDetails = courseLessonChellengeDetailsModel("Lesson 8 Challenge",null,null,false)
                    val courseDetails =  courseLessonDetailsModel("Swift Structures","https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%208%2Flesson%2FLesson%208%20Swift%20Structures.mp4?alt=media&token=c17fc93c-c4f4-4b86-ac57-0ce1320a24c7",
                        false,LessonDetailsDescription,null)
                    setLessonsOFModules("iosFC1M1L8","Swift Structures",8,1,3,0,0,false,false,false,courseDetails,lessonQuiz,ChallengeDetails,null)

                }
                else if (index == 9){
                    var lessonQuizQuestions = ArrayList<QuestionsModel>()
                    for (QuestionNumber in 1..3){
                        if (QuestionNumber == 1){
                            var Answers = ArrayList<String>()
                            Answers.add("By creating an instance of it")
                            Answers.add("By using dot-notation")
                            Answers.add("By calling it by the method name")
                            lessonQuizQuestions.add(QuestionsModel("How do we use a structure?",Answers,"",
                                0))
                        }
                        else if (QuestionNumber == 2){
                            var Answers = ArrayList<String>()
                            Answers.add("By using dot-notation on the structure name")
                            Answers.add("By using dot-notation on an instance of the structure")
                            Answers.add("By referencing the property or method name")
                            lessonQuizQuestions.add(QuestionsModel("How do we access the properties and methods of a structure?",Answers,"",
                                1))

                        }
                        else if (QuestionNumber == 3){
                            var Answers = ArrayList<String>()
                            Answers.add("Yes")
                            Answers.add("No")
                            lessonQuizQuestions.add(QuestionsModel("Are instances of a structure independent from one another?",Answers,"",
                                0))
                        }
                    }
                    val lessonQuiz = courseLessonQuizDetailsModel("Lesson 9 Quiz",lessonQuizQuestions,false)
                    val ChallengeDetails = courseLessonChellengeDetailsModel("Lesson 9 Challenge",null,null,false)
                    val courseDetails =  courseLessonDetailsModel("Swift Instances","https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%209%2FLesson%209%20Swift%20Instances.mp4?alt=media&token=251fc3f5-dafd-4c08-8f5c-981f31b2c5bd",
                        false,LessonDetailsDescription,null)
                    setLessonsOFModules("iosFC1M1L9","Swift Instances",9,1,1,0,0,false,false,false,courseDetails,lessonQuiz,ChallengeDetails,null)
                }
                else if (index == 10){
                    var lessonQuizQuestions = ArrayList<QuestionsModel>()
                    for (QuestionNumber in 1..3){
                        if (QuestionNumber == 1){
                            var Answers = ArrayList<String>()
                            Answers.add("It's the same thing as a method")
                            Answers.add("A block of code that can be passed into a method call as a parameter")
                            Answers.add("A block of code that can be executed by calling its name")
                            lessonQuizQuestions.add(QuestionsModel("What is a closure?",Answers,"A closure is simply a block of code statements enclosed in a set of curly brackets. It's like a function without a name. Closures can be passed into method calls as parameters.",
                                1))
                        }
                        else if (QuestionNumber == 2){
                            var Answers = ArrayList<String>()
                            Answers.add("It's a method that accepts parameters.")
                            Answers.add("It's a method that is attached to the end of a method call")
                            Answers.add("It's a closure that is taken out of the parameter list and moved to the end of the method call")
                            lessonQuizQuestions.add(QuestionsModel("What is a trailing closure?",Answers,"A trailing closure is a closure that is shorthand that involves taking the closure parameter out of the parameter list of a method call and declaring the closure behind the method call. A closure in a parameter list can only be turned into a trailing closure if it's the last parameter in the method call.",
                                2))

                        }
                        else if (QuestionNumber == 3){
                            var Answers = ArrayList<String>()
                            Answers.add("By specifying a closure for the label parameter of the Button initializer")
                            Answers.add("By specifying a method to call when the button is tapped")
                            Answers.add("By specifying a closure for the action parameter of the Button initializer")
                            lessonQuizQuestions.add(QuestionsModel("How do you handle the tap of a Button?",Answers,"The button initializer includes an action parameter that accepts a closure. When the button is tapped, the code in the closure will be executed.",
                                2))
                        }
                    }
                    val lessonQuiz = courseLessonQuizDetailsModel("Lesson 10 Quiz",lessonQuizQuestions,false)
                    val ChallengeDetails = courseLessonChellengeDetailsModel("Lesson 10 Challenge",null,null,false)
                    val courseDetails =  courseLessonDetailsModel("SwiftUI Buttons","https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%2010%2FLesson%2010%20SwiftUI%20Buttons.mp4?alt=media&token=ebd045a9-1f7c-4c4b-a5aa-5a4da952f041",
                        false,LessonDetailsDescription,null)
                    setLessonsOFModules("iosFC1M1L10","SwiftUI Buttons",10,1,3,0,0,false,false,false,courseDetails,lessonQuiz,ChallengeDetails,null)

                }
                else if (index == 11){
                    var lessonQuizQuestions = ArrayList<QuestionsModel>()
                    for (QuestionNumber in 1..3){
                        if (QuestionNumber == 1){
                            var Answers = ArrayList<String>()
                            Answers.add("It's a property with 2 states: true or false")
                            Answers.add("It's a constant that you can reassign data to")
                            Answers.add("It's a property that the view code relies on to render its UI")
                            lessonQuizQuestions.add(QuestionsModel("What is a state property?",Answers,"Adding the @State property wrapper to a property declaration will signify that it's a piece of data that the view code relies upon to display the UI. Furthermore, state properties will allow you to change the data it stores.",
                                2))
                        }
                        else if (QuestionNumber == 2){
                            var Answers = ArrayList<String>()
                            Answers.add("By using the State keyword in front of the property declaration")
                            Answers.add("By using the @State keyword in front of the property declaration")
                            Answers.add("By using the state property wrapper as the data type in the property declaration")
                            lessonQuizQuestions.add(QuestionsModel("How do you specify that a property is a state property?",Answers,"The @State property wrapper can be speci\u0000ed in front of a property declaration to mark it as a state property.",
                                1))

                        }
                        else if (QuestionNumber == 3){
                            var Answers = ArrayList<String>()
                            Answers.add("By specifying the property names in your view code")
                            Answers.add("By calling methods in your view code")
                            Answers.add("By linking the data to the view code")
                            lessonQuizQuestions.add(QuestionsModel("How do you reference your properties in your view code?",Answers,"You can simply reference your properties by name in your view code.",
                                0))
                        }
                    }
                    val lessonQuiz = courseLessonQuizDetailsModel("Lesson 11 Quiz",lessonQuizQuestions,false)
                    val ChallengeDetails = courseLessonChellengeDetailsModel("Lesson 11 Challenge",null,null,false)
                    val courseDetails =  courseLessonDetailsModel("State Properties","https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%2011%2FLesson%2011%20State%20Properties.mp4?alt=media&token=7e2ca1a9-4ee9-4ebe-bc88-bb973b415771",
                        false,LessonDetailsDescription,null)
                    setLessonsOFModules("iosFC1M1L11","State Properties",11,1,3,0,0,false,false,false,courseDetails,lessonQuiz,ChallengeDetails,null)

                }
                else if (index == 12){
                    var lessonQuizQuestions = ArrayList<QuestionsModel>()
                    for (QuestionNumber in 1..3){
                        if (QuestionNumber == 1){
                            var Answers = ArrayList<String>()
                            Answers.add("=")
                            Answers.add("==")
                            Answers.add("!=")
                            lessonQuizQuestions.add(QuestionsModel("How do you compare equality?",Answers,"Use == to compare equality because = is used for assignment.",
                                1))
                        }
                        else if (QuestionNumber == 2){
                            var Answers = ArrayList<String>()
                            Answers.add("All branches where the condition is true will be executed")
                            Answers.add("The last branch with a true condition will be executed")
                            Answers.add("The \u0000rst branch with a true condition will be executed")
                            lessonQuizQuestions.add(QuestionsModel("Assume you have an \"If\" statement with multiple if and else if branches. What happens if multiple conditions are true?",Answers,"The conditions are checked top to bottom and the \u0000rst branch that has a true condition is the ONLY branch that gets executed.",
                                2))

                        }
                        else if (QuestionNumber == 3){
                            var Answers = ArrayList<String>()
                            Answers.add("When none of the branches above it have been executed")
                            Answers.add("When one of the branches above it has been executed")
                            Answers.add("It will get executed at the end no matter what happens")
                            lessonQuizQuestions.add(QuestionsModel("When does the \"else\" branch get executed?",Answers,"The \"else\" branch serves as a catch all and will execute if none of the branches\n" +
                                    "above it was able to execute (due to their conditions being false).",
                                0))
                        }
                    }
                    val lessonQuiz = courseLessonQuizDetailsModel("Lesson 12 Quiz",lessonQuizQuestions,false)
                    val ChallengeDetails = courseLessonChellengeDetailsModel("Lesson 12 Challenge",null,null,false)
                    val courseDetails =  courseLessonDetailsModel("Swift If Statements","https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%2012%2FLesson%2012%20Swift%20If%20Statements.mp4?alt=media&token=5d07d8cc-8087-4912-b5e3-03418e3058ae",
                        false,LessonDetailsDescription,null)
                    setLessonsOFModules("iosFC1M1L12","Swift If Statements",12,1,3,0,0,false,false,false,courseDetails,lessonQuiz,ChallengeDetails,null)

                }
                else if (index == 13){
                    val lessonQuiz = courseLessonQuizDetailsModel()
                    val ChallengeDetails = courseLessonChellengeDetailsModel()
                    val courseDetails =  courseLessonDetailsModel("Wrap Up Challenge","https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%2013%2FLesson%2013%20Wrap%20Up%20Challenge.mp4?alt=media&token=f56bfcd3-6af9-4e6d-888e-a36d644ae55f",
                        false,LessonDetailsDescription,null)
                    setLessonsOFModules("iosFC1M1L13","Wrap Up Challenge",13,1,0,0,0,false,false,false,courseDetails,lessonQuiz,ChallengeDetails,null)
                }
            }
            setModulesOFCourse("iosFC1M1","Module 1: War Card Game","War Card Game",null,13,15,10,Lessons,0,false,false,true,null)
            setModulesOFCourse("iosFC1M2","Module 2: Recipe List App","Recipe List App",null,12,17,9,ArrayList<CourseLessonsModel>(),0,false,false,false,null)
            setModulesOFCourse("iosFC1M3","Module 3: GitHub","GitHub",null,7,10,6,ArrayList<CourseLessonsModel>(),0,false,false,false,null)
            setModulesOFCourse("iosFC1M4","Module 4: Recipe App","Recipe App",null,20,25,18,ArrayList<CourseLessonsModel>(),0,false,false,false,null)
            setModulesOFCourse("iosFC1M5","Module 5: Learning App","Learning App",null,28,28,25,ArrayList<CourseLessonsModel>(),0,false,false,false,null)
            setModulesOFCourse("iosFC1M6","Module 6: City Sights App","City Sights App",null,20,23,16,ArrayList<CourseLessonsModel>(),0,false,false,false,null)
        }
    }

    private fun setModulesOFCourse(
        id:String,name:String,title:String,Image:Int?,lessons: Int,videos:Int?,task:Int?,lessonDetails:ArrayList<CourseLessonsModel>,
        HowManyLessonsFinished:Int,finished:Boolean,process:Boolean?,enabled:Boolean?,
        description:ArrayList<String>?)
    {
        modules.add(
            courseModulesModel(
                id,
                name,
                title,
                Image,
                lessons,
                videos,
                task,
                lessonDetails,
                HowManyLessonsFinished,
                finished,
                process,
                enabled,
                description
            )
        )
    }

    private fun setLessonsOFModules(
        id: String,
        name: String,
        number:Int,
        videosCount:Int,
        quizCount:Int,
        assignmentCount:Int,
        finishedCount:Int,
        finished: Boolean,
        Process:Boolean,
        enabled:Boolean,
        lessonDetails: courseLessonDetailsModel,
        lessonQuizDetails: courseLessonQuizDetailsModel,
        lessonChallengeDetails: courseLessonChellengeDetailsModel,
        description: ArrayList<String>?){
        Lessons.add(
            CourseLessonsModel(
                id,
                name,
                number,
                videosCount,
                quizCount,
                assignmentCount,
                finishedCount,
                finished,
                Process,
                enabled,
                lessonDetails,
                lessonQuizDetails,
                lessonChallengeDetails,
                description
            ))
    }

}