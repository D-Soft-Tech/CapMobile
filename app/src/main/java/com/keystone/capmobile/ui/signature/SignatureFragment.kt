package com.keystone.capmobile.ui.signature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keystone.capmobile.util.SignatureCanvas
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignatureFragment @Inject constructor(): Fragment() {
    @Inject
    lateinit var signatureCanvas: SignatureCanvas
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        signatureCanvas.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        return signatureCanvas
    }

    fun clear() {
        signatureCanvas.clear()
    }

    fun getSignatureInBase64() = signatureCanvas.getSignatureInBase64()
}