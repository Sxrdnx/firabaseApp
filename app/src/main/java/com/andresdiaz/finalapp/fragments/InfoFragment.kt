package com.andresdiaz.finalapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andresdiaz.finalapp.R

class InfoFragment : Fragment() {
    private lateinit var _View:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _View=inflater.inflate(R.layout.fragment_info, container, false)
        return _View
    }
}