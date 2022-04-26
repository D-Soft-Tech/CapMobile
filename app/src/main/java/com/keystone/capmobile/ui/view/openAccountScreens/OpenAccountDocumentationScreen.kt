package com.keystone.capmobile.ui.view.openAccountScreens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.airbnb.paris.extensions.style
import com.keystone.capmobile.R
import com.keystone.capmobile.databinding.FragmentOpenAccountDocumentationScreenBinding
import com.keystone.capmobile.ui.viewModel.OpenAccountViewModel
import com.keystone.capmobile.util.AppConstants
import com.keystone.capmobile.util.AppConstants.bitmapToBase64
import com.keystone.capmobile.util.AppConstants.cameraPermissionHandler
import com.keystone.capmobile.util.AppConstants.customSnackBar
import com.keystone.capmobile.util.AppConstants.galleryPermissionHandler
import com.keystone.capmobile.util.AppConstants.getDialog
import com.keystone.capmobile.util.AppConstants.getFileName
import com.keystone.capmobile.util.AppConstants.getImageUriFromBitmap
import com.keystone.capmobile.util.Status
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class OpenAccountDocumentationScreen @Inject constructor() : Fragment(),
    EasyPermissions.PermissionCallbacks {
    private val viewModel: OpenAccountViewModel by activityViewModels()
    private lateinit var binding: FragmentOpenAccountDocumentationScreenBinding
    private lateinit var customerProfilePicture: TextInputEditText
    private lateinit var idCard: TextInputEditText
    private lateinit var utility: TextInputEditText
    private lateinit var customerSignature: TextInputEditText
    private lateinit var accountOfficerPhoneNumber: TextInputEditText
    private lateinit var submitButton: Button
    private var errorTracker = HashMap<String, String>()

    private var currentProfileImageActivityResultAsBitMap: Bitmap? = null
    private var currentUtilityImageActivityResultAsBitMap: Bitmap? = null

    // Camera launchers
    private lateinit var profileImageCameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var customerSignatureImageCameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var idCardImageCameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var utilityImageCameraLauncher: ActivityResultLauncher<Intent>

    // XD Card Launchers
    private lateinit var profileImageXDCardLauncher: ActivityResultLauncher<String>
    private lateinit var customerSignatureImageXDCardLauncher: ActivityResultLauncher<String>
    private lateinit var idCardImageXDCardLauncher: ActivityResultLauncher<String>
    private lateinit var utilityImageXDCardLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requireActivity().supportFragmentManager.setFragmentResultListener(
//            SET_SIGNATURE_REQUEST_KEY,
//            requireActivity()
//        ) { _, bundle ->
//            Log.d("Result", bundle.getString(SET_SIGNATURE_BUNDLE_KEY).toString())
//            confirmationThatSignatureHasBeenUploaded = bundle.getString(SET_SIGNATURE_BUNDLE_KEY)
//            confirmationThatSignatureHasBeenUploaded?.let { confirm ->
//                Log.d("confirm", confirm)
//                customToast(confirm)
//                customerSignature.setText(confirm)
//                customerProfilePicture.setText(confirm)
//            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_open_account_documentation_screen,
            container,
            false
        )

        binding.apply {
            openAccountViewModel = viewModel
            lifecycleOwner = this@OpenAccountDocumentationScreen
            executePendingBindings()
        }
        initializeViews()
        // Initialize camera launchers by registering for activity result
        initializeCameraLaunchers()
        // Initialize gallery launchers by registering for activity result
        initializeGalleryLaunchers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle click
        customerProfilePicture.handleClick(
            profileImageCameraLauncher,
            profileImageXDCardLauncher,
            "profilePicture"
        )
        customerSignature.handleClick(
            customerSignatureImageCameraLauncher,
            customerSignatureImageXDCardLauncher,
            "signature"
        )
        idCard.handleClick(idCardImageCameraLauncher, idCardImageXDCardLauncher, "idCard")
        utility.handleClick(utilityImageCameraLauncher, utilityImageXDCardLauncher, "utility")


    }

    override fun onResume() {
        super.onResume()
        with(viewModel) {
            wasUserSignatureSetViaDrawingTheSignature.observe(viewLifecycleOwner) {
                if (it) {
                    (customerSignature.apply {
                        setText(getString(R.string.signatureUploaded))
                    }.parent.parent as TextInputLayout).apply {
                        style(R.style.lekanAccountOpeningDocumentationSuccessStyle)
                    }
                }
            }
        }

        submitButton.setOnClickListener {
            // First validate inputs
            validateAllInputs()
            if (errorTracker.isEmpty()) {
                viewModel.bvnResponse.observe(viewLifecycleOwner, { bvnRes ->
                    when (bvnRes.status) {
                        Status.SUCCESS -> {
                            bvnRes.data
                        }
                        Status.TIMEOUT -> {}
                        Status.ERROR -> {}
                        Status.LOADING -> {}
                    }
                    viewModel.openAccount()
                    viewModel.openAccountResponse.observe(viewLifecycleOwner) {
                        when(it.status) {
                            Status.LOADING -> {}
                            Status.ERROR -> {
                                customSnackBar(binding.root, getString(R.string.errorPleaseTryAgain))
                                findNavController().navigate(R.id.action_newAccountOpeningViewPager_to_accountOpeningSummaryPage)
                            }
                            Status.TIMEOUT -> {
                                customSnackBar(binding.root, getString(R.string.timeOut))
                            }
                            Status.SUCCESS -> {
                                findNavController().navigate(R.id.action_newAccountOpeningViewPager_to_accountOpeningSummaryPage)
                            }
                        }
                    }
                })
            }
        }
    }

    private fun initializeViews() {
        with(binding) {
            accountOfficerPhoneNumber = accountOfficerPhoneNumberTIET
            customerProfilePicture = profilePicture
            idCard = validId
            utility = utilityBill
            customerSignature = signature
            submitButton = submitBtn
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when {
            requestCode == AppConstants.STORAGE_PERM_REQUEST_CODE -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.storage_permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
            requestCode == AppConstants.CAMERA_PERM_REQUEST_CODE -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.camera_permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
            perms.size > 1 -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(requireContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun initializeCameraLaunchers() {
        profileImageCameraLauncher =
            registerActivityResultLauncher(customerProfilePicture, "profilePicture")
        customerSignatureImageCameraLauncher =
            registerActivityResultLauncher(customerSignature, "signature")
        idCardImageCameraLauncher = registerActivityResultLauncher(idCard, "idCard")
        utilityImageCameraLauncher = registerActivityResultLauncher(utility, "utility")
    }

    private fun initializeGalleryLaunchers() {
        profileImageXDCardLauncher =
            registerGalleryLauncher(customerProfilePicture, "profilePicture")
        customerSignatureImageXDCardLauncher =
            registerGalleryLauncher(customerSignature, "signature")
        idCardImageXDCardLauncher = registerGalleryLauncher(idCard, "idCard")
        utilityImageXDCardLauncher = registerGalleryLauncher(utility, "utility")
    }

    private fun registerActivityResultLauncher(
        textInputEditText: TextInputEditText,
        documentTypeTag: String
    ): ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val bitmap = it.data?.extras?.get("data") as Bitmap
                currentUtilityImageActivityResultAsBitMap = bitmap
                val fileName = getFileName(
                    getImageUriFromBitmap(
                        requireContext(),
                        bitmap
                    ), requireContext()
                )
                textInputEditText.setText(fileName)
                bitmapToBase64(bitmap)?.let { base64FormatOfImageFromGallery ->
                    when (documentTypeTag) {
                        "profilePicture" -> {
                            viewModel.setUserPassport(base64FormatOfImageFromGallery)
                        }
                        "idCard" -> {
                            viewModel.setUserIdCard(base64FormatOfImageFromGallery)
                        }
                        "utility" -> {
                            viewModel.setUserUtilityBill(base64FormatOfImageFromGallery)
                        }
                        "signature" -> {
                            viewModel.setUserSignature(base64FormatOfImageFromGallery, false)
                        }
                    }
                }
                errorTracker[documentTypeTag]?.let { errorTracker.remove(documentTypeTag) }
                textInputEditText.style(R.style.lekanAccountOpeningDocumentationSuccessStyle)
            }
        }

    private fun registerGalleryLauncher(
        textInputEditText: TextInputEditText,
        documentTypeTag: String
    ): ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            val bitmap = MediaStore.Images.Media.getBitmap(
                activity?.contentResolver, it
            )
            currentProfileImageActivityResultAsBitMap = bitmap
            val fileName = getFileName(it, requireContext())
            textInputEditText.setText(fileName)

            bitmapToBase64(bitmap)?.let { base64FormatOfImageFromGallery ->
                when (documentTypeTag) {
                    "profilePicture" -> {
                        viewModel.setUserPassport(base64FormatOfImageFromGallery)
                    }
                    "idCard" -> {
                        viewModel.setUserIdCard(base64FormatOfImageFromGallery)
                    }
                    "utility" -> {
                        viewModel.setUserUtilityBill(base64FormatOfImageFromGallery)
                    }
                    "signature" -> {
                        textInputEditText.setText(fileName)
                        viewModel.setUserSignature(base64FormatOfImageFromGallery, false)
                    }
                }
            }
            errorTracker[documentTypeTag]?.let { errorTracker.remove(documentTypeTag) }
            textInputEditText.style(R.style.lekanAccountOpeningDocumentationSuccessStyle)
        }

    @SuppressLint("ClickableViewAccessibility")
    private fun TextInputEditText.handleClick(
        cameraLauncher: ActivityResultLauncher<Intent>,
        galleryLauncher: ActivityResultLauncher<String>,
        documentTypeTag: String
    ) {
        this.setOnTouchListener(View.OnTouchListener { _, event ->
            val drawableLeft = 0
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= this.left - this.compoundDrawables[drawableLeft].bounds.width()
                ) {
                    if (documentTypeTag == "signature") {
                        getDialog(requireContext(),
                            resources.getStringArray(R.array.cameraOrGalleryOrDrawStringArray),
                            {
                                cameraPermissionHandler(
                                    this@OpenAccountDocumentationScreen,
                                    requireContext()
                                ) {
                                    cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                                }
                            },
                            {
                                galleryPermissionHandler(
                                    this@OpenAccountDocumentationScreen,
                                    requireContext()
                                ) {
                                    galleryLauncher.launch("image/*")
                                }
                            },
                            {
                                findNavController().navigate(R.id.action_newAccountOpeningViewPager_to_drawYourSignatureFragment2)
                            }
                        ).show()
                    } else {
                        // show Bottom Sheet
                        getDialog(
                            requireContext(),
                            resources.getStringArray(R.array.cameraOrGalleryStringArray),
                            {
                                cameraPermissionHandler(
                                    this@OpenAccountDocumentationScreen,
                                    requireContext()
                                ) {
                                    cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                                }
                            },
                            {
                                galleryPermissionHandler(
                                    this@OpenAccountDocumentationScreen,
                                    requireContext()
                                ) {
                                    galleryLauncher.launch("image/*")
                                }
                            }
                        ).show()
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun validateAllInputs() {
        customerProfilePicture.validateInput("profilePicture", "error")
        idCard.validateInput("idCard", "error")
        utility.validateInput("utility", "error")
        customerSignature.validateInput("signature", "error")
    }

    private fun TextInputEditText.validateInput(key: String, value: String) {
        if (text.isNullOrEmpty()) {
            errorTracker[key] = value
            style(R.style.lekanAccountOpeningDocumentationErrorStyle)
        }
    }
}