package com.andresdiaz.finalapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andresdiaz.finalapp.R
import com.andresdiaz.finalapp.adapters.RatesAdapter
import com.andresdiaz.finalapp.dialogues.RateDialog
import com.andresdiaz.finalapp.models.NewRateEvent
import com.andresdiaz.finalapp.models.Rate
import com.andresdiaz.finalapp.toast
import com.andresdiaz.finalapp.util.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_rates.*
import kotlinx.android.synthetic.main.fragment_rates.view.*
import kotlinx.android.synthetic.main.fragment_rates.view.fabRating
import java.util.*
import java.util.EventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.fragment_rates, container, false)

        setUpRatesDB()
        setUpCurrentUser()
        setUpRecyclerView()
        setUpFab()
        subscribeToRatings()
        subscribeToNewRatings()

        return _view
    }

    private fun setUpRatesDB() {
        rateDBRef = store.collection("rates")
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = RatesAdapter(ratesList)
        _view.recyclerViewRates.setHasFixedSize(true)
        _view.recyclerViewRates.layoutManager = layoutManager
        _view.recyclerViewRates.itemAnimator = DefaultItemAnimator()
        _view.recyclerViewRates.adapter = adapter
        scrollListener=object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy>0||dy<0 && _view.fabRating.isShown){
                    _view.fabRating.hide()
                }
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState==RecyclerView.SCROLL_STATE_IDLE){
                    _view.fabRating.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        }
        _view.recyclerViewRates.addOnScrollListener(scrollListener)

    }

    private fun setUpFab() {
        _view.fabRating.setOnClickListener {
            RateDialog().show(fragmentManager!!, "")
        }
    }

    private fun hasUserRated(rate: ArrayList<Rate>):Boolean{
        var result=false
        rate.forEach{
            if(it.userId == currentUser.uid)
                result=true
        }
        return result
    }
    private fun removeFABIfRated(rated: Boolean){
        if(rated){
            _view.fabRating.hide()
            _view.recyclerViewRates.removeOnScrollListener(scrollListener)
        }
    }

    private fun saveRate(rate: Rate) {
        val newRating=HashMap<String,Any>()
        newRating["userId"]= rate.userId
        newRating["text"]= rate.text
        newRating["rate"]= rate.rate
        newRating["createdAt"]=rate.createdAt
        newRating["profileImgURL"]=rate.profileImgURL
        rateDBRef.add(newRating).addOnCanceledListener {
            activity!!.toast("Rating added!")
        }
            .addOnFailureListener{
                activity!!.toast("Error!")
            }
    }

    private fun subscribeToRatings(){
        rateSubscription = rateDBRef
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener(object :
                EventListener,com.google.firebase.firestore.EventListener<QuerySnapshot>{
                override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                    exception?.let {
                        activity!!.toast("Exception!")
                        return
                    }
                    snapshot?.let {
                        ratesList.clear()
                        val rates = it.toObjects(Rate::class.java)
                        ratesList.addAll(rates)
                        removeFABIfRated(hasUserRated(ratesList))
                        adapter.notifyDataSetChanged()
                        _view.recyclerViewRates.smoothScrollToPosition(0)
                    }
                }
            })

    }

    private fun subscribeToNewRatings(){
        rateBusListener= RxBus.listen(NewRateEvent::class.java).subscribe{
            saveRate(it.rate)
        }
    }

    override fun onDestroyView() {
        _view.recyclerViewRates.removeOnScrollListener(scrollListener)
        rateBusListener.dispose()
        rateSubscription?.remove()
        super.onDestroyView()
    }
}