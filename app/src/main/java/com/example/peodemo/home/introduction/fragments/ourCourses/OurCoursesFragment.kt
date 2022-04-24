package com.example.peodemo.home.introduction.fragments.ourCourses

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.CourseDetails.courseDetailModel
import com.example.peodemo.home.introduction.fragments.ourCourses.CourseDetails.courseDetails
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CourseLessonsModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CoursesModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonDetailsModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseModulesModel
import com.example.peodemo.home.introduction.fragments.ourCourses.categories.categoriesViewModel
import com.example.peodemo.home.introduction.fragments.ourCourses.categories.gategoriesModel

import com.example.peodemo.home.introduction.fragments.ourCourses.topCourses.topCoursesModel
import com.example.peodemo.home.introduction.fragments.ourCourses.topCourses.topCoursesViewModel
import com.example.peodemo.home.introduction.fragments.ourCourses.checkout.CheckOutProcessingModel
import kotlinx.android.synthetic.main.fragment_our_courses.*
import java.util.ArrayList


class ourCoursesFragment : Fragment(), categoriesViewModel.OnItemClickListener,
    topCoursesViewModel.OnItemClickListener {
    //assigning a tapList array property to store the recycle view in it
    private val  categoryTabList = ArrayList<gategoriesModel>()
    //assigning a courseDetailsList array property to store the recycle view in i

    private lateinit var CategoriesType: String

    //assigning a adapter property to store the tapView instance in ti
    private val adapter = categoriesViewModel(categoryTabList,this)

    //assigning a tapList array property to store the recycle view in it
    private val  topCoursesTabList = ArrayList<topCoursesModel>()
    //assigning a adapter property to store the tapView instance in ti
    private val topCoursesAdapter = topCoursesViewModel(topCoursesTabList,this)

    private lateinit var courseCategory:String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_our_courses, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SetDataInOurCoursesRecycleView()
    }

    private fun addCourses(name: String,price:String, count: String, image: Int) {
        topCoursesTabList.add(topCoursesModel(name,price,count,image))

    }

    @SuppressLint("WrongConstant")
    override fun onItemClick(position: Int) {
        when (position){
            // if the user tap on the first item
            0 ->{
                CategoriesType = "IOS"
                //call this item and put it in tap property
                var taps = categoryTabList[position]
                taps.pressed = 1
                //And set 0 to other items
                categoryTabList[1].pressed = 0
                categoryTabList[2].pressed = 0
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(1)
                adapter.notifyItemChanged(2)
                topCoursesTabList.clear()
                addCourses("Foundation","199.99","60",R.drawable.foundation)
                addCourses("Design app","49.99","15",R.drawable.iosdesigingcourse)
                addCourses("Database","29.99","12",R.drawable.data)
                TopCoursesRecycleViewCourseIntroduction.layoutManager = LinearLayoutManager(this.activity, LinearLayout.HORIZONTAL,false)
                TopCoursesRecycleViewCourseIntroduction.adapter = topCoursesAdapter

                courseCategory = taps.name
            }
            // if the user tap on the second item
            1 ->{
                CategoriesType = "Android"
                //call this item and put it in tap property
                var taps = categoryTabList[position]
                //and set a last var in this item to 1, if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                categoryTabList[0].pressed = 0
                categoryTabList[2].pressed = 0
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(2)
                topCoursesTabList.clear()
                addCourses("Foundation","299.99","180",R.drawable.androidfoundationcourse)
                addCourses("Design","149.99","25",R.drawable.androidxmlcourse)
                addCourses("Android JC ","399.99","130",R.drawable.androiddesigncourse)
                addCourses("Pattern","99.99","40",R.drawable.androidpatterncourse)
                TopCoursesRecycleViewCourseIntroduction.layoutManager = LinearLayoutManager(this.activity, LinearLayout.HORIZONTAL,false)
                TopCoursesRecycleViewCourseIntroduction.adapter = topCoursesAdapter

                courseCategory = taps.name
            }
            // if the user tap on the third item
            2 ->{
                CategoriesType = "Data Structure"
                //call this item and put it in tap property
                var taps = categoryTabList[position]
                //and set a last var in this item to 1, if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                categoryTabList[0].pressed = 0
                categoryTabList[1].pressed = 0
                //And change the item in the tap array
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(1)
                topCoursesTabList.clear()
                addCourses("Part 1","99.99","35",R.drawable.datastructurepart1course)
                addCourses("Part 2","199.99","10",R.drawable.datastructurepart2course)
                addCourses("Algorithms","149.99","32",R.drawable.algorithmscourse)
                TopCoursesRecycleViewCourseIntroduction.layoutManager = LinearLayoutManager(this.activity, LinearLayout.HORIZONTAL,false)
                TopCoursesRecycleViewCourseIntroduction.adapter = topCoursesAdapter
                courseCategory = taps.name
            }
        }
    }

    override fun onPause() {
        super.onPause()
        categoryTabList.clear()
        topCoursesTabList.clear()
    }


    @SuppressLint("UseRequireInsteadOfGet")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCoursesItemClick(position: Int) {
        if (CategoriesType == "IOS"){

            when(position){
                0 -> {
                    val courseDetailsList = courseDetailModel(
                        CoursesModel(
                            "Foundation",
                            "iosFC1",
                            R.drawable.foundation,
                            0f,
                            activity!!.getColor(R.color.theMainIOSFoundationCourse),
                            30
                            ,0,
                            false,
                            6,
                            null
                        ),
                        CheckOutProcessingModel("iosFC1",199.99f,300,10,4)
                        ,"Foundation"
                        ,"Chris Ching"
                        ,"199.99"
                        ,"https://firebasestorage.googleapis.com/v0/b/messrenger.appspot.com/o/Courses%20Videos%2FTHE%20introduction%20of%20courses%2FIOS%20development%2FiOS%20Foundations%20(SwiftUI).mp4?alt=media&token=ee3c2084-b743-4a75-a433-e78c996cf0ea",
                        "5",
                        "60",
                        "Chris here. I want to give you a big, warm welcome to the SwiftUI iOS Foundations course!",
                        "In this course, you'll learn the basics of iOS app development using Apple's newest SwiftUI framework.",
                        "By the end, you'll be able to build multi-view apps powered by data from APIs or your own JSON feeds.  ",
                        "(Don't worry if these terms are foreign to you; they are different ways of providing data to the app and you'll learn all about them in the course)",
                        activity!!.getColor(R.color.theMainIOSFoundationCourse),
                        activity!!.getColor(R.color.theSubIOSFoundationCourse),
                        R.drawable.foundation
                    )
                    val intent = Intent(this.activity,courseDetails::class.java)
                    intent.putExtra("data",courseDetailsList)
                    startActivity(intent)

                }
                1 ->{
                    val courseDetailsList = courseDetailModel(
                        null,
                        CheckOutProcessingModel("iosDC2",49.99f,150,6,3)
                        ,"Design app"
                        ,"Chris Ching"
                        ,"49.99"
                        ,"https://firebasestorage.googleapis.com/v0/b/messrenger.appspot.com/o/Courses%20Videos%2FTHE%20introduction%20of%20courses%2FIOS%20development%2FDesign%20Course.mp4?alt=media&token=7e7e4709-d6fc-4899-b41b-b06f309a0477",
                        "1",
                        "15",
                        "Chris here. I want to give you a big, warm welcome to the SwiftUI iOS Design app course!",
                        "In this module, I’ll show you that there is a lot of styling that you can do using just code!",
                        null,
                        null,
                        activity!!.getColor(R.color.theMainIOSDesignCourse),
                        activity!!.getColor(R.color.theSubIOSDesignCourse),
                        R.drawable.iosdesigingcourse
                    )
                    val intent = Intent(this.activity,courseDetails::class.java)
                    intent.putExtra("data",courseDetailsList)
                    startActivity(intent)

                }
                2 ->{
                    val courseDetailsList = courseDetailModel(
                        null,
                        CheckOutProcessingModel("iosDAC3",29.99f,30,11,3)
                        ,"Database"
                        ,"Chris Ching"
                        ,"29.99"
                        ,"https://firebasestorage.googleapis.com/v0/b/messrenger.appspot.com/o/Courses%20Videos%2FTHE%20introduction%20of%20courses%2FIOS%20development%2FiOS%20Databases%20(SwiftUI).mp4?alt=media&token=a3fb83b6-2656-4221-9036-efd501c3a06f",
                        "3.5",
                        "12",
                        "Chris here. First of all, congratulations on finishing the iOS Foundations course. That's a huge accomplishment and you're well onto your way to build deeper iOS apps.",
                        "That's where this iOS Databases course comes in.You'll learn how to use CoreData and Firestore with your SwiftUI app which enables you to do all sorts of neat things in your app related to data.",
                        "By the end of this course:\n" +
                                "You'll learn how to create a user account system so you can log in and out of your app.\n",
                        "You'll learn how to store and retrieve data on a remote server\n" +
                                "You'll learn how to store and retrieve data on the user's device\n" +
                                "These three skills will enable a variety of new scenarios for your apps as you'll see in the next lesson!\n",
                        activity!!.getColor(R.color.theMainIOSDatabaseCourse),
                        activity!!.getColor(R.color.theSubIOSDatabaseCourse),
                        R.drawable.data
                    )
                    val intent = Intent(this.activity,courseDetails::class.java)
                    intent.putExtra("data",courseDetailsList)
                    startActivity(intent)
                }
            }
        }else if (CategoriesType == "Android"){
            when(position){
                0 -> {
                    val courseDetailsList = courseDetailModel(
                        null,
                        CheckOutProcessingModel("andF0",299.99f,360,34,8)
                        ,"Foundation"
                        ,"John Tripho"
                        ,"299.99"
                        ,"https://firebasestorage.googleapis.com/v0/b/messrenger.appspot.com/o/Courses%20Videos%2FTHE%20introduction%20of%20courses%2FAndroid%20Development%2Ffoundation.mp4?alt=media&token=48dd253d-c49b-4214-819b-d93668541c15",
                        "10",
                        "180",
                        "This Specialization enables learners to successfully apply core Java programming languages features & software patterns needed to develop maintainable mobile apps comprised of core Android components, as well as fundamental Java I/O & persistence mechanisms. ",
                        "The Capstone project will integrate the material from throughout the Specialization to exercise and assess the ability of learners to create an interesting Android app by applying knowledge and skills learned in previous MOOCs, including Java programming features, Android Studio tools, Android Activity components, Material Design, file I/O and data persistence, unit testing, and software patterns. ",
                        "The project itself will be similar in design goals to previous assignments, however it will provide less of the skeleton code than earlier MOOCs provide to enable more creativity to learners and greater opportunity for learners to customize the app.",
                        null,
                        activity!!.getColor(R.color.theMainAndroidFoundationCourse),
                        activity!!.getColor(R.color.theSubAndroidFoundationCourse),
                        R.drawable.androidfoundationcourse
                    )
                    val intent = Intent(this.activity,courseDetails::class.java)
                    intent.putExtra("data",courseDetailsList)
                    startActivity(intent)
                }
                1 -> {
                    val courseDetailsList = courseDetailModel(
                        null,
                        CheckOutProcessingModel("andD1",149.99f,150,27,2)
                        ,"Design Course"
                        ,"Jesse Showalter "
                        ,"149.99"
                        ,"https://firebasestorage.googleapis.com/v0/b/messrenger.appspot.com/o/Courses%20Videos%2FTHE%20introduction%20of%20courses%2FAndroid%20Development%2FDesgin.mp4?alt=media&token=23690085-c906-4e03-9152-18fd51d11a14",
                        "9.5",
                        "25",
                        "You’ll learn how to create a digital user experience that is ready to be handed off for development. You’ll start by building familiarity and fluency with design research fundamentals to identify the user and the solutions they need. ",
                        "You’ll then synthesize your research, and use design sprints to take an idea from concept to low-fidelity prototype. ",
                        " Finally, you’ll learn how to turn your low-fidelity prototype into a high-fidelity design, and improve its performance based on data. ",
                        "Along the way, you’ll complete projects that can be incorporated into a UX portfolio at the end of the program in order to showcase your capabilities to future employers.",
                        activity!!.getColor(R.color.theMainAndroidDesignCourse),
                        activity!!.getColor(R.color.theSubAndroidDesignCourse),
                        R.drawable.androidxmlcourse
                    )
                    val intent = Intent(this.activity,courseDetails::class.java)
                    intent.putExtra("data",courseDetailsList)
                    startActivity(intent)
                }
                2 -> {
                    val courseDetailsList = courseDetailModel(
                        null,
                        CheckOutProcessingModel("andJP2",399.99f,400,21,7)
                        ,"Jetpack Compose"
                        ,"Stevdza-san"
                        ,"399.99"
                        ,"https://firebasestorage.googleapis.com/v0/b/messrenger.appspot.com/o/Courses%20Videos%2FTHE%20introduction%20of%20courses%2FAndroid%20Development%2FIntroduction%20-%20Jetpack%20Compose.mp4?alt=media&token=a2843613-ba12-4cf7-b0b5-c164845c503b",
                        "7",
                        "130",
                        "Build modern Android apps with Jetpack Compose and explore the integrations with MVVM, Coroutines, ViewModel, LiveData, Retrofit and Navigation.",
                        "Tired of creating Android UIs and layouts in XML? Jetpack Compose comes to the rescue!",
                        "Jetpack Compose is Android’s modern toolkit for building native UI. It simplifies and accelerates UI development on Android. Compose allows to quickly bring your app to life with less code, powerful tools, and intuitive Kotlin APIs.",
                        "This course will teach you the basics and fundamental concepts behind Compose while creating tons of cool modern layouts and designs. We will dive deep in the concepts behind Compose as we will build awesome Android UI widgets",
                        activity!!.getColor(R.color.theMainAndroidJetpackComposeCourse),
                        activity!!.getColor(R.color.theSubAndroidJetpackComposeCourse),
                        R.drawable.androiddesigncourse
                    )
                    val intent = Intent(this.activity,courseDetails::class.java)
                    intent.putExtra("data",courseDetailsList)
                    startActivity(intent)

                }
                3 -> {
                    val courseDetailsList = courseDetailModel(
                        null,
                        CheckOutProcessingModel("andP3",99.99f,120,13,4)
                        ,"Pattern"
                        ,"Mosh Hamedani"
                        ,"99.99"
                        ,"https://firebasestorage.googleapis.com/v0/b/messrenger.appspot.com/o/Courses%20Videos%2FTHE%20introduction%20of%20courses%2FAndroid%20Development%2Fpattern%20Introduction.mp4?alt=media&token=e02cada6-19b8-456c-91c0-6a2e2b468236",
                        "1.4",
                        "40",
                        "This Course is about using the techniques of pattern making, also known as pattern drafting, in fashion design, to create patterns that will then be used to cut fabric and ultimately be sewn into garments.",
                        "Methods and techniques studied in major fashion universities are demonstrated in a clear and simple visual demos so that anyone interested in learning about fashion design can, and will learn the foundational principles of fashion design. ",
                        "New terminology is clearly explained and applied to the subject matter. As a fashion designer, pattern making is essential in learning how garments are created and put together",
                        "This course will empower you to be a better fashion designer, give you more creative options, establish the foundations for a successful fashion brand or elevate your existing one.",
                        activity!!.getColor(R.color.theMainAndroidPatternCourse),
                        activity!!.getColor(R.color.theSubAndroidPatternCourse),
                        R.drawable.androidpatterncourse
                    )
                    val intent = Intent(this.activity,courseDetails::class.java)
                    intent.putExtra("data",courseDetailsList)
                    startActivity(intent)
                }
            }
        }else if (CategoriesType == "Data Structure"){
            when(position){
                0 -> {
                    val courseDetailsList = courseDetailModel(
                        null,
                        CheckOutProcessingModel("dsP10",99.99f,30,18,5)
                        ,"Part 1"
                        ,"Mosh Hamedani"
                        ,"99.99"
                        ,"https://firebasestorage.googleapis.com/v0/b/messrenger.appspot.com/o/Courses%20Videos%2FTHE%20introduction%20of%20courses%2FData%20structure%20and%20algorithms%2Fpart%201.mp4?alt=media&token=fb0fb242-4a0d-437d-a9f6-5d5f8db890cc",
                        "3",
                        "35",
                        "Computer science legend Donald Knuth once said “I don’t understand things unless I try to program them.” We also believe that the best way to learn an algorithm is to program it.",
                        "However, many excellent books and online courses on algorithms, that excel in introducing algorithmic ideas, have not yet succeeded in teaching you how to implement algorithms, the crucial computer science skill that you have to master at your next job interview. We tried to fill this gap by forming a diverse team of instructors that includes world-leading experts in theoretical and applied algorithms at UCSD (Daniel Kane, Alexander Kulikov, and Pavel Pevzner) and a former software engineer at Google (Neil Rhodes). ",
                        "This unique combination of skills makes this Specialization different from other excellent MOOCs on algorithms that are all developed by theoretical computer scientists. While these MOOCs focus on theory, our Specialization is a mix of algorithmic theory/practice/applications with software engineering. You will learn algorithms by implementing nearly 100 coding problems in a programming language of your choice.",
                        "To the best of knowledge, no other online course in Algorithms comes close to offering you a wealth of programming challenges (and puzzles!) that you may face at your next job interview.",
                        activity!!.getColor(R.color.theMainDataStructurePart1Course),
                        activity!!.getColor(R.color.theSubDataStructurePart1Course),
                        R.drawable.datastructurepart1course
                    )
                    val intent = Intent(this.activity,courseDetails::class.java)
                    intent.putExtra("data",courseDetailsList)
                    startActivity(intent)
                }
                1 -> {
                    val courseDetailsList = courseDetailModel(
                        null,
                        CheckOutProcessingModel("dsP21",199.99f,20,5,7)
                        ,"Part 2"
                        ,"Mosh Hamedani"
                        ,"199.99"
                        ,"https://firebasestorage.googleapis.com/v0/b/messrenger.appspot.com/o/Courses%20Videos%2FTHE%20introduction%20of%20courses%2FData%20structure%20and%20algorithms%2Fpart%202.mp4?alt=media&token=ba461aab-d45c-4892-8cf5-a54db492e6dc",
                        "1",
                        "10",
                        "The key to successful technical interviews is practice. In this course, you'll review common Java data structures and algorithms. You'll learn how to explain your solutions to technical problems.",
                        "This course is ideal for you if you've never taken a course in data structures or algorithms. It's also a good refresher if you have some experience with these topics.",
                        "You'll learn the concepts through video tutorials.",
                        "You'll watch experienced engineers review supplementary examples and discuss different interview approaches.",
                        activity!!.getColor(R.color.theMainDataStructurePart2Course),
                        activity!!.getColor(R.color.theSubDataStructurePart2Course),
                        R.drawable.datastructurepart2course
                    )
                    val intent = Intent(this.activity,courseDetails::class.java)
                    intent.putExtra("data",courseDetailsList)
                    startActivity(intent)
                }
                2 -> {
                    val courseDetailsList = courseDetailModel(
                        null,
                        CheckOutProcessingModel("dsA2",149.99f,100,14,4)
                        ,"Algorithms"
                        ,"Mosh Hamedani"
                        ,"149.99"
                        ,"https://firebasestorage.googleapis.com/v0/b/messrenger.appspot.com/o/Courses%20Videos%2FTHE%20introduction%20of%20courses%2FData%20structure%20and%20algorithms%2FAlgorithm.mp4?alt=media&token=caeeb9fd-e068-4909-975b-5ac56a66814a",
                        "1",
                        "32",
                        "This course covers the essential information that every serious programmer needs to know about algorithms and data structures, with emphasis on applications and scientific performance analysis of Java implementations.",
                        " Part I covers elementary data structures, sorting, and searching algorithms. Part II focuses on graph- and string-processing algorithms.",
                        "All the features of this course are available for free.  It does not offer a certificate upon completion.",
                        null,
                        activity!!.getColor(R.color.theMainDataStructureAlgorithmsCourse),
                        activity!!.getColor(R.color.theSubAndroidAlgorithmsCourse),
                        R.drawable.algorithmscourse
                    )
                    val intent = Intent(this.activity,courseDetails::class.java)
                    intent.putExtra("data",courseDetailsList)
                    startActivity(intent)
                }
            }
        }
    }


    @SuppressLint("WrongConstant")
    override fun onResume() {
        super.onResume()
        categoryTabList.clear()
        topCoursesTabList.clear()
        SetDataInOurCoursesRecycleView()
    }

    @SuppressLint("WrongConstant")
    private fun SetDataInOurCoursesRecycleView(){
        CategoriesType = "IOS"
        categoryTabList.add(gategoriesModel("Ios Development",R.drawable.ios,1))
        categoryTabList.add(gategoriesModel("Android Development",R.drawable.androiddevelopment,0))
        categoryTabList.add(gategoriesModel("Data Structure & Algorithm",R.drawable.datastructure,0))

        categoriesRecycleViewCourseIntroduction.layoutManager = LinearLayoutManager(this.activity, LinearLayout.HORIZONTAL,false)
        categoriesRecycleViewCourseIntroduction.adapter = adapter


        addCourses("Foundation","199.99","60",R.drawable.foundation)
        addCourses("Design app","49.99","15",R.drawable.iosdesigingcourse)
        addCourses("Database","29.99","12",R.drawable.data)
        TopCoursesRecycleViewCourseIntroduction.layoutManager = LinearLayoutManager(this.activity, LinearLayout.HORIZONTAL,false)
        TopCoursesRecycleViewCourseIntroduction.adapter = topCoursesAdapter
    }



}