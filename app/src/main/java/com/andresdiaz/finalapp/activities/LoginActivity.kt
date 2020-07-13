package com.andresdiaz.finalapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.andresdiaz.finalapp.*
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy{ FirebaseAuth.getInstance()}
    private val mSignInClient: GoogleSignInClient by lazy { getGoogleSingIn()}
    private val RC_SIGN_IN = 9001 // el codigo da igual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        /*if (mAuth.currentUser==null){
            toast("No tas logiado")
        }else{
            toast("tas logiado")
            mAuth.signOut()
        }*/
        editTextEmail.validate {
            editTextEmail.error=if (isValidEmail(it)) null else "email is not valid"
        }

        editPassword.validate {
            editPassword.error=if (isValidPassword(it)) null else "Password should contain 1 lowercase, 1uppercase, 1 number, 1 special character and 4 for characters length"
        }

        buttonLogIn.setOnClickListener{
            val email = editTextEmail.text.toString()
            val password=editPassword.text.toString()
            if(isValidEmail(email) && isValidPassword(password)) {
                logInByEmail(email, password)
            } else {
                toast("Pleace make sure all the data is correct.")
            }
        }

        textViewForgotPassword.setOnClickListener{
            goToActivity<ForgotPasswordActivity>()
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
        }

        buttonLogInGoogle.setOnClickListener{
            val intent: Intent = mSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

        buttonCreateAccount.setOnClickListener{
            goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
        }

    }

    private fun getGoogleSingIn():GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this,gso)
    }

    private fun logInByEmail(email: String, password:String){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                if(mAuth.currentUser!!.isEmailVerified){
                    goToActivity<MainActivity>{
                        flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                }else{
                    toast("User must confirm email first")
                }
                /*--informacion del usuario
                val currentUser=mAuth.currentUser!!
                currentUser.displayName
                currentUser.email
                currentUser.photoUrl
                currentUser.phoneNumber
                currentUser.isEmailVerified*/
            }else{
                toast("An unexpected error ocurred, please try again")
            }
        }


    }

    /* como esta en la documentacion
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==RC_SIGN_IN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account!!)
        }else{
            toast("fallo bro :c ")
        }
    }

    private fun firebaseAuthWithGoogle(acc: GoogleSignInAccount){
        Log.d("Tag","FirebaseAuthWithGoogle: "+ acc.id!!)
        val credential=GoogleAuthProvider.getCredential(acc.idToken,null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){task->
                if (task.isSuccessful){
                    toast("Ya quedo con el Gooogle v:")
                }else{
                    Log.w("TAG",task.exception)
                  toast("no jalo  agein :c")
                }
            }
    }*/

   private fun loginByGoogleAccountIntoFirebase(googleAccount: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken,null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(this){task->
            if(task.isSuccessful){
              mSignInClient.signOut()
            }
            goToActivity<MainActivity>{
                flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result!!.isSuccess){
                val account = result.signInAccount
                loginByGoogleAccountIntoFirebase(account!!)
            } else {
                toast("salio mal bro :c ")
            }
        }
    }


}
