package com.example.arcore20_round

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    //bee_drill selector = 1   goaround=false


    val selector = 2
    val goAround = true
    val rotateAroundCamera=true

    var maxModelScale = 0.07f
    var minModelScale = 0.06f
    lateinit var model: Models
    var modelResourceId = 1
    var animationSring = ""

    lateinit var arFragment: ArFragment
    lateinit var modelR: ModelRenderable
    private var curCameraPosition = Vector3.zero()
    private val nodes = mutableListOf<RotatingNode>()
    private lateinit var util: Util

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSelector()
        arFragment = fragment as ArFragment
        util = Util(this, arFragment)

        loadModel()
        if (goAround) {
            locateModelGoAround()
        } else {
            locateModelOnePlace()
        }

    }

    private fun locateModelOnePlace() {
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            getCurrentScene().addChild(anchorNode)
            Node().apply {
                renderable=modelR
                setParent(anchorNode)
                localScale= Vector3(0.05f,0.05f,0.05f)
                localPosition=Vector3(0.0f,0.7f,0.0f)        //x,z,y
                localRotation = Quaternion.eulerAngles(Vector3(0f, 180f, 0f))
                startAnimation(renderable as ModelRenderable)
                util.eliminateDot()
            }
        }
    }

    private fun addNodeToSceneRound() {
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)

            val rotatingNode = RotatingNode(model.degreesPerSecond)
                .apply {
                    setParent(anchorNode)
                }
            Node().apply {
                renderable = modelR
                setParent(rotatingNode)
                localScale= Vector3(1.0f,1.0f,1.0f)
                localPosition = Vector3(6f, 0.05f, 0f)  //radius,hight
                localRotation = Quaternion.eulerAngles(Vector3(0f, 0f, 0f))
            }
            arFragment.arSceneView.scene.addChild(anchorNode)
         if (rotateAroundCamera)   nodes.add(rotatingNode)
            val animationData = modelR.getAnimationData(animationSring)
            ModelAnimator(animationData, modelR).apply {
                repeatCount = ModelAnimator.INFINITE
                start()
            }
            util.eliminateDot()
        }
    }

    private fun getCurrentScene() = arFragment.arSceneView.scene

    private fun startAnimation(renderable: ModelRenderable) {
        if (renderable.animationDataCount == 0) {
            return
        }
        val animationData = renderable.getAnimationData(animationSring)
        ModelAnimator(animationData, renderable).apply {
            repeatCount = ModelAnimator.INFINITE
            start()
        }

    }


    private fun setSelector() {
        when (selector) {
            1 -> {
                model = Models.Bee
                modelResourceId = R.raw.beedrill
                animationSring = "Beedrill_Animation"
                maxModelScale = 0.07f
                minModelScale = 0.06f
            }
            2 -> {
                model = Models.Fish
                modelResourceId = R.raw.sarfish
                animationSring = "Armature|ArmatureAction"
                maxModelScale = 0.07f
                minModelScale = 0.06f
            }
        }
    }


    private fun loadModel() {
        Toast.makeText(this, "First load the Model ", Toast.LENGTH_LONG).show()

      //  download.setOnClickListener {
            ModelRenderable.builder()
                .setSource(this, modelResourceId)
                .build()
                .thenAccept {
                    modelR = it
                    Toast.makeText(this, "Finish dDownload model Mr .", Toast.LENGTH_LONG).show()
                }.exceptionally {
                    Toast.makeText(this, "Error creating node: $it", Toast.LENGTH_LONG).show()
                    null
                }
     //   }
    }

    private fun locateModelGoAround() {
        addNodeToSceneRound()
        if (rotateAroundCamera) {
            arFragment.arSceneView.scene.addOnUpdateListener {
                curCameraPosition = arFragment.arSceneView.scene.camera.worldPosition
                for (node in nodes) {
                    node.worldPosition =
                        Vector3(curCameraPosition.x, node.worldPosition.y, curCameraPosition.z)
                }
            }
        }
    }


}
