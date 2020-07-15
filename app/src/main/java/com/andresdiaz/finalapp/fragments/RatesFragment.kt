package com.andresdiaz.finalapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.andresdiaz.finalapp.R
import com.andresdiaz.finalapp.adapters.RatesAdapter
import com.andresdiaz.finalapp.dialogues.RateDialog
import com.andresdiaz.finalapp.models.Rate
import kotlinx.android.synthetic.main.fragment_rates.view.*


class RatesFragment : Fragment() {
    private lateinit var _view: View
    private lateinit var adapter: RatesAdapter
    private val ratesList: ArrayList<Rate> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_rates, container, false)
        setUpRecyclerView()
        setUpFab()

        return _view
    }

    private fun setUpRecyclerView() {
        val layoutManager=LinearLayoutManager(context)
        adapter= RatesAdapter(ratesList)
        _view.recyclerViewRates.setHasFixedSize(true)
        _view.recyclerViewRates.layoutManager=layoutManager
        _view.recyclerViewRates.itemAnimator=DefaultItemAnimator()
        _view.recyclerViewRates.adapter=adapter
    }

    private fun setUpFab(){
        _view.fabRating.setOnClickListener { RateDialog().show(fragmentManager!!,"")
        }
    }



}