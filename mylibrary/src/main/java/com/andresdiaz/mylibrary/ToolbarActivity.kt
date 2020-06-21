package com.andresdiaz.mylibrary

import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.andresdiaz.mylibrary.interfaces.Itoolbar

open class ToolbarActivity: AppCompatActivity(),Itoolbar{
    protected  var _toolbar:Toolbar?=null
    override fun toolbarToLoad(toolbar: Toolbar?) {
        _toolbar=toolbar
        _toolbar?.let{
            setSupportActionBar(_toolbar)
        }
    }

    override fun enableHomeDisplay(value: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }

}