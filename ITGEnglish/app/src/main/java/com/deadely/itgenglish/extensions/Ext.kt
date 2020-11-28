package com.deadely.itgenglish.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.deadely.itgenglish.R
import com.deadely.itgenglish.utils.FieldConverter
import com.google.android.material.snackbar.Snackbar

fun Fragment.setActivityTitle(@StringRes id: Int) {
    (activity as? AppCompatActivity)?.supportActionBar?.title = getString(id)
}

fun Fragment.setActivityTitle(title: String) {
    (activity as? AppCompatActivity)?.supportActionBar?.title = title
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun View.makeGone() {
    visibility = View.GONE
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun snack(view: View, id: Int) {
    Snackbar.make(view, FieldConverter.getString(id).toString(), Snackbar.LENGTH_LONG)
        .setAction(FieldConverter.getString(R.string.action), null).show()
}

fun snack(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_LONG)
        .setAction(FieldConverter.getString(R.string.action), null).show()
}
