package com.example.betterrecognize.review

import androidx.lifecycle.ViewModel
import com.example.betterrecognize.network.Network
import com.example.betterrecognize.processing.ParsedOutput

class ReviewViewModel constructor(private val network: Network) : ViewModel() {

    fun submitData(data: ParsedOutput) {
        network.uploadData()
    }
}