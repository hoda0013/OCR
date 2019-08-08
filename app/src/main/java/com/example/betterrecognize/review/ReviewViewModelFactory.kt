package com.example.betterrecognize.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.betterrecognize.network.Network

class ReviewViewModelFactory(val network: Network) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReviewViewModel(network) as T
    }
}