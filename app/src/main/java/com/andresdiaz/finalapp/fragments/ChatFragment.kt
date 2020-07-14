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
import com.andresdiaz.finalapp.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import kotlinx.android.synthetic.main.fragment_chat.view.editTextMessage
import java.util.*
import java.util.EventListener
import kotlin.collections.ArrayList


class ChatFragment : Fragment() {
    private lateinit var _view: View
    private lateinit var adapter:ChatAdapter
    private val messageList:ArrayList<Message> = ArrayList()
    private val mAuth:FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser
    private val  store: FirebaseFirestore= FirebaseFirestore.getInstance()
    private lateinit var chatDBRef: CollectionReference
    private var chatSubscription: ListenerRegistration?=null



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
        subscribeToChatMessages()
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
            val photo=currentUser.photoUrl?.let { currentUser.photoUrl.toString()} ?: run {""}
            val message =Message(currentUser.uid,messageText,photo,Date())
            saveMessage(message)
            _view.editTextMessage.setText("")
        }
    }

    private fun saveMessage(message:Message){
        val newMesage= hashMapOf<String,Any>()
        newMesage["authorId"]=message.authorId
        newMesage["message"]=message.message
        newMesage["profileImageURL"]=message.profileImageURL
        newMesage["sentAt"]=message.sentAt
        chatDBRef.add(newMesage)
            .addOnCompleteListener{
                activity!!.toast("Message addes!")
            }
            .addOnFailureListener{
                activity!!.toast("Message error, try again!")
            }
    }

    private fun subscribeToChatMessages(){
        chatSubscription=chatDBRef
            .orderBy("sentAt", Query.Direction.DESCENDING)
            .limit(10)
            .addSnapshotListener(object :EventListener,com.google.firebase.firestore.EventListener<QuerySnapshot>{
            override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                exception?.let {
                     activity!!.toast("Exception!")
                    return
                }
      ma          snapshot?.let {
                    messageList.clear()
                    val messages=it.toObjects(Message::class.java)
                    messageList.addAll(messages.asReversed())//cambia o voltea la lista
                    adapter.notifyDataSetChanged()
                    _view.recyclerViewChat.smoothScrollToPosition(messageList.size)//scrollea hasta el final de la lista
                }

            }

        })
    }

    override fun onDestroyView() {//se remueve el listener para optimazion
        chatSubscription?.remove()
        super.onDestroyView()
    }

}