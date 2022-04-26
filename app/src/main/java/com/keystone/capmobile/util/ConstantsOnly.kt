package com.keystone.capmobile.util

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.MotionEvent
import android.view.View
import com.airbnb.paris.extensions.style
import com.keystone.capmobile.R
import com.google.android.material.textfield.TextInputEditText
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object ConstantsOnly {

    const val USER_FIRST_NAME = "UserName"
    const val FULL_NAME = "name"
    const val BASE_URL_SELECTOR = "UseAnotherBaseUrl"
    const val CAP_BASE_URL = "http://41.203.110.24:4011/"
    const val ACCOUNT_OPENING_BASE_URL = "http://41.203.110.24:7780/OmniAPI/"
    const val SELECT_BRANCH_REQUEST_KEY = "selectBranchRqKy"
    const val SELECT_BRANCH_BUNDLE_KEY = "selectBranchBdKy"
    const val SELECT_BRANCH_BOTTOM_SHEET_FRAGMENT_TAG = "selectBranch"
    const val SET_SIGNATURE_REQUEST_KEY = "setSignatureRK"
    const val SET_SIGNATURE_BUNDLE_KEY = "setSignatureBK"
    const val REACTIVATE_ACCT_BOTTOM_SHEET_DIALOG_RK = "ReactivateAccountResponseBottomSheetRK"
    const val REACTIVATE_ACCT_BOTTOM_SHEET_DIALOG_BK = "ReactivateAccountResponseBottomSheetBK"
    const val REACTIVATE_ACCT_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG = "reactivateAccount"

    fun TextInputEditText.notifySuccessfulValidationWithCheckAndBorder() {
        style(R.style.lekanTilStyleForSuccess)
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_lekan_check_icon,
            0
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    fun TextInputEditText.handleEditTextDrawableRightClick(
        actionToRun: () -> Unit
    ) {
        setOnTouchListener(View.OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (right - compoundDrawables[2].bounds.width())) {
                    actionToRun()
                }
            }
            false
        })
    }

    fun TextInputEditText.notifySuccessfulValidationWithOnlyCheck() {
        style(R.style.lekanTilStyle)
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_lekan_check_icon,
            0
        )
    }

    fun TextInputEditText.notifySuccessfulValidationWithOnlyBorder() {
        style(R.style.lekanTilStyleForSuccess)
    }

    fun View.notifyErrorValidation() {
        style(R.style.lekanTilStyleForErrorBorder)
    }

    fun TextInputEditText.onTextChangeValidation(
        condition: Boolean,
        successAction: () -> Unit,
        errorAction: () -> Unit
    ) {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (condition) {
                    successAction()
                } else {
                    errorAction()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    fun phoneNumberValidator(userPhoneNumber: String): Boolean {
        val patterns = ("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$")

        val pattern: Pattern = Pattern.compile(patterns)
        val matcher: Matcher = pattern.matcher(userPhoneNumber)
        return matcher.matches()
    }

    private val VALID_EMAIL_ADDRESS_REGEX: Pattern =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    fun emailValidator(userEmail: String): Boolean {
        val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(userEmail)
        return matcher.matches()
    }

    fun base64StringToImage(base64String: String): Bitmap {
        val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    fun formatDateFromBvn(dateFromBvnValidation: String): Int {
        val originalFormat: DateFormat = SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat("yyyyMMdd", Locale.ENGLISH)
        val date = originalFormat.parse(dateFromBvnValidation)
        return date?.let { targetFormat.format(it).toInt() } ?: 0
    }
}