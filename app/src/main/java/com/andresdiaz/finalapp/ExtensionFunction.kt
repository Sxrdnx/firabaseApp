package com.andresdiaz.finalapp

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sing_up.*
import java.util.regex.Pattern

fun Activity.toast(message: CharSequence, duration:Int= Toast.LENGTH_SHORT)=Toast.makeText(this,message,duration).show()

fun ViewGroup.inflate(layoutId: Int)= LayoutInflater.from(context).inflate(layoutId,this,false)!!

inline fun <reified T: Activity>Activity.goToActivity(noinline init: Intent.()->Unit={}){//noinline permite crear un contexto al intent
    val intent= Intent(this,T::class.java)
    intent.init()
    startActivity(intent)
}
//funciones para la validacion
fun EditText.validate(validation: (String)->Unit){
    //este metodo nos permite cuando se cambie el texto podremos validar el texto, en real time
    this.addTextChangedListener(object : TextWatcher{
        override fun afterTextChanged(editable: Editable?) {
            validation(editable.toString())

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    })
}


fun Activity.isValidEmail(email: String):Boolean{
    val pattern= Patterns.EMAIL_ADDRESS
    return  pattern.matcher(email).matches()
}
fun Activity.isValidPassword(password: String):Boolean{
    //necesita contener --> 1 numero, 1 minuscula/ 1 mayuscula/1 especial/minCaracteres 4
    val passwordPatterns="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%?!^&+=])(?=\\S+\$).{4,}\$$"
    val pattern= Pattern.compile(passwordPatterns)
    return pattern.matcher(password).matches()

}

fun Activity.isValidConfirmPassword(password: String, confirmPassword: String):Boolean{
    return password==confirmPassword
}