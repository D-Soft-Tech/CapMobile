package com.keystone.capmobile.ui.view.openAccountScreens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import com.airbnb.paris.extensions.style
import com.keystone.capmobile.R
import com.keystone.capmobile.data.model.GetBankBranchResponse
import com.keystone.capmobile.data.model.UserAddress
import com.keystone.capmobile.databinding.FragmentOpenAccountAddressScreenBinding
import com.keystone.capmobile.ui.view.dialogFragments.SelectBranchBottomSheetDialogFragment
import com.keystone.capmobile.ui.viewModel.OpenAccountViewModel
import com.keystone.capmobile.util.AppConstants.customToast
import com.keystone.capmobile.util.ConstantsOnly.SELECT_BRANCH_BOTTOM_SHEET_FRAGMENT_TAG
import com.keystone.capmobile.util.ConstantsOnly.SELECT_BRANCH_BUNDLE_KEY
import com.keystone.capmobile.util.ConstantsOnly.SELECT_BRANCH_REQUEST_KEY
import com.keystone.capmobile.util.ConstantsOnly.emailValidator
import com.keystone.capmobile.util.ConstantsOnly.handleEditTextDrawableRightClick
import com.keystone.capmobile.util.ConstantsOnly.notifyErrorValidation
import com.keystone.capmobile.util.ConstantsOnly.notifySuccessfulValidationWithOnlyCheck
import com.keystone.capmobile.util.ConstantsOnly.phoneNumberValidator
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OpenAccountAddressScreen @Inject constructor() : Fragment() {
    private val viewModel: OpenAccountViewModel by activityViewModels()

    @Inject
    lateinit var selectBranchBottomSheet: SelectBranchBottomSheetDialogFragment
    private lateinit var binding: FragmentOpenAccountAddressScreenBinding
    private lateinit var selectBranch: TextInputEditText
    private lateinit var customerAddress: TextInputEditText
    private lateinit var customerCity: TextInputEditText
    private lateinit var customerState: AutoCompleteTextView
    private lateinit var customerPhoneNumber: TextInputEditText
    private lateinit var customerEmail: TextInputEditText
    private lateinit var previousBtn: Button
    private lateinit var nextBtn: Button
    private var selectedBranchFromBottomSheet: GetBankBranchResponse? = null
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private var branchErrorTracker = true
    private var errorTracker = HashMap<String, String>()
    private lateinit var states: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(SELECT_BRANCH_REQUEST_KEY) { _, bundle ->
            selectedBranchFromBottomSheet = bundle.getParcelable(SELECT_BRANCH_BUNDLE_KEY)
            selectedBranchFromBottomSheet?.run {
                viewModel.setSelectedBankBranch(this@run)
                selectBranch.setText(BranchName)
                branchErrorTracker = false
                selectBranch.style(R.style.lekanTilStyle)
                customToast(getString(R.string.preferred_branch_selected))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_open_account_address_screen,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        states = requireContext().resources.getStringArray(R.array.nigeria_states)
        arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, states)
        initializeViews()
        Log.d("statesss", states.contentToString())
        customerState.apply {
            setAdapter(arrayAdapter)
            onItemClickListener =
                AdapterView.OnItemClickListener { parent, _, position, _ ->
                    val selected = parent?.getItemAtPosition(position).toString()
                    if (states.contains(selected) && selected != "Select a State") {
                        errorTracker["state"]?.let { errorTracker.remove("state") }
                        customerState.style(R.style.lekanTilStyle)
                    }
                }
        }
    }

    override fun onResume() {
        super.onResume()
        previousBtn.setOnClickListener {
            (requireParentFragment() as NewAccountOpeningViewPager).gotoPreviousStep()
        }

        nextBtn.setOnClickListener {
            handleValidationOfAllTextInputEditTexts()
            if (errorTracker.size == 0 && !branchErrorTracker) {
                val userAddress = UserAddress(
                    customerAddress.text.toString(),
                    customerCity.text.toString(),
                    customerState.text.toString(),
                    customerPhoneNumber.text.toString(),
                    customerEmail.text.toString(),
                    selectedBranchFromBottomSheet!!.BranchCode
                )
                viewModel.setUserAddress(userAddress)
                (requireParentFragment() as NewAccountOpeningViewPager).gotoNextStep()
            }
        }
        handleUndoingAllErrors()

        // Select Branch
        selectBranch.handleEditTextDrawableRightClick {
            handleShowingOfSelectBranchBottomSheet()
        }
    }

    private fun initializeViews() {
        with(binding) {
            selectBranch = branch
            previousBtn = previousPage
            nextBtn = proceedBtn
            customerAddress = residentialAddress
            customerCity = residentialCity
            customerState = residentialState
            customerPhoneNumber = phoneNumber
            customerEmail = email
        }
    }

    private fun handleValidationOfAllTextInputEditTexts() {
        customerAddress.classPrivateValidation("address", "error")
        customerCity.classPrivateValidation("city", "error")
        // Validate that the user has selected state
        if (!states.contains(customerState.text.toString())) {
            errorTracker["state"] = "error"
            customerState.notifyErrorValidation()
        }

        if (!phoneNumberValidator(customerPhoneNumber.text.toString())) {
            errorTracker["phoneNumber"] = "error"
            customerPhoneNumber.apply {
                notifyErrorValidation()
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
        }

        if (selectBranch.text.isNullOrEmpty()) {
            selectBranch.notifyErrorValidation()
        }

        if (!emailValidator(customerEmail.text.toString())) {
            errorTracker["email"] = "error"
            customerEmail.apply {
                notifyErrorValidation()
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
        }
    }

    private fun TextInputEditText.classPrivateValidation(key: String, value: String) {
        if (text.toString().length < 3) {
            errorTracker[key] = value
            notifyErrorValidation()
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
    }

    private fun TextInputEditText.undoErrorOnIndividual(key: String) {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.length >= 3) {
                        notifySuccessfulValidationWithOnlyCheck()
                        errorTracker[key]?.let { errorTracker.remove(key) }
                    } else {
                        errorTracker[key] = "error"
                        notifyErrorValidation()
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.length >= 3) {
                        notifySuccessfulValidationWithOnlyCheck()
                        errorTracker[key]?.let { errorTracker.remove(key) }
                    } else {
                        errorTracker[key] = "error"
                        notifyErrorValidation()
                    }
                }
            }
        })
    }

    private fun handleUndoingAllErrors() {
        customerAddress.undoErrorOnIndividual("address")
        customerCity.undoErrorOnIndividual("city")
        customerPhoneNumber.handleUndoErrorOfPhoneNumberAndEmailValidation("phoneNumber")
        customerEmail.handleUndoErrorOfPhoneNumberAndEmailValidation("email")
    }

    private fun TextInputEditText.handleUndoErrorOfPhoneNumberAndEmailValidation(key: String) {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    this@handleUndoErrorOfPhoneNumberAndEmailValidation.abstractPhoneAndEmailValidation(
                        s.toString(),
                        key
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    this@handleUndoErrorOfPhoneNumberAndEmailValidation.abstractPhoneAndEmailValidation(
                        s.toString(),
                        key
                    )
                }
            }
        })
    }

    private fun TextInputEditText.abstractPhoneAndEmailValidation(character: String, key: String) {
        if (key == "phoneNumber") {
            // this means it is phoneNumber input field
            if (phoneNumberValidator(character)) {
                notifySuccessfulValidationWithOnlyCheck()
                errorTracker[key]?.let { errorTracker.remove(key) }
            } else {
                errorTracker[key] = "error"
                notifyErrorValidation()
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
        } else {
            // Then it is email input field
            if (emailValidator(character)) {
                notifySuccessfulValidationWithOnlyCheck()
                errorTracker[key]?.let { errorTracker.remove(key) }
            } else {
                errorTracker[key] = "error"
                notifyErrorValidation()
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
        }
    }

    private fun handleShowingOfSelectBranchBottomSheet() {
        selectBranchBottomSheet.show(parentFragmentManager, SELECT_BRANCH_BOTTOM_SHEET_FRAGMENT_TAG)
    }
}