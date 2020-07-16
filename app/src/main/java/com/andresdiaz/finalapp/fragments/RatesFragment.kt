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
import com.andresdiaz.finalapp.models.NewRateEvent
import com.andresdiaz.finalapp.models.Rate
import com.andresdiaz.finalapp.util.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_rates.view.*


class RatesFragment : Fragment() {
    private lateinit var _view: View
    private lateinit var adapter: RatesAdapter
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var rateDBRef: CollectionReference
    private var rateSubscription: ListenerRegistration? = null
    private lateinit var rateBusListener: Disposable
    private val ratesList: ArrayList<Rate> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_rates, container, false)

        setUpRatesDB()
        setUpCurrentUser()
        setUpRecyclerView()
        setUpFab()
        subscribeToNewRatings()

        return _view
    }

    private fun setUpRatesDB() {
        rateDBRef = store.collection("rates")
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpFab() {
        _view.fabRating.setOnClickListener {
            RateDialog().show(fragmentManager!!, "")
        }
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = RatesAdapter(ratesList)
        _view.recyclerViewRates.setHasFixedSize(true)
        _view.recyclerViewRates.layoutManager = layoutManager
        _view.recyclerViewRates.itemAnimator = DefaultItemAnimator()
        _view.recyclerViewRates.adapter = adapter
    }

    private fun saveRate(rate: Rate) {
    }
    private fun subscribeToRatings(){

    }
    private fun subscribeToNewRatings(){
        RxBus.listen(NewRateEvent::class.java).subscribe{
            saveRate(it.rate)
        }
    }



}