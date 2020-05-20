package com.example.arcore20_round

import android.app.Activity
import android.content.Context
import android.media.CamcorderProfile
import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.ACTION_UP
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.activity_main.*


class Util(val context: Context,val arFragment: ArFragment) {
    val  activity:Activity
    init {
         activity = context as Activity
    }

    private lateinit var photoSaver: PhotoSaver
    private lateinit var videoRecorder: VideoRecorder
    private var isRecording = false


    fun eliminateDot() {
        arFragment.arSceneView.planeRenderer.isVisible = false
        arFragment.planeDiscoveryController.hide()
        arFragment.planeDiscoveryController.setInstructionView(null)
    }




    fun activateButtom() {
        photoSaver = PhotoSaver(activity)
        videoRecorder = VideoRecorder(activity).apply {
            sceneView = arFragment.arSceneView
            setVideoQuality(
                CamcorderProfile.QUALITY_1080P,
                activity.resources.configuration.orientation
            )
        }

        activity.btn5.setOnLongClickListener {
            isRecording = videoRecorder.toggleRecordingState()
            true
        }

        activity.btn5.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP && isRecording) {
                isRecording = videoRecorder.toggleRecordingState()
                Toast.makeText(context, "Saved video to gallery!", Toast.LENGTH_LONG).show()
                true
            } else false
        }
        activity.btn4.setOnClickListener {
            eliminateDot()
        }
        activity.btn1.setOnClickListener {
           activity.finishAffinity()
        }
    }

}