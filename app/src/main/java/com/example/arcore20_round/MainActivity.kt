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
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    //bee_drill selector = 1   onePlaceNode=false

    val selector = 1

    var maxModelScale = 0.07f
    var minModelScale = 0.06f
    lateinit var model: Models
    var modelResourceId = 1
    var animationSring = ""

    lateinit var arFragment: ArFragment
    lateinit var modelRenderable: ModelRenderable
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

        locateModel()


    }

    private fun locateModel() {
        arFragment.arSceneView.scene.addOnUpdateListener {
            curCameraPosition = arFragment.arSceneView.scene.camera.worldPosition
            for (node in nodes) {
                node.worldPosition =
                    Vector3(curCameraPosition.x, node.worldPosition.y, curCameraPosition.z)
            }
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
        }

    }

    /*private fun getData() {
        val rendrebaleSource = RenderableSource.builder()
            .setSource(this, Uri.parse(url), RenderableSource.SourceType.GLB)
            // .setScale(scale)
            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
            .build()
        ModelRenderable.builder()
            .setSource(this, rendrebaleSource)
            .setRegistryId(Uri.parse(url))
            .build()
            .thenAccept {
                modelRenderable = it
                Toast.makeText(this, "Finish dDownload model Mr .", Toast.LENGTH_LONG).show()
                // addNodeToScene(anchor,it)
            }.exceptionally {
                Log.e("clima", "Somthing go wrong in loading model")
                null
            }

    }*/




    private fun loadModel() {
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
           // loadModelAndAddToSceneRound(hitResult.createAnchor(), modelResourceId)
            ModelRenderable.builder()
                .setSource(this, modelResourceId)
                .build()
                .thenAccept { modelRenderable ->
                    addNodeToSceneRound(hitResult.createAnchor(), modelRenderable)
                    //util.eliminateDot()
                }.exceptionally {
                    Toast.makeText(this, "Error creating node: $it", Toast.LENGTH_LONG).show()
                    null
                }
        }
    }


    private fun addNodeToSceneRound(
        anchor: Anchor,
        modelRenderable: ModelRenderable

    ) {
        val anchorNode = AnchorNode(anchor)
        val rotatingNode = RotatingNode(model.degreesPerSecond)
            .apply {
                setParent(anchorNode)
            }
        Node().apply {
            renderable = modelRenderable
            setParent(rotatingNode)
            localPosition = Vector3(model.radius, model.height, 0f)
            localRotation = Quaternion.eulerAngles(Vector3(0f, model.rotationDegrees, 0f))
        }
        arFragment.arSceneView.scene.addChild(anchorNode)
        nodes.add(rotatingNode)
        val animationData = modelRenderable.getAnimationData(animationSring)
        ModelAnimator(animationData, modelRenderable).apply {
            repeatCount = ModelAnimator.INFINITE
            start()
        }
    }
}
