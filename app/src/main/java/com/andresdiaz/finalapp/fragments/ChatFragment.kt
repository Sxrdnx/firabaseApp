package com.andresdiaz.finalapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.andresdiaz.finalapp.R
import com.andresdiaz.finalapp.adapters.ChatAdapter
import com.andresdiaz.finalapp.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import kotlinx.android.synthetic.main.fragment_chat.view.editTextMessage
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {
    private lateinit var _view: View
    private lateinit var adapter:ChatAdapter
    private val messageList:ArrayList<Message> = ArrayList()
    private val mAuth:FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser
    private val  store: FirebaseFirestore= FirebaseFirestore.getInstance()
    private lateinit var chatDBRef: CollectionReference



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _view=inflater.inflate(R.layout.fragment_chat, container, false)
        setUpChatDB()
        setUpCurrentUser()
        setUpRecyclerView()
        setUpChatBtn()
        return _view
    }

    private fun setUpChatDB(){
        chatDBRef=store.collection("chat")
    }
    private fun setUpCurrentUser(){
        currentUser=mAuth.currentUser!!

    }

    private fun setUpRecyclerView(){
        var layoutManager =LinearLayoutManager(context)
       adapter= ChatAdapter(messageList,currentUser.uid)
        _view.recyclerViewChat.setHasFixedSize(true)
        _view.recyclerViewChat.layoutManager= layoutManager
        _view.recyclerViewChat.itemAnimator=DefaultItemAnimator()
        _view.recyclerViewChat.adapter=adapter
    }
    private fun setUpChatBtn(){
        _view.buttonSend.setOnClickListener{
            val messageText=editTextMessage.text.toString()
            val message =Message(currentUser.uid,messageText,currentUser.photoUrl.toString(),Date())
            //guardar mensaje en firebase
              _view.editTextMessage.setText("")
        }
    }
}