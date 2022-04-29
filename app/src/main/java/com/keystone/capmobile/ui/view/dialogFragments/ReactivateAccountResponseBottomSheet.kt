package com.keystone.capmobile.ui.view.dialogFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keystone.capmobile.data.model.ReactivateAcctBottomSheetData
import com.keystone.capmobile.databinding.LayoutReactivateAccountResponseBottomSheetBinding
import com.keystone.capmobile.util.ConstantsOnly.REACTIVATE_ACCT_BOTTOM_SHEET_DIALOG_BK
import com.keystone.capmobile.util.ConstantsOnly.REACTIVATE_ACCT_BOTTOM_SHEET_DIALOG_RK
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReactivateAccountResponseBottomSheet @Inject constructor() : BottomSheetDialogFragment() {
    private var _binding: LayoutReactivateAccountResponseBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var cancelButton: ImageView
    private lateinit var statusMessage: TextView
    private lateinit var viewDetailsBtn: Button
    private lateinit var lottieIcon: LottieAnimationView
    private var reactivateAcctBottomSheetRespData: ReactivateAcctBottomSheetData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(REACTIVATE_ACCT_BOTTOM_SHEET_DIALOG_RK) { _, bundle ->
            reactivateAcctBottomSheetRespData =
                bundle.getParcelable(REACTIVATE_ACCT_BOTTOM_SHEET_DIALOG_BK)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = LayoutReactivateAccountResponseBottomSheetBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        this.isCancelable = true
    }

    override fun onResume() {
        super.onResume()
        setData()
        cancelButton.setOnClickListener {
            this.dismiss()
        }
    }

    private fun setData() {
        statusMessage.text =
            reactivateAcctBottomSheetRespData?.message ?: "Error reactivating account"
        reactivateAcctBottomSheetRespData?.let { lottieIcon.setAnimation(it.icon) }
        if (reactivateAcctBottomSheetRespData?.actionId != null) {
            viewDetailsBtn.visibility = View.VISIBLE
            viewDetailsBtn.setOnClickListener {
                findNavController().navigate(reactivateAcctBottomSheetRespData!!.actionId!!)
            }
        } else {
            viewDetailsBtn.visibility = View.GONE
        }
    }

    private fun initViews() {
        with(binding) {
            cancelButton = cancelBtn
            lottieIcon = statusIconLAV
            statusMessage = reactivateAccountStatus
            viewDetailsBtn = viewDetailsButton
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
