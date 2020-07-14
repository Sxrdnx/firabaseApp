package com.andresdiaz.finalapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andresdiaz.finalapp.R
import com.andresdiaz.finalapp.util.CircleTransform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.fragment_info.view.*

class InfoFragment : Fragment() {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDBRef: CollectionReference
    private var chatSubscription: ListenerRegistration? = null
    private lateinit var _View: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _View = inflater.inflate(R.layout.fragment_info, container, false)
        setUpChatDB()
        setUpCurrentUser()
        setUpCurrentUserInfoUI()
        return _View
    }

    private fun setUpChatDB() {
        chatDBRef = store.collection("chat")
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpCurrentUserInfoUI() {
        _View.textViewInfoEmail.text = currentUser.email
        _View.textViewInfoName.text = currentUser.displayName?.let { currentUser.displayName }
            ?: run { getString(R.string.info_no_name) }
        currentUser.photoUrl?.let {
            Picasso.get().load(currentUser.photoUrl).resize(300, 300)
                .centerCrop().transform(CircleTransform()).into(_View.imageViewInfo)
        }?: run{
            Picasso.get().load(R.drawable.ic_person).resize(300, 300)
                .centerCrop().transform(CircleTransform()).into(_View.imageViewInfo)
        }
    }
}


