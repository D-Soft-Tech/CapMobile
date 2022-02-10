package com.example.capmobile.ui.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.navigation.Navigation
import coil.load
import com.airbnb.paris.extensions.style
import com.example.capmobile.R
import com.example.capmobile.util.AppConstants.getBitmapFromUri
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("loadIcon")
fun ImageView.setDrawable(drawable: Int) {
    this.setImageDrawable(resources.getDrawable(drawable))
}

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("loadIcon")
fun ImageView.setDrawable(drawable: String?) {
    drawable?.let {
        this.load(
            getBitmapFromUri(it.toUri(), this.context)
        )
    } ?: this.setImageDrawable(resources.getDrawable(R.drawable.user_image))
}

@BindingAdapter("setBackGroundColor")
fun CardView.backgroundColor(color: Int) {
    this.setCardBackgroundColor(resources.getColor(color))
}

@BindingAdapter("validate")
fun TextInputEditText.validateInput(str: Int) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.toString().length < 11 || s.toString().length > 11) {
                ((this@validateInput.parent.parent) as TextInputLayout).apply {
                    style(R.style.lekanTilStyleForErrorBorder)
                    setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)
                }
            } else {
                ((this@validateInput.parent.parent) as TextInputLayout).apply {
                    style(R.style.lekanTilStyleForSuccess)
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_lekan_check_icon,
                        0
                    )
                }
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

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("loadBackForActiveStatus")
fun View.getActiveIcon(active: String) {
    when (active) {
        "Active" -> {
            background = resources.getDrawable(R.drawable.active_status_bg)
        }
        "Inactive" -> {
            background = resources.getDrawable(R.drawable.inactive_status_bg)
        }
        "Dormant" -> {
            background = resources.getDrawable(R.drawable.dormant_status_bg)
        }
    }
}