package com.keystone.capmobile.ui.view.dialogFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import com.airbnb.lottie.LottieAnimationView
import com.keystone.capmobile.R
import com.keystone.capmobile.data.model.ModalData
import com.keystone.capmobile.databinding.ResponseModalBinding
import com.keystone.capmobile.util.ConstantsOnly.NETWORK_CALL_RESPONSE_DATA_DIALOG_BUNDLE_KEY
import com.keystone.capmobile.util.ConstantsOnly.NETWORK_CALL_RESPONSE_DATA_DIALOG_REQUEST_KEY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResponseModal @Inject constructor() : DialogFragment() {
    private lateinit var binding: ResponseModalBinding
    private lateinit var lottieIcon: LottieAnimationView
    private lateinit var messageTv: TextView
    private lateinit var cancelBtn: ImageView
    private var modalData: ModalData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(NETWORK_CALL_RESPONSE_DATA_DIALOG_REQUEST_KEY) { _, bundle ->
            modalData =
                bundle.getParcelable(NETWORK_CALL_RESPONSE_DATA_DIALOG_BUNDLE_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.response_modal, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        dialog?.window?.apply {
            setBackgroundDrawableResource(R.drawable.curve_bg)
            isCancelable = false
        }
    }

    override fun onResume() {
        super.onResume()
        setData()
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun initViews() {
        with(binding) {
            lottieIcon = statusIconLAV
            messageTv = statusMessage
            cancelBtn = cancelButton
        }
    }

    private fun setData() {
        modalData?.let {
            lottieIcon.setAnimation(it.icon)
            messageTv.text = it.message
        }
    }
}