package com.example.capmobile.ui.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.navigation.Navigation
import coil.load
import com.example.capmobile.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("loadIcon")
fun ImageView.setDrawable(drawable: Int) {
    this.setImageDrawable(resources.getDrawable(drawable))
}

@BindingAdapter("setBackGroundColor")
fun CardView.backgroundColor(color: Int) {
    this.setCardBackgroundColor(resources.getColor(color))
}

@BindingAdapter("validate")
fun EditText.validateInput(getDetailsBtn: Button) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.toString().length < 11 || s.toString().length > 11) {
                ((this@validateInput.parent.parent ) as TextInputLayout).apply {
                    boxStrokeColor = resources.getColor(R.color.error)
                    hintTextColor =
                        AppCompatResources.getColorStateList(this.context, R.color.error)
                    hint = resources.getString(R.string.invalidBvnLength)
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0, 0, R.drawable.ic_error, 0
                    )
                    getDetailsBtn.isEnabled = false
                }
            } else {
                ((this@validateInput.parent.parent) as TextInputLayout).apply {
                    boxStrokeColor = resources.getColor(R.color.success)
                    hintTextColor =
                        AppCompatResources.getColorStateList(this.context, R.color.success)
                    hint = resources.getString(R.string.good)
                    setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_success, 0)
                }
                getDetailsBtn.isEnabled = true
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    })
}

@BindingAdapter("nextPage", requireAll = true)
fun Button.nextPage(getDetailsBtn: Button) {
    this.setOnClickListener {
        if (getDetailsBtn.isEnabled) {
            // Navigate to other screens
//            Navigation.findNavController(it).navigate()
            Toast.makeText(it.context, "Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}

//@BindingAdapter("getBvnDetails")
//fun Button.getBvnDetail() {
//
//}

//fun getDialog(view: View, activity: Activity) =
//    AlertDialog.Builder(activity).setView(
//        view
//    ).create().setCanceledOnTouchOutside(false)

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("loadBackForActiveStatus")
fun View.getActiveIcon(active: String) {
    when (active) {
        "Active" -> {background = resources.getDrawable(R.drawable.active_status_bg)}
        "Inactive" -> {background = resources.getDrawable(R.drawable.inactive_status_bg)}
        "Dormant" -> {background = resources.getDrawable(R.drawable.dormant_status_bg)}
    }
}