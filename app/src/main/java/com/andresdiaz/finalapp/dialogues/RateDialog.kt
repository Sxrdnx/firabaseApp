package com.andresdiaz.finalapp.dialogues

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.andresdiaz.finalapp.R
import com.andresdiaz.finalapp.toast

class RateDialog : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_title))
            .setView(R.layout.dialog_rate)
            .setPositiveButton(getString(R.string.dialog_ok)) { dialog, wich->
                activity!!.toast("Pressed OK")
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { dialog, wich->
                activity!!.toast("pressed Cancel")
            }
            .create()


    }
}