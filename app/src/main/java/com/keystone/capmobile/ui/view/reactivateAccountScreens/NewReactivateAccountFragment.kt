package com.keystone.capmobile.ui.view.reactivateAccountScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.google.android.material.textfield.TextInputEditText
import com.keystone.capmobile.R
import com.keystone.capmobile.data.model.ReactivateAcctBottomSheetData
import com.keystone.capmobile.databinding.FragmentNewReactivateAccountBinding
import com.keystone.capmobile.databinding.LoadingDialogLayoutBinding
import com.keystone.capmobile.ui.view.dialogFragments.ReactivateAccountResponseBottomSheet
import com.keystone.capmobile.ui.viewModel.OpenAccountViewModel
import com.keystone.capmobile.util.AppConstants
import com.keystone.capmobile.util.AppConstants.showAlerter
import com.keystone.capmobile.util.ConstantsOnly.REACTIVATE_ACCT_BOTTOM_SHEET_DIALOG_BK
import com.keystone.capmobile.util.ConstantsOnly.REACTIVATE_ACCT_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG
import com.keystone.capmobile.util.ConstantsOnly.REACTIVATE_ACCT_BOTTOM_SHEET_DIALOG_RK
import com.keystone.capmobile.util.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewReactivateAccountFragment @Inject constructor() : Fragment() {
    private lateinit var binding: FragmentNewReactivateAccountBinding
    private lateinit var activateBtn: Button
    private lateinit var acctNumET: TextInputEditText
    private val viewModel: OpenAccountViewModel by activityViewModels()

    private lateinit var dialogBinding: LoadingDialogLayoutBinding
    private lateinit var pleaseWaitDialog: AlertDialog

    @Inject
    lateinit var reactivateAccountResponseBottomSheet: ReactivateAccountResponseBottomSheet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_reactivate_account,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        dialogBinding = LoadingDialogLayoutBinding.inflate(layoutInflater)
        pleaseWaitDialog = AppConstants.getCustomDialog(requireContext(), dialogBinding.root).apply {
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onResume() {
        super.onResume()

        activateBtn.setOnClickListener {
            if (!(acctNumET.text.isNullOrEmpty())) {

                viewModel.reactivateAccount(acctNumET.text.toString())
                viewModel.reactivateAccountResponseBody.observe(viewLifecycleOwner) {
                    when (it.status) {
                        Status.LOADING -> {
                            pleaseWaitDialog.show()
                        }
                        Status.SUCCESS -> {
                            pleaseWaitDialog.dismiss()

                            showReactivateAccountResponseBottomSheet(
                                ReactivateAcctBottomSheetData(
                                    it.message,
                                    R.raw.success,
                                    null
                                )
                            )
                        }
                        Status.ERROR -> {
                            pleaseWaitDialog.dismiss()
                            showReactivateAccountResponseBottomSheet(
                                ReactivateAcctBottomSheetData(
                                    it.message,
                                    R.raw.failed,
                                    null
                                )
                            )
                        }
                        Status.TIMEOUT -> {
                            pleaseWaitDialog.dismiss()
                        }
                    }
                }
            } else {
                showAlerter(
                    binding.root,
                    requireActivity(),
                    getString(R.string.fieldsCantBeEmpty)
                ) {}
            }
        }
    }

    private fun initViews() {
        with(binding) {
            activateBtn = activateButton
            acctNumET = accountNumberTIET
        }
    }

    private fun showReactivateAccountResponseBottomSheet(dataToShowInTheBottomSheet: ReactivateAcctBottomSheetData) {
//        setFragmentResult(
//            REACTIVATE_ACCT_BOTTOM_SHEET_DIALOG_RK,
//            bundleOf(REACTIVATE_ACCT_BOTTOM_SHEET_DIALOG_BK to dataToShowInTheBottomSheet)
//        )
        reactivateAccountResponseBottomSheet.show(
            parentFragmentManager,
            REACTIVATE_ACCT_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG
        )
    }
}