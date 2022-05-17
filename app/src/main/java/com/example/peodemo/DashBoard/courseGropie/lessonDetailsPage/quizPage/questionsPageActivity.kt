package com.example.peodemo.DashBoard.courseGropie.lessonDetailsPage.quizPage

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CourseLessonsModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials.QuestionsModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials.courseLessonQuizDetailsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_questions_page.*
import kotlinx.android.synthetic.main.quiz_alert_dialog.view.*

class questionsPageActivity : AppCompatActivity() {
    private var question = ""
    private var correctIndex = 0
    private var currentIndex = 0
    private var correctQuestionCount = 0
    private var wrongQuestionCount = 0


    private var seconds = 60

    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val fireStoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val currentUserCourseDocRef: CollectionReference
        get() = fireStoreInstance.collection("Users/${mAuth.currentUser?.uid.toString()}/My Courses")

    private lateinit var timer: CountDownTimer


    @SuppressLint("ResourceAsColor", "ResourceType")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions_page)
        val window = this.window
        window?.statusBarColor = this.resources.getColor(R.color.questionPageStatusBarColor)
        window.navigationBarColor = this.resources.getColor(R.color.questionPageStatusBarColor)
        question = intent.getStringExtra("questionRoot") as String
        setQuizDataIntoView(1)

        timer =  object : CountDownTimer(60000, 1000) {
            // Callback function, fired on regular interval
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                seconds--
                questionTimeText.text = "$seconds Seconds"
            }

            override fun onFinish() {
                seconds = 60
                checkCorrect(currentIndex,correctIndex)
            }


        }.start()

        firstAnswer.setOnClickListener {
            correctIndex = 1

            firstAnswer.setBackgroundResource(R.drawable.select_answer_layout_background)
            firstAnswerText.setTextColor(this.getColor(R.color.white))

            secondAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            secondAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))

            thirdAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            thirdAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))

            fouthAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            fourthAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))

        }
        secondAnswer.setOnClickListener {
            correctIndex = 2

            secondAnswer.setBackgroundResource(R.drawable.select_answer_layout_background)
            secondAnswerText.setTextColor(this.getColor(R.color.white))

            firstAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            firstAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))

            thirdAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            thirdAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))

            fouthAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            fourthAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))

        }
        thirdAnswer.setOnClickListener {
            correctIndex = 3

            thirdAnswer.setBackgroundResource(R.drawable.select_answer_layout_background)
            thirdAnswerText.setTextColor(this.getColor(R.color.white))

            secondAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            secondAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))

            firstAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            firstAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))

            fouthAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            fourthAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))
        }
        fouthAnswer.setOnClickListener {
            correctIndex = 4

            fouthAnswer.setBackgroundResource(R.drawable.select_answer_layout_background)
            fourthAnswerText.setTextColor(this.getColor(R.color.white))

            secondAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            secondAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))

            thirdAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            thirdAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))

            firstAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            firstAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))
        }
        setQuizDataIntoView(currentIndex)
        quizCloseButton.setOnClickListener {
            finish()
        }
        confirmButton.setOnClickListener {
            checkCorrect(currentIndex,correctIndex)
            timer.cancel()
            seconds = 60
        }
        nextQuestionButton.setOnClickListener {
            firstAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            firstAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))

            secondAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            secondAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))

            thirdAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            thirdAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))

            fouthAnswer.setBackgroundResource(R.drawable.answer_layout_background)
            fourthAnswerText.setTextColor(this.getColor(R.color.UncheckedSwitchTextColor))
            if (currentIndex < 2){
                currentIndex++
                setQuizDataIntoView(currentIndex)
                correctLayout.visibility = View.GONE
                confirmButton.visibility = View.VISIBLE
                timer.start()
            }else{
                timer.cancel()
                getUserLessonQuizInformation {
                    if (correctQuestionCount >= 2){
                        val quizHashMap = mutableMapOf<String, Any?>()
                        quizHashMap["finished"] = true
                        quizHashMap["name"] = it.name
                        quizHashMap["questions"] = it.questions
                        currentUserCourseDocRef.document("$question/lesson Details/Quiz info").update(quizHashMap)
                        getUserLessonInformation {
                            val lessonDataByHashMap = mutableMapOf<String, Any?>()
                            lessonDataByHashMap["assignmentCount"] = it.assignmentCount
                            lessonDataByHashMap["challengeFinishedCount"] = it.challengeFinishedCount
                            lessonDataByHashMap["description"] = it.description
                            lessonDataByHashMap["enabled"] = it.enabled
                            lessonDataByHashMap["finishCount"] = it.finishCount
                            lessonDataByHashMap["finished"] = it.finished
                            lessonDataByHashMap["finishedCount"] = it.finishedCount
                            lessonDataByHashMap["id"] = it.id
                            lessonDataByHashMap["lessonChallengeDetails"] = it.lessonChallengeDetails
                            lessonDataByHashMap["lessonDetails"] = it.lessonDetails
                            lessonDataByHashMap["lessonQUIZDetails"] = it.lessonQUIZDetails
                            lessonDataByHashMap["minutes"] = it.minutes
                            lessonDataByHashMap["name"] = it.name
                            lessonDataByHashMap["number"] = it.number
                            lessonDataByHashMap["process"] = it.Process
                            lessonDataByHashMap["quizCount"] = it.quizCount
                            lessonDataByHashMap["quizFinishedCount"] = 1
                            lessonDataByHashMap["resourceFinishedCount"] = it.resourceFinishedCount
                            lessonDataByHashMap["videoFinishedCount"] = it.videoFinishedCount
                            lessonDataByHashMap["videosCount"] = it.videosCount
                            currentUserCourseDocRef.document(question).update(lessonDataByHashMap)
                        }
                    }
                }
                quizAlertDialog(correctQuestionCount,wrongQuestionCount)
            }

        }
    }



    private fun getUserLessonQuizInformation(onComplete:(courseLessonQuizDetailsModel) -> Unit){
        currentUserCourseDocRef.document("$question/lesson Details/Quiz info").get().addOnSuccessListener {
                onComplete(it.toObject(courseLessonQuizDetailsModel::class.java)!!)
            }
    }

    private fun getUserLessonInformation(onComplete:(CourseLessonsModel) -> Unit){
        currentUserCourseDocRef.document(question).get().addOnSuccessListener {
            onComplete(it.toObject(CourseLessonsModel::class.java)!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun setQuizDataIntoView(Index : Int){
        getUserLessonQuizInformation {
            val question = it.questions!![Index]
            questionNumber.text = "Question ${Index + 1} / 3"
            correctAnswer.text = question.correctAnswer!!.toString()
            questionText.text = question.question!!.toString()
            val answers = question.answers!!

            if (answers.count() < 3){
                val firstAnswer = answers[0]
                val secondAnswer = answers[1]

                firstAnswerText.text = firstAnswer
                secondAnswerText.text = secondAnswer

                thirdAnswer.visibility = View.GONE
                fouthAnswer.visibility = View.GONE
            }else if (answers.count() < 4){
                val firstAnswer = answers[0]
                val secondAnswer = answers[1]
                val thirdAnswer = answers[2]

                firstAnswerText.text = firstAnswer
                secondAnswerText.text = secondAnswer
                thirdAnswerText.text = thirdAnswer

                fouthAnswer.visibility = View.GONE
            }else if (answers.count() == 4){
                val firstAnswer = answers[0]
                val secondAnswer = answers[1]
                val thirdAnswer = answers[2]
                val fourthAnswer = answers[3]

                firstAnswerText.text = firstAnswer
                secondAnswerText.text = secondAnswer
                thirdAnswerText.text = thirdAnswer
                fourthAnswerText.text = fourthAnswer

            }
        }
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkCorrect(Index: Int , CorrectIndex:Int){
        getUserLessonQuizInformation {
            val questions =it.questions!![Index]
            if ( questions.correctIndex!! + 1 == CorrectIndex){
                if (correctQuestionCount <= 3){
                    correctQuestionCount++
                }
                correctLayout.setBackgroundResource(R.drawable.correct_answer_layout_background)
                if(CorrectIndex == 1){
                    secondAnswer.setBackgroundResource(R.drawable.correct_answer_layout_background)
                    secondAnswerText.setTextColor(this.getColor(R.color.white))

                }else if(CorrectIndex == 2){
                    secondAnswer.setBackgroundResource(R.drawable.correct_answer_layout_background)
                    secondAnswerText.setTextColor(this.getColor(R.color.white))

                }
                else if(CorrectIndex == 3){
                    thirdAnswer.setBackgroundResource(R.drawable.correct_answer_layout_background)
                    thirdAnswerText.setTextColor(this.getColor(R.color.white))

                }else if(CorrectIndex == 4){
                    fouthAnswer.setBackgroundResource(R.drawable.correct_answer_layout_background)
                    fourthAnswerText.setTextColor(this.getColor(R.color.white))
                }
            }else{
                if (wrongQuestionCount <= 3 ){
                    wrongQuestionCount++
                }
                correctLayout.setBackgroundResource(R.drawable.wrong_answer_frame_layout_background)
                if(CorrectIndex == 1){
                    firstAnswer.setBackgroundResource(R.drawable.wrong_answer_frame_layout_background)
                    firstAnswerText.setTextColor(this.getColor(R.color.white))
                }else if(CorrectIndex == 2){
                    secondAnswer.setBackgroundResource(R.drawable.wrong_answer_frame_layout_background)
                    secondAnswerText.setTextColor(this.getColor(R.color.white))
                }
                else if(CorrectIndex == 3){
                    thirdAnswer.setBackgroundResource(R.drawable.wrong_answer_frame_layout_background)
                    thirdAnswerText.setTextColor(this.getColor(R.color.white))

                }else if(CorrectIndex == 4){
                    fouthAnswer.setBackgroundResource(R.drawable.wrong_answer_frame_layout_background)
                    fourthAnswerText.setTextColor(this.getColor(R.color.white))
                }
            }
            correctIndex = 0
            confirmButton.visibility = View.GONE
            correctLayout.visibility = View.VISIBLE
            lessonPageProgressBar.setProgress(currentIndex + 1,true)
        }
    }

    private fun quizAlertDialog(correctAnswerCount:Int,wrongAnswerCount:Int){
        val builder = AlertDialog.Builder(this)
        // Get the layout inflater
        val inflater = this.layoutInflater
        val inflation = inflater.inflate(R.layout.quiz_alert_dialog, null)

        inflation.questionCorrectText.text = correctAnswerCount.toString()
        inflation.questionWinText.text = correctAnswerCount.toString()
        inflation.questionWrongText.text = wrongAnswerCount.toString()
        inflation.quizAlertDialogCircularProgressBar.apply {
            progressMax = 3f
            setProgressWithAnimation(correctAnswerCount.toFloat(),3000)
        }


        builder.setView(inflation)
        // Add action buttons
        val dialog = builder.create()

        inflation.questionAlertDialogCloseButton.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.window!!.setBackgroundDrawableResource(R.drawable.quiz_alert_dialog_layout_background)
        dialog.show()
    }
}