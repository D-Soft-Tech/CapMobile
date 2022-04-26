package com.keystone.capmobile.ui.view.openAccountScreens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.airbnb.paris.extensions.style
import com.keystone.capmobile.R
import com.keystone.capmobile.data.dataStore.AppProtoDataStore
import com.keystone.capmobile.data.model.BvnValidationRequestModel
import com.keystone.capmobile.databinding.FragmentOpenAccountBvnScreenBinding
import com.keystone.capmobile.di.BaseUrlSetterInterceptor
import com.keystone.capmobile.ui.viewModel.OpenAccountViewModel
import com.keystone.capmobile.util.AppConstants.customSnackBar
import com.keystone.capmobile.util.DataStoreManager
import com.keystone.capmobile.util.Status
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OpenAccountBvnScreen @Inject constructor() : Fragment() {
    @Inject
    lateinit var dataStore: DataStoreManager
    @Inject
    lateinit var hostSelectionInterceptor: BaseUrlSetterInterceptor
    @Inject
    lateinit var protoDataStore: AppProtoDataStore
    private lateinit var binding: FragmentOpenAccountBvnScreenBinding
    private val viewModel: OpenAccountViewModel by activityViewModels()
    private lateinit var selectAProductTv: AutoCompleteTextView
    private lateinit var progressBarView: ProgressBar
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var bvnTieT: TextInputEditText
    private lateinit var existingAccountView: TextInputEditText
    private lateinit var daoCode: TextInputEditText
    private lateinit var continueBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_open_account_bvn_screen,
            container,
            false
        )

        arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.exposed_dropdown_menu_layout,
            resources.getStringArray(R.array.products)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        // Set adapter to the autoCompleteTextView Dropdown
        selectAProductTv.setAdapter(arrayAdapter)
        continueBtn.setOnClickListener {
            (requireParentFragment() as NewAccountOpeningViewPager).gotoNextStep()
        }
    }

    private fun initializeViews() {
        with(binding) {
            selectAProductTv = accountTypeTIET
            bvnTieT = bvn
            existingAccountView = existingAccount
            daoCode = daoCodeTIET
            continueBtn = proceedBtn
            progressBarView = progressBar2
        }
    }

    override fun onResume() {
        super.onResume()
        // Fetch the account officer's daoCode from protoDataStore
        // and prefill it to the daoCode textInputEditText
        lifecycleScope.launch {
            protoDataStore.readProto.collect {
                daoCode.setText(it.daoCode)
            }
        }
        bvnTieT.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length < 11 || s.toString().length > 11) {
                    ((bvnTieT.parent.parent) as TextInputLayout).apply {
                        style(R.style.lekanTilStyleForErrorBorder)
                        bvnTieT.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_error,
                            0
                        )
                    }
                } else {
                    ((bvnTieT.parent.parent) as TextInputLayout).apply {
                        viewModel.validateBvn(
                            BvnValidationRequestModel(
                                bvn = bvnTieT.text.toString(),
                                channelname = "Mobile",
                                username = "SYSTEM"
                            )
                        )

                        viewModel.bvnResponse.observe(viewLifecycleOwner, {

                            when (it.status) {
                                Status.LOADING -> {
                                    progressBarView.visibility = View.VISIBLE
                                }

                                Status.ERROR -> {
                                    progressBarView.visibility = View.INVISIBLE
                                    customSnackBar(
                                        binding.root,
                                        getString(R.string.bvnDoesNotExist)
                                    )
                                }

                                Status.TIMEOUT -> {
                                    progressBarView.visibility = View.INVISIBLE
                                    customSnackBar(binding.root, getString(R.string.timeOut))
                                }

                                Status.SUCCESS -> {
                                    it.data?.resultStatus?.let { it1 -> Log.d("obbseerv", it1) }
                                    if (it.data?.resultStatus == "00") {
                                        progressBarView.visibility = View.INVISIBLE
                                        style(R.style.lekanTilStyleForSuccess)
                                        bvnTieT.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                            0,
                                            0,
                                            R.drawable.ic_lekan_check_icon,
                                            0
                                        )
                                    } else {
                                        progressBarView.visibility = View.INVISIBLE
                                        customSnackBar(
                                            binding.root,
                                            getString(R.string.bvnDoesNotExist)
                                        )
                                    }
                                }
                            }
                        })
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        selectAProductTv.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                viewModel._selectedProductCode.value = position
            }
    }

}