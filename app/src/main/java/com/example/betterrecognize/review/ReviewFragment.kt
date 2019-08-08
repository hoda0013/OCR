package com.example.betterrecognize.review

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.betterrecognize.BetterRecognizeApplication
import com.example.betterrecognize.processing.GraphicOverlay
import com.example.betterrecognize.R
import com.example.betterrecognize.network.Network
import com.example.betterrecognize.processing.ParsedOutput
import com.example.betterrecognize.processing.TextParser
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.fragment_review.*
import java.io.IOException
import java.io.InputStream


class ReviewFragment : Fragment() {

    private val recognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
    private lateinit var mGraphicOverlay: GraphicOverlay
    private val parser = TextParser()
    private lateinit var photoUri: Uri
    private lateinit var viewModel: ReviewViewModel
    private lateinit var parsedResult: ParsedOutput

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(
            this,
            ReviewViewModelFactory(Network((requireActivity().applicationContext as BetterRecognizeApplication).retrofit))
        ).get(ReviewViewModel::class.java)

        val uriString = arguments?.getString(KEY_PHOTO_URI) ?: throw RuntimeException("Must pass Uri as argument")
        photoUri = Uri.parse(uriString)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button_submit.setOnClickListener {
            // TODO: Submit image and data
            viewModel.submitData(parsedResult)
        }

        mGraphicOverlay = view.findViewById(R.id.graphic_overlay)

        val image = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, photoUri)
        imageview_preview.setImageURI(photoUri)
        image?.let {
            progressbar_review.visibility = View.VISIBLE
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

        parsedResult = parser.parse(texts)

        textview_data.text = parsedResult.toString()
        progressbar_review.visibility = View.INVISIBLE
        button_submit.isEnabled = true
    }

    private fun makeToast(text: String) {
        Toast.makeText(context!!, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        val TAG: String = ReviewFragment::class.java.simpleName
        val KEY_PHOTO_URI = "$TAG.KEY_PHOTO_URI"
    }
}