package com.andresdiaz.finalapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andresdiaz.finalapp.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance()  }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        editTextEmail.validate {
            editTextEmail.error = if (isValidEmail(it)) null else "Email is not valid"
        }

        buttonGoLogIn.setOnClickListener{
            goToActivity<LoginActivity>{
                flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK// para que no regrese el activciti
            }
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }


        buttonForgot.setOnClickListener{
            val email= editTextEmail.text.toString()
            if (isValidEmail(email)){
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                    toast("Email has been send to reset your password")
                    goToActivity<LoginActivity>{
                        flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK// para que no regrese el activciti
                    }
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }
            }else{
                toast("Please make sure the email addres is correct")
            }
        }


    }
}
