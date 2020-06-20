package com.andresdiaz.finalapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andresdiaz.finalapp.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sing_up.*

/*verificar que un husuario esta logeado  */
class SignUpActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy{FirebaseAuth.getInstance()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        buttonGoLogIn.setOnClickListener{
            goToActivity<LoginActivity>{
                flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK//PERMITE QUE NO SE QUEDE AHI GUARADO
            }
            /*val intent=Intent(this,LoginActivity::class.java)
             intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK//PERMITE QUE NO SE QUEDE AHI GUARADO
             startActivity(intent) */
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)//animacion para las trancisiones de cambio de activity siempre debe ir despues de un start(activity)
            //overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
        }


        buttonSignUp.setOnClickListener{
            val email=editTextEmail.text.toString()
            val password=editTextPassword.text.toString()
            val confirmPassword=editTextConfirmPassword.text.toString()
            if (isValidEmail(email) && isValidPassword(password) && isValidConfirmPassword(password,confirmPassword)){
                signUpByEmail(email,password)
            }else{
                toast("please make sure all the data is correct.")
            }
        }


        editTextEmail.validate {
            editTextEmail.error=if (isValidEmail(it)) null else "email is not valid"
        }

        editTextPassword.validate {
            editTextPassword.error=if (isValidPassword(it)) null else "Password should contain 1 lowercase, 1uppercase, 1 number, 1 special character and 4 for characters length"
        }
        editTextConfirmPassword.validate {
            editTextConfirmPassword.error=if (isValidConfirmPassword(it,editTextPassword.text.toString())) null else "Confirm Password does not match with password"
        }
        /*esta es la forma sin usar  las extencion functions
        editTextEmail.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                este metodo nos permite cuando se cambie el texto podremos validar el texto, en real time
                if(isValidEmail(editTextEmail.text.toString())){
                      editTextEmail.error="el correo no es valido"
                } else {
                    editTextEmail.error=""
                }
             }
             override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

             }

             override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

             }

         })
        val currentUser= mAuth.currentUser
        if(currentUser==null){
            toast("User is NOT logged in")
            createAccount("beyser_teil_ram@hotmail.com","elcacas")
        }else{
            toast("User IS logged in")
        }*/
    }
    private fun signUpByEmail(email: String, password:String){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
                task ->
            if (task.isSuccessful){
                mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(this){//para saber que el email a sido mandado
                    toast("An email as been sent to you.Plase,confirm before sign in.")
                    goToActivity<LoginActivity>{
                        flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK//PERMITE QUE NO SE QUEDE AHI GUARADO
                    }
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }
            }else{
                toast("An unexpected error occurred, please try again")
            }
        }
    }
    

    /*private fun isValidEmailAndPassword(email: String, password:String):Boolean{
        return !email.isNullOrEmpty() &&
                !password.isNullOrEmpty() &&
                password== editTextConfirmPassword.text.toString()
    }

    private fun isValidEmail(email: String):Boolean{
        val pattern= Patterns.EMAIL_ADDRESS
        return  pattern.matcher(email).matches()
    }
    private fun isValidPassword(password: String):Boolean{
        //necesita contener --> 1 numero, 1 minuscula/ 1 mayuscula/1 especial/minCaracteres 4
        val passwordPatterns="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\s+$).{4,}$"
        val pattern= Pattern.compile(passwordPatterns)
        return pattern.matcher(password).matches()

    }

    private fun isValidConfirmPassword(password: String, confirmPassword: String):Boolean{
        return password==confirmPassword
    }*/


}
