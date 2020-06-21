package com.andresdiaz.finalapp


import android.os.Bundle   
import androidx.appcompat.widget.Toolbar
import com.andresdiaz.mylibrary.ToolbarActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :ToolbarActivity() {
    private val mAuth: FirebaseAuth by lazy{ FirebaseAuth.getInstance()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbarToLoad(toolbarView as Toolbar)
    }
}
