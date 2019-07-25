package com.swenggco.contactapp.base

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.swenggco.contactapp.AndroidApp
import com.swenggco.contactapp.injections.modules.ViewModule


abstract class BaseFragment : Fragment() {

    val Fragment.app: AndroidApp
        get() = activity?.application as AndroidApp


    val component by lazy {
        app.component.plus(ViewModule(activity))
    }


    abstract fun inject()
    open fun setListener() {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListener()
    }

    fun Fragment.toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.toast(msgId: Int) {
        Toast.makeText(context, getString(msgId), Toast.LENGTH_SHORT).show()
    }

    fun TextInputLayout.getText(): String {
        return editText?.text.toString()
    }

    fun TextInputLayout.setText(text: String) {
        editText?.setText(text, TextView.BufferType.EDITABLE)
    }

}
