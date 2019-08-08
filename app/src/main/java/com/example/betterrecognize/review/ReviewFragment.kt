package com.example.betterrecognize.review

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.betterrecognize.BetterRecognizeApplication
import com.example.betterrecognize.R
import com.example.betterrecognize.network.Network
import com.example.betterrecognize.processing.GraphicOverlay
import com.example.betterrecognize.processing.ParsedOutput
import kotlinx.android.synthetic.main.fragment_review.*

class ReviewFragment : Fragment() {

    private lateinit var mGraphicOverlay: GraphicOverlay
    private lateinit var photoUri: Uri
    private lateinit var viewModel: ReviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(
            this,
            ReviewViewModelFactory(Network((requireActivity().applicationContext as BetterRecognizeApplication).retrofit))
        ).get(ReviewViewModel::class.java)

        val uriString = arguments?.getString(KEY_PHOTO_URI) ?: throw RuntimeException("Must pass Uri as argument")

        val progressObserver = Observer<Boolean> {
            if (it) progressbar_review.visibility = View.VISIBLE else progressbar_review.visibility = View.INVISIBLE
        }

        val buttonEnabledObserver = Observer<Boolean> {
            button_submit.isEnabled = it
        }

        val resultObserver = Observer<ParsedOutput?> {
            textview_data.text = it.toString()
        }

        val toastObserver = Observer<Event<String>> {
            if (!it.hasBeenHandled) {
                Toast.makeText(context!!, it.getContentIfNotHandled(), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.apply {
            progressLiveData.observe(this@ReviewFragment, progressObserver)
            buttonLiveData.observe(this@ReviewFragment, buttonEnabledObserver)
            resultLiveData.observe(this@ReviewFragment, resultObserver)
            toastEventLiveData.observe(this@ReviewFragment, toastObserver)
        }

        photoUri = Uri.parse(uriString)
        val image = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, photoUri)
        viewModel.runRecognition(image)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button_submit.setOnClickListener {
            viewModel.submitData()
        }
        mGraphicOverlay = view.findViewById(R.id.graphic_overlay)
        imageview_preview.setImageURI(photoUri)
    }

    companion object {
        val TAG: String = ReviewFragment::class.java.simpleName
        val KEY_PHOTO_URI = "$TAG.KEY_PHOTO_URI"
    }
}