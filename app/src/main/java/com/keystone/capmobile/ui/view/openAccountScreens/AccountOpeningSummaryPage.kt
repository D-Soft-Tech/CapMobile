package com.keystone.capmobile.ui.view.openAccountScreens

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.keystone.capmobile.R
import com.keystone.capmobile.data.dataStore.AppProtoDataStore
import com.keystone.capmobile.databinding.FragmentAccountOpeningSummaryPageBinding
import com.keystone.capmobile.databinding.OpenAccountSummaryPdfLayoutBinding
import com.keystone.capmobile.ui.viewModel.OpenAccountViewModel
import com.keystone.capmobile.util.AppConstants
import com.keystone.capmobile.util.AppConstants.WRITE_PERMISSION_REQUEST_CODE
import com.keystone.capmobile.util.AppConstants.customToast
import com.keystone.capmobile.util.AppConstants.genericPermissionHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class AccountOpeningSummaryPage @Inject constructor() : Fragment(),
    EasyPermissions.PermissionCallbacks {
    private lateinit var pdFData: AppConstants.AccountSummary

    @Inject
    lateinit var protoDataStore: AppProtoDataStore
    val viewModel: OpenAccountViewModel by activityViewModels()
    private lateinit var binding: FragmentAccountOpeningSummaryPageBinding
    private lateinit var pdfView: OpenAccountSummaryPdfLayoutBinding
    private lateinit var accountDetails: File
    private lateinit var downloadImage: ImageView
    private lateinit var shareImageView: ImageView
    private lateinit var accountNameTv: TextView
    private lateinit var accountNumberTv: TextView
    private lateinit var accountTypeTv: TextView
    private lateinit var branchAddressTv: TextView
    private lateinit var branchCodeTv: TextView
    private lateinit var accountOfficerNameTv: TextView
    private lateinit var accountOfficerPhoneNumberTv: TextView
    private lateinit var finishBtn: Button
    private lateinit var backArrowTv: ImageView
    private lateinit var backTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_account_opening_summary_page,
            container,
            false
        )

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_accountOpeningSummaryPage_to_dashboardNew)
                findNavController()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()

        // Pdf layout inflater
        val inflater = LayoutInflater.from(context)
        pdfView = OpenAccountSummaryPdfLayoutBinding.inflate(inflater)

        lifecycleScope.launch {
            protoDataStore.readProto.collect { accountOfficer ->
                with(viewModel) {
                    _accountOfficerPhoneNumber.observe(viewLifecycleOwner) { phoneNumberOfAccountOfficer ->
                        bvnResponse.observe(viewLifecycleOwner) { userBvn ->
                            _selectedProductCode.observe(viewLifecycleOwner) { selectedProductCode ->
                                userBvn.data!!.bvnSearchResult.let { bvnSearchResult ->
                                    viewModel.selectedBankBranch.observe(viewLifecycleOwner) { selectedBranch ->
                                        pdFData = AppConstants.AccountSummary(
                                            "${bvnSearchResult.lastName} ${bvnSearchResult.firstName} ${bvnSearchResult.middleName}",
                                            "1234567890",
                                            accType = selectedProductCode.toString(),
                                            accOfficer = accountOfficer.fullName,
                                            accOfficerNum = phoneNumberOfAccountOfficer,
                                            branchAddress = selectedBranch.BranchAddress,
                                            branchCode = selectedBranch.BranchCode
                                        )

                                        // Set branchCode and BranchAddress for this summary page, not the pdf layout o
                                        branchCodeTv.text = getString(
                                            R.string.account_branch_code_place_holder,
                                            selectedBranch.BranchCode
                                        )
                                        branchAddressTv.text = getString(
                                            R.string.account_branch_place_holder,
                                            selectedBranch.BranchAddress
                                        )

                                        // Set text to the textViews on the pdf layout
                                        pdfView.apply {
                                            accountName.text = getString(
                                                R.string.account_name_place_holder,
                                                pdFData.accName
                                            )
                                            branchCode.text = getString(
                                                R.string.account_branch_code_place_holder,
                                                pdFData.branchCode
                                            )
                                            branchAddress.text = getString(
                                                R.string.account_branch_place_holder,
                                                pdFData.branchAddress
                                            )
                                            accountNumber.text = getString(
                                                R.string.account_number_place_holder,
                                                pdFData.accNumber
                                            )
                                            accountType.text =
                                                getString(
                                                    R.string.account_type_place_holder,
                                                    pdFData.accType
                                                )
                                            accountOfficerName.text =
                                                getString(
                                                    R.string.account_officer_name_place_holder,
                                                    pdFData.accOfficer
                                                )
                                            accountOfficerNumber.text =
                                                getString(
                                                    R.string.account_officer_phone_number_place_holder,
                                                    pdFData.accOfficerNum
                                                )
                                        }

                                        setAccountDetailsTextViews(pdFData)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        downloadImage.setOnClickListener {
            genericPermissionHandler(
                this@AccountOpeningSummaryPage,
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                WRITE_PERMISSION_REQUEST_CODE,
                getString(R.string.storage_permission_rationale_for_download),
            ) {
                createPdf()
                customToast(
                    getString(R.string.fileDownloaded)
                )
            }
        }

        shareImageView.setOnClickListener {
            // First create the Pdf
            createPdf()
            sharePdf(accountDetails)
        }

        finishBtn.setOnClickListener {
            findNavController().navigate(R.id.action_accountOpeningSummaryPage_to_dashboardNew)
        }

        backTextView.setOnClickListener {
            findNavController().navigate(R.id.action_accountOpeningSummaryPage_to_dashboardNew)
        }

        backArrowTv.setOnClickListener {
            findNavController().navigate(R.id.action_accountOpeningSummaryPage_to_dashboardNew)
        }
    }

    private fun initializeViews() {
        with(binding) {
            downloadImage = downLoadIcon
            shareImageView = shareIcon
            accountNameTv = accountName
            accountNumberTv = accountNumber
            accountTypeTv = accountType
            accountOfficerNameTv = accountOfficerName
            accountOfficerPhoneNumberTv = accountOfficerPhoneNumber
            finishBtn = submitBtn
            backArrowTv = backArrow
            backTextView = backText
            branchAddressTv = accountBranch
            branchCodeTv = accountBranchCode
        }
    }

    private fun createPdf() {
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context?.display?.getRealMetrics(displayMetrics)
            displayMetrics.densityDpi
        } else {
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        }

        pdfView.root.measure(
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.heightPixels, View.MeasureSpec.EXACTLY
            )
        )

        pdfView.root.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)

        val bitmap = view?.measuredWidth?.let {
            pdfView.root.measuredHeight.let { it1 ->
                Bitmap.createBitmap(
                    it,
                    it1, Bitmap.Config.ARGB_8888
                )
            }
        }

        val canvas = bitmap?.let { Canvas(it) }
        pdfView.root.draw(canvas)

        if (bitmap != null) {
            Bitmap.createScaledBitmap(bitmap, 595, 842, true)
        }
        val pdfDocument = PdfDocument()
        val pageInfo = bitmap?.let { PdfDocument.PageInfo.Builder(it.width, it.height, 1).create() }
        val page = pdfDocument.startPage(pageInfo)
        if (bitmap != null) {
            page.canvas.drawBitmap(bitmap, 0F, 0F, null)
        }
        pdfDocument.finishPage(page)
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "KeystoneAccountDetails" + ".pdf"
        )
        Log.d("pdfNew", "$filePath")

        accountDetails = filePath
        pdfDocument.writeTo(FileOutputStream(filePath))
        pdfDocument.close()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            AppConstants.STORAGE_PERM_REQUEST_CODE -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.storage_permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(requireContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT)
            .show()
    }

    private fun setAccountDetailsTextViews(accountDetails: AppConstants.AccountSummary) {
        with(accountDetails) {
            accountNameTv.text = getString(R.string.account_name_place_holder, accName)
            accountNumberTv.text = getString(R.string.account_number_place_holder, accNumber)
            accountTypeTv.text = getString(R.string.account_type_place_holder, accType)
            accountOfficerNameTv.text =
                getString(R.string.account_officer_name_place_holder, accOfficer)
            accountOfficerPhoneNumberTv.text =
                getString(R.string.account_officer_phone_number_place_holder, accOfficerNum)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    // share the pdf file
    private fun sharePdf(outputFile: File) {

        // This is the way to do it for targetSdkVersion < 24
//        val uri: Uri = Uri.fromFile(outputFile)
        // For targetSdkVersion >= 24
        val uri: Uri = FileProvider.getUriForFile(
            requireContext(),
            context?.applicationContext?.packageName + ".provider",
            outputFile
        )
        val share = Intent().apply {
            action = Intent.ACTION_SEND
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        startActivity(share)
    }
}