package com.example.betterrecognize.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.betterrecognize.MainActivity
import com.example.betterrecognize.R
import kotlinx.android.synthetic.main.fragment_camera.*

class CameraFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_takephoto.setOnClickListener {
            (requireActivity() as MainActivity).launchCamera()
        }
    }
}