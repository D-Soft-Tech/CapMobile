package com.example.capmobile.ui.reactivateAccount

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentReactivateAccountDocumentationBinding
import com.example.capmobile.databinding.UploadDocumentsBinding
import com.example.capmobile.ui.openAccountScreens.FragmentOpenAccount
import com.example.capmobile.util.AppConstants
import pub.devrel.easypermissions.EasyPermissions

class ReactivateAccountDocumentation : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: FragmentReactivateAccountDocumentationBinding
    private var currentUriGottenFromExdCardLauncherInBitmap: Bitmap? =
        null // To keep track of the image in the user profile image's image view
    private var currentActivityResultAsBitMap: Bitmap? = null
    private var currentProfileImageActivityResultAsBitMap: Bitmap? = null
    private var currentUtilityImageActivityResultAsBitMap: Bitmap? = null
    private lateinit var profilePictureIV: ImageView
    private lateinit var signatureIV: ImageView
    private lateinit var idCardIv: ImageView
    private lateinit var utilityIV: ImageView

    // Camera launchers
    private lateinit var profileImageCameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var signatureImageCameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var idCardImageCameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var utilityImageCameraLauncher: ActivityResultLauncher<Intent>

    // XD Card Launchers
    private lateinit var profileImageXDCardLauncher: ActivityResultLauncher<String>
    private lateinit var signatureImageXDCardLauncher: ActivityResultLauncher<String>
    private lateinit var idCardImageXDCardLauncher: ActivityResultLauncher<String>
    private lateinit var utilityImageXDCardLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_reactivate_account_documentation,
            container,
            false
        )
        // Initialize views
        initializeViews()

        // Initialize camera launchers by registering for activity result
        initializeCameraLaunchers()
        // Initialize gallery launchers by registering for activity result
        initializeGalleryLaunchers()

        return binding.root
    }

    private fun initializeCameraLaunchers() {
        profileImageCameraLauncher = registerActivityResultLauncher(profilePictureIV)
        signatureImageCameraLauncher = registerActivityResultLauncher(signatureIV)
        idCardImageCameraLauncher = registerActivityResultLauncher(idCardIv)
        utilityImageCameraLauncher = registerActivityResultLauncher(utilityIV)
    }

    private fun initializeGalleryLaunchers() {
        profileImageXDCardLauncher = registerGalleryLauncher(profilePictureIV)
        signatureImageXDCardLauncher = registerGalleryLauncher(signatureIV)
        idCardImageXDCardLauncher = registerGalleryLauncher(idCardIv)
        utilityImageXDCardLauncher = registerGalleryLauncher(utilityIV)
    }

    private fun registerActivityResultLauncher(imageView: ImageView): ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val bitmap = it.data?.extras?.get("data") as Bitmap
                currentUtilityImageActivityResultAsBitMap = bitmap
                AppConstants.getImageLoader(imageView, bitmap)
            }
        }

    private fun registerGalleryLauncher(imageView: ImageView): ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            val bitmap = MediaStore.Images.Media.getBitmap(
                activity?.contentResolver, it
            )
            currentProfileImageActivityResultAsBitMap = bitmap
            AppConstants.getImageLoader(imageView, bitmap)
        }

    override fun onResume() {
        super.onResume()
        (requireParentFragment() as ReactivateAccount).setSpecifications(
            0,
            resources.getString(R.string.uploadDocumentsScreen),
            0
        )

        binding.submitBtn.setOnClickListener {
            (requireParentFragment() as ReactivateAccount)
                .view?.findViewById<ViewPager2>(R.id.viewPager2)?.currentItem = 4
        }
    }

    override fun onPause() {
        super.onPause()
        (requireParentFragment() as ReactivateAccount).undoSpecifications()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profilePictureIV.handleClick(profileImageCameraLauncher, profileImageXDCardLauncher)
        signatureIV.handleClick(signatureImageCameraLauncher, signatureImageXDCardLauncher)
        idCardIv.handleClick(idCardImageCameraLauncher, idCardImageXDCardLauncher)
        utilityIV.handleClick(utilityImageCameraLauncher, utilityImageXDCardLauncher)
    }

    private fun ImageView.handleClick(
        cameraLauncher: ActivityResultLauncher<Intent>,
        galleryLauncher: ActivityResultLauncher<String>
    ) {
        this.setOnClickListener {
            AppConstants.getDialog(
                requireContext(),
                resources.getStringArray(R.array.cameraOrGalleryStringArray),
                {
                    AppConstants.cameraPermissionHandler(
                        this@ReactivateAccountDocumentation,
                        requireContext()
                    ) {
                        cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                    }
                },
                {
                    AppConstants.galleryPermissionHandler(
                        this@ReactivateAccountDocumentation,
                        requireContext()
                    ) {
                        galleryLauncher.launch("image/*")
                    }
                }
            ).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun initializeViews() {
        profilePictureIV = binding.profilePicture
        signatureIV = binding.signature
        idCardIv = binding.idCard
        utilityIV = binding.utility
    }
}