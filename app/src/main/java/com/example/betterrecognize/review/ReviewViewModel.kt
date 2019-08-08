package com.example.betterrecognize.review

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.betterrecognize.network.Network
import com.example.betterrecognize.processing.ParsedOutput
import com.example.betterrecognize.processing.TextParser
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.coroutines.*

class ReviewViewModel constructor(private val network: Network) : ViewModel() {

    private val recognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
    private val parser = TextParser()

    val progressLiveData = MutableLiveData<Boolean>().apply { value = false }
    val buttonLiveData = MutableLiveData<Boolean>().apply { value = false }
    val resultLiveData = MutableLiveData<ParsedOutput?>().apply { value = ParsedOutput(null, null, null, null, null) }
    val toastEventLiveData = MutableLiveData<Event<String>>().apply { value = Event("") }

    // dispatches execution into Android main thread
    private val uiDispatcher: CoroutineDispatcher = Dispatchers.Main
    // represent a pool of shared threads as coroutine dispatcher
    private val bgDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val uiScope = CoroutineScope(Dispatchers.Main)

    fun runRecognition(bitmap: Bitmap) {
        runTextRecognition(bitmap)
    }

    fun submitData() {
        network.uploadData()
    }

    private fun runTextRecognition(bitmap: Bitmap) {
        progressLiveData.value = true

        val image = FirebaseVisionImage.fromBitmap(bitmap)
        recognizer.processImage(image)
            .addOnSuccessListener { texts ->
                if (texts.textBlocks.isEmpty()) {
                    toastEventLiveData.value = Event("No text found in image")
                    progressLiveData.value = false
                    resultLiveData.value = ParsedOutput(null, null, null, null, null)
                } else {
                    uiScope.launch {
                          val output: ParsedOutput = async(bgDispatcher) {
                            parse(texts)
                        }.await()

                        resultLiveData.value = output
                        progressLiveData.value = false
                        buttonLiveData.value = true
                    }
                }
            }
            .addOnFailureListener { e ->
                progressLiveData.value = false
                toastEventLiveData.value = Event("Text processing failed. Please contact Google customer support.")
                e.printStackTrace()
            }
    }

    private fun parse(texts: FirebaseVisionText): ParsedOutput {
        return parser.parse(texts)
    }
}