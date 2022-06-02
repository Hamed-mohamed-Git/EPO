package com.example.peodemo.DashBoard.courseGropie.lessonDetailsPage.challengePage

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.CourseDetails.IntroductionVideo
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseChallengeDetails.courseLessonChellengeDetailsModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials.courseLessonQuizDetailsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_challenge_page.*

class challengePageActivity : AppCompatActivity() {

    val emojiArray = listOf(R.drawable.happyfancy,R.drawable.happynormy,R.drawable.happysmilefancy,R.drawable.happyteethfancy,
        R.drawable.happywithsmilenormy,R.drawable.justnormy)
    private var challengeRoot = ""

    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val fireStoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val currentUserCourseDocRef: CollectionReference
        get() = fireStoreInstance.collection("Users/${mAuth.currentUser?.uid.toString()}/My Courses")

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_page)

        challengeRoot = intent.getStringExtra("questionsRoot") as String

        val randomImagesIndex = emojiArray.random()
        challengeEmoji.setImageResource(randomImagesIndex)
        val window = this.window
        window?.statusBarColor = this.resources.getColor(R.color.challengePageStatusBar)
        window?.navigationBarColor = this.resources.getColor(R.color.challengePageStatusBar)

        getUserLessonChallengeInformation {
            taskDoneText.text = "1/2 Done!"
            challenge_progress_bar.setProgress(1,true)
            currentLessonChallenge.text = "Welcome to ${it.name!!}. The solution can be found in the Resources lesson."
            if (it.challengeInfo!!.uriVideo != null){
                doneButtonChallenge.visibility = View.GONE
                downloadButtonChallenge.visibility = View.GONE
                watchButtonChallenge.visibility = View.VISIBLE
                textUnderShape.text = "May I tell you something yeah ''I whispered''. You can make it don't worry just watch the challenge"
                val uri = it.challengeInfo.uriVideo!!
                watchButtonChallenge.setOnClickListener {
                    challenge_progress_bar.setProgress(2,true)
                    taskDoneText.text = "2/2 Done!"
                    doneButtonChallenge.visibility = View.VISIBLE
                    watchButtonChallenge.visibility = View.GONE
                    val intent  = Intent(this, IntroductionVideo::class.java)
                    intent.putExtra("url",uri)
                    startActivity(intent)
                    this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
                }
            }else if (it.challengeInfo.pdfUri != null){
                doneButtonChallenge.visibility = View.GONE
                watchButtonChallenge.visibility = View.GONE
                downloadButtonChallenge.visibility = View.VISIBLE
                textUnderShape.text = "May I tell you something yeah ''I whispered''. You can make it don't worry just downLoad the document to access the challenge"
                val uri = it.challengeInfo.pdfUri
                downloadButtonChallenge.setOnClickListener {
                    downloadButtonChallenge.visibility = View.GONE
                    doneButtonChallenge.visibility = View.VISIBLE
                    challenge_progress_bar.setProgress(2,true)
                    taskDoneText.text = "2/2 Done!"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data =  Uri.parse(uri)
                    startActivity(intent)
                    this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
                }
            }
            val challengeInfo = it.challengeInfo
            val name = it.name
            doneButtonChallenge.setOnClickListener {
                val mutableChallenge = mutableMapOf<String,Any?>()
                mutableChallenge["challengeInfo"] = challengeInfo
                mutableChallenge["finished"] = true
                mutableChallenge["name"] = name
                currentUserCourseDocRef.document("$challengeRoot/lesson Details/challenge info").update(mutableChallenge)
                finish()
            }
        }
    }



    private fun getUserLessonChallengeInformation(onComplete:(courseLessonChellengeDetailsModel) -> Unit){
        currentUserCourseDocRef.document("$challengeRoot/lesson Details/challenge info").get().addOnSuccessListener {
            onComplete(it.toObject(courseLessonChellengeDetailsModel::class.java)!!)
        }
    }
}