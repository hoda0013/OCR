package com.example.betterrecognize.review

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.betterrecognize.GraphicOverlay
import com.example.betterrecognize.R
import com.example.betterrecognize.TextGraphic
import com.example.betterrecognize.processing.TextParser
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
//                        mTextButton.setEnabled(true)
                        // TODO Show error message
                        e.printStackTrace()
                    }
                })
    }

    private fun processTextRecognitionResult(texts: FirebaseVisionText) {
        if (texts.textBlocks.isEmpty()) {
//            showToast("No text found")
            // TODO: Show error toast
            return
        }

        val parseResult = parser.parse(texts)

        textview_data.text = parseResult.toString()
        progressbar_review.visibility = View.INVISIBLE
        button_submit.isEnabled = true

//        mGraphicOverlay.clear()
//        for (i in blocks.indices) {
//            val lines = blocks[i].lines
//            for (j in lines.indices) {
//                val elements = lines[j].elements
//                for (k in elements.indices) {
//                    val textGraphic = TextGraphic(mGraphicOverlay, elements[k])
//                    //                    String keyword = "Grs Bu.";
//                    //                    FirebaseVisionText.TextBlock block = blocks.get(i);
//                    //                    String text = block.getText();
//                    //                    String lineText = lines.get(j).getText();
//                    //                    if (lineText.contains(keyword)) {
//                    //                        Rect boundingBox = block.getBoundingBox();
//                    //                        Point[] cornerPoints = block.getCornerPoints();
//
////                    mGraphicOverlay.add(textGraphic)
//                    //                        Log.i("TAG", String.format("Keyword: %s exists at \n boundingBox L: %d, R: %d, T: %d, B: %d \n cornerPoints: 1: %d", keyword));
//                    //                    }
//
//                }
//            }
//        }
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