package com.keystone.capmobile.ui.view.customerEngagement

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RawRes
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keystone.capmobile.R
import com.keystone.capmobile.data.model.ModalData
import com.keystone.capmobile.data.model.SendMessageRequestBody
import com.keystone.capmobile.databinding.NewCustomerEngagementBinding
import com.keystone.capmobile.ui.interfaces.DrawerLocker
import com.keystone.capmobile.ui.view.dialogFragments.BottomSheetCreateMessageDialog
import com.keystone.capmobile.ui.view.dialogFragments.LoadingDialog
import com.keystone.capmobile.ui.view.dialogFragments.ResponseModal
import com.keystone.capmobile.ui.viewModel.MainViewModel
import com.keystone.capmobile.util.AppConstants.customSnackBar
import com.keystone.capmobile.util.AppConstants.customToast
import com.keystone.capmobile.util.AppConstants.hideKeyboard
import com.keystone.capmobile.util.ConstantsOnly.NETWORK_CALL_RESPONSE_DATA_DIALOG_BUNDLE_KEY
import com.keystone.capmobile.util.ConstantsOnly.NETWORK_CALL_RESPONSE_DATA_DIALOG_REQUEST_KEY
import com.keystone.capmobile.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class CustomerEngagement : Fragment() {
    private lateinit var binding: NewCustomerEngagementBinding

    @Inject
    lateinit var createMessageBottomSheet: BottomSheetCreateMessageDialog
    @Inject
    lateinit var loadingProgressDialog: LoadingDialog
    @Inject
    lateinit var responseModal: ResponseModal
    private lateinit var backArrowImageView: ImageView
    private lateinit var editMessage: ImageView
    private lateinit var backTextView: TextView
    private lateinit var editorET: EditText
    private lateinit var subjectTextView: TextView
    private lateinit var progressBarView: ProgressBar
    private lateinit var fetchLevelLoadingBar: ProgressBar
    private lateinit var selectLevel: AutoCompleteTextView
    private lateinit var selectTransactionStatus: AutoCompleteTextView
    private lateinit var levelsAdapter: ArrayAdapter<String>
    private var levelCodes: ArrayList<String> = arrayListOf()
    private lateinit var statusAdapter: ArrayAdapter<String>
    private lateinit var sendMsgViaKeystoneServ: Button
    private var selectedLevelPosition: Int = -1
    private var selectedTransactionStatus: String = ""
    private var selectedTransactionStatusPosition: Int = -1
    private val viewModel: MainViewModel by viewModels()
    private val transactionStatus = arrayOf("TRANSACTING", "NON-TRANSACTING", "ALL")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.new_customer_engagement,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()

        statusAdapter =
            ArrayAdapter(requireContext(), R.layout.exposed_dropdown_menu_layout, transactionStatus)
        selectTransactionStatus.setAdapter(statusAdapter)

        initializeLevelAdapter()
    }

    override fun onResume() {
        super.onResume()

        // Disable drawer
        (activity as DrawerLocker).setDrawerEnabled(false)
        backArrowImageView.setOnClickListener {
            hideKeyboard()
            findNavController().popBackStack()
        }
        backTextView.setOnClickListener {
            hideKeyboard()
            findNavController().popBackStack()
        }
        // Hide the keyboard by default
        hideKeyboard()

        selectLevel.setOnDismissListener {
            if (levelCodes.isEmpty()) {
                initializeLevelAdapter()
            }
        }

        editMessage.setOnClickListener {
            createMessageBottomSheet.show(parentFragmentManager, "CreateMessage")
        }

        selectLevel.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                selectedLevelPosition = position
                makeTheCallToFetchTheMessage(
                    selectedLevelPosition,
                    selectedTransactionStatusPosition
                )
            }

        selectTransactionStatus.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                selectedTransactionStatusPosition = position
                selectedTransactionStatus = transactionStatus[position]
                makeTheCallToFetchTheMessage(
                    selectedLevelPosition,
                    selectedTransactionStatusPosition
                )
            }

        sendMsgViaKeystoneServ.setOnClickListener {
            clickToSendMessage()
        }
    }

    @ExperimentalCoroutinesApi
    private fun makeTheCallToFetchTheMessage(
        selectedLevelPos: Int,
        selectedTransactionStatusPos: Int
    ) {

        if (selectedLevelPos >= 0) {
            if (selectedTransactionStatusPos >= 0) { // i.e if Level has been selected
                // Get selected items in the autocomplete textview adapters
                val selectedLvl = levelCodes[selectedLevelPos]
                val selectedTransactStus = statusAdapter.getItem(selectedTransactionStatusPos)

                // Show progress bar
                progressBarView.visibility = View.VISIBLE

                if (selectedTransactStus != null) {
                    viewModel.fetchMessage(selectedLvl, selectedTransactStus)
                }
                viewModel.messageFetchedFromTheBackEnd.observe(viewLifecycleOwner, {
                    it.data?.message?.let { it1 -> Log.d("message", it1) }

                    // Clear the previous text
                    editorET.text.clear()
                    subjectTextView.text = ""

                    when (it.status) {
                        Status.LOADING -> {
                            progressBarView.visibility = View.VISIBLE
                        }

                        Status.SUCCESS -> {
                            val catchMessage =
                                if ((it.data?.details?.Message).isNullOrEmpty()) getString(R.string.noMessageFoundForThis) else it.data?.details?.Message

                            val catchMessageSubject =
                                if ((it.data?.details?.Subject).isNullOrEmpty()) "" else it.data?.details?.Subject

                            Log.d("Subjecttttttt", catchMessageSubject ?: "NOTHING")

                            progressBarView.visibility = View.GONE
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                subjectTextView.text = catchMessageSubject.toString()
                                editorET.setText(
                                    Html.fromHtml(
                                        catchMessage,
                                        Html.FROM_HTML_MODE_COMPACT
                                    )
                                )
                            } else {
                                editorET.setText(Html.fromHtml(catchMessage))
                            }
                        }

                        Status.TIMEOUT -> {
                            progressBarView.visibility = View.GONE
                            customSnackBar(binding.root, getString(R.string.takeTooLong))
                        }

                        Status.ERROR -> {
                            customSnackBar(binding.root, getString(R.string.errorFetchingMessage))
                            progressBarView.visibility = View.GONE
                            editorET.setText(it.data?.status)
                        }
                    }
                })
            } else {
                customToast(getString(R.string.transactionStatusNotSelected))
            }
        } else {
            customToast(getString(R.string.pleaseSelectALevel))
        }
    }

    private fun initializeViews() {
        with(binding) {
            progressBarView = progressBar
            editorET = textAreaInformation
            selectLevel = levelsDropDown
            selectTransactionStatus = statusDropDown
            backTextView = backText
            backArrowImageView = backArrow
            sendMsgViaKeystoneServ = engageViaKeystoneService
            fetchLevelLoadingBar = progressBarForGetLevels
            subjectTextView = messageSubject
            editMessage = createNewMessage
        }
    }

    private fun initializeLevelAdapter() {
        viewModel.getLevelsAndDescription()

        viewModel.getLevelsAndDescription.observe(viewLifecycleOwner, { lvlAndDescription ->
            when (lvlAndDescription.status) {
                Status.SUCCESS -> {
                    fetchLevelLoadingBar.visibility = View.INVISIBLE
                    levelCodes =
                        lvlAndDescription.data?.map { receivedData -> receivedData.Level } as ArrayList<String>
                    Log.d("levels", levelCodes.toString())
                    // Set adapter to recyclerView
                    levelsAdapter = ArrayAdapter(
                        requireContext(),
                        R.layout.spinner_drop_down,
                        lvlAndDescription.data.map { lvlName -> lvlName.LevelName } as ArrayList<String>
                    )
                    selectLevel.setAdapter(levelsAdapter)
                }
                Status.ERROR -> {
                    fetchLevelLoadingBar.visibility = View.INVISIBLE
                    customSnackBar(binding.root, getString(R.string.errorGettingLevels))
                }

                Status.LOADING -> {
                    fetchLevelLoadingBar.visibility = View.VISIBLE
                }
                Status.TIMEOUT -> {
                    fetchLevelLoadingBar.visibility = View.INVISIBLE
                    customSnackBar(
                        binding.root,
                        getString(R.string.takeTooLong)
                    )
                }
            }
        })
    }

    private fun clickToSendMessage() {
        val message = SendMessageRequestBody(
            levelCodes[selectedLevelPosition],
            editorET.text.toString(),
            subjectTextView.text.toString(),
            selectedTransactionStatus
        )
        viewModel.sendMessage(message)
        observeSendMessageResponse()
    }

    private fun observeSendMessageResponse() {
        viewModel.sendMessageResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    loadingProgressDialog.show(parentFragmentManager, "LOADER")
                }
                Status.SUCCESS -> {
                    loadingProgressDialog.dismiss()
                    showModal(R.raw.success, getString(R.string.messageSentSuccessfully))
                }
                Status.ERROR -> {
                    loadingProgressDialog.dismiss()
                    showModal(R.raw.failed, getString(R.string.messageWasNotSent))
                }
                Status.TIMEOUT -> {
                    loadingProgressDialog.dismiss()
                    showModal(R.raw.success, getString(R.string.messageSentSuccessfully))
                }
            }
        }
    }

    private fun showModal(@RawRes lottieIcon: Int, message: String) {
        val modalData = ModalData(
            lottieIcon,
            message
        )
        setFragmentResult(
            NETWORK_CALL_RESPONSE_DATA_DIALOG_REQUEST_KEY,
            bundleOf(NETWORK_CALL_RESPONSE_DATA_DIALOG_BUNDLE_KEY to modalData)
        )

        if (findNavController().currentDestination?.id != R.id.action_customerEngagement_to_networkResponseModal) {
            val action  = CustomerEngagementDirections.actionCustomerEngagementToNetworkResponseModal()
            findNavController().navigate(action)
        }
    }

}