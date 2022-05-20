package com.example.peodemo.welcomeFragments

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.peodemo.R


open class get_started_Fragment : Fragment() {

    //create a private play Audio property(with Pasmala) to use this property in every override methods
    private lateinit var playAudio:MediaPlayer
    //create a private play Audio property(without Pasmala) to use this property in every override methods
    private lateinit var anotherPlayAudio:MediaPlayer
    //create a member of nav control to navigate the fragments
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //set Raad file in the playAudio property
        playAudio = MediaPlayer.create(context,R.raw.raadsoura)
        //set Raad soura without pasmala file in the anotherPlayAudio property
        anotherPlayAudio = MediaPlayer.create(context,R.raw.raadsourawithoutpasmala)

        //this line to find the nav host that be in main activity
        mNavController = findNavController()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_started_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //assigning this property to context the activity on it
        val window = this.activity?.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.islamicFragment)


        //those lines to get the view in xml file by them id
        //assigning the view this view is imageview called first welcome button
        val nextButton = view.findViewById<Button>(R.id.getStartedButton)
        //assigning audioCasing property to control the image(a audioCasing image) in fragment xml file
        val audioCasing = view.findViewById<ImageView>(R.id.audioCase)
        //this line change the quranImage view position about 450 degree at 6 seconds
        val quranImage = view.findViewById<ImageView>(R.id.imageView16)


        //create an action by click on quran image
        quranImage.setOnClickListener {
            //if the user click visible the image
            audioCasing.visibility = View.VISIBLE
            //create a condition if it matches
            if (playAudio.isPlaying) {
                //pause the the audio
                playAudio.pause()
                //and set the pause image by it's id
                audioCasing.setImageResource(R.drawable.play)
            }
            //if not
            else if(!playAudio.isPlaying){
                //play the audio
                playAudio.start()
                //and set the play image by it's id
                audioCasing.setImageResource(R.drawable.pause)
            }
        }

        //create an action by the audio is completed
        playAudio.setOnCompletionListener {
            //and set the replay image by it's id
            audioCasing.setImageResource(R.drawable.replay)
            //if the audio with Pasmala ended let the play audio property equal the audio without Pasmala
            playAudio = anotherPlayAudio
        }

        //create an action by the audio that without Pasmala is completed
        anotherPlayAudio.setOnCompletionListener {
            //and set the replay image by it's id
            audioCasing.setImageResource(R.drawable.replay)
        }

        //this line to make an action when the user click on next button
        nextButton.setOnClickListener {
            //create an action to move to another Activity called introduction Activity by save change
            val action = get_started_FragmentDirections.actionGetStartedFragmentToIntroductionActivity()
            //create a extras property to add flags if the user click on return system button close the app
            val extras = ActivityNavigator.Extras.Builder()
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .build()
            //to add this action to navigate method to move to introduction Activity
            mNavController.navigate(action,extras)
            //this method return ture and key to splash fragment
            onBoardingFinished()
        }


    }
    //create a method to return the splash fragment a boolean value if the user click on nextButton send to splash fragment true
    //if true the app does not launch the welcome fragments
    private fun onBoardingFinished(){
        //assigning a sharedPref property to create a key and get a context of this fragment
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        //assigning a editor property to create set a key and boolean value to use in splash fragment
        val editor = sharedPref.edit()
        //set those values
        editor.putBoolean("Finished", true)
        //apply it if the method called
        editor.apply()
    }



    //if the user click on return system button do this
    override fun onPause() {
        super.onPause()
        //stop the audio
        playAudio.stop()
    }




}