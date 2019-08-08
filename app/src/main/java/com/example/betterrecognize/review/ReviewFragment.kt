package com.example.betterrecognize.review

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.betterrecognize.GraphicOverlay
import com.example.betterrecognize.R
import com.example.betterrecognize.processing.TextParser
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.fragment_review.*
import java.io.IOException
import java.io.InputStream


class ReviewFragment : Fragment() {

    val recognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
    private lateinit var mGraphicOverlay: GraphicOverlay
    private val parser = TextParser()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button_submit.setOnClickListener {
            // TODO: Submit image and data
        }

        // TODO: Grab image from camera (file, URI, etc...) and show in ImageView
        mGraphicOverlay = view.findViewById(R.id.graphic_overlay)

        val image = getBitmapFromAsset(context!!, "test_receipt_2.jpg")
        image?.let {
            progressbar_review.visibility = View.VISIBLE
            imageview_preview.setImageBitmap(it)
            runTextRecognition(it)
        }
    }

    private fun runTextRecognition(image: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(image)
        recognizer.processImage(image)
            .addOnSuccessListener { texts ->
                processTextRecognitionResult(texts)
            }
            .addOnFailureListener(
                object : OnFailureListener {
                    override fun onFailure(e: Exception) {
                        progressbar_review.visibility = View.VISIBLE
                        // Task failed with an exception
                        makeToast("Text processing failed. Please contact Google customer support.")
                        e.printStackTrace()
                    }
                })
    }

    private fun processTextRecognitionResult(texts: FirebaseVisionText) {
        if (texts.textBlocks.isEmpty()) {
            makeToast("No text found in image")
            return
        }

        val parseResult = parser.parse(texts)

        textview_data.text = parseResult.toString()
        progressbar_review.visibility = View.INVISIBLE
        button_submit.isEnabled = true

    }

    private fun makeToast(text: String) {
        Toast.makeText(context!!, text, Toast.LENGTH_SHORT).show()
    }

    fun getBitmapFromAsset(context: Context, filePath: String): Bitmap? {
        val assetManager = context.assets

        val `is`: InputStream
        var bitmap: Bitmap? = null
        try {
            `is` = assetManager.open(filePath)
            bitmap = BitmapFactory.decodeStream(`is`)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }
}