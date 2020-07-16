package com.andresdiaz.finalapp.dialogues

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.andresdiaz.finalapp.R
import com.andresdiaz.finalapp.models.NewRateEvent
import com.andresdiaz.finalapp.models.Rate
import com.andresdiaz.finalapp.toast
import com.andresdiaz.finalapp.util.RxBus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog_rate.view.*
import java.util.*

class RateDialog : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_rate,null)
        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_title))
            .setView(R.layout.dialog_rate)
            .setPositiveButton(getString(R.string.dialog_ok)) { _, _ ->
                activity!!.toast("Pressed OK")
                val textRate=view.editTextRateFeedback.text.toString()
                val imgURL=FirebaseAuth.getInstance().currentUser!!.photoUrl?.toString() ?: run{""}
                val rate= Rate(textRate,view.ratingBarFeedback.rating, Date(),imgURL)
                RxBus.publish(NewRateEvent(rate))
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ ->
                activity!!.toast("pressed Cancel")
            }
            .create()


    }
}