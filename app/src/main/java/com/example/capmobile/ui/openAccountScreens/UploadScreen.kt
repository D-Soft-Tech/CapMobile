package com.example.capmobile.ui.openAccountScreens

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.capmobile.R
import com.example.capmobile.databinding.UploadDocumentsBinding
import com.example.capmobile.util.AppConstants.CAMERA_PERM_REQUEST_CODE
import com.example.capmobile.util.AppConstants.STORAGE_PERM_REQUEST_CODE
import com.example.capmobile.util.AppConstants.cameraPermissionHandler
import com.example.capmobile.util.AppConstants.galleryPermissionHandler
import com.example.capmobile.util.AppConstants.getDialog
import com.example.capmobile.util.AppConstants.getImageLoader
import pub.devrel.easypermissions.EasyPermissions

class UploadScreen : Fragment(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: UploadDocumentsBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.upload_documents, container, false)
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
                getImageLoader(imageView, bitmap)
            }
        }


    private fun registerGalleryLauncher(imageView: ImageView): ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            val bitmap = MediaStore.Images.Media.getBitmap(
                activity?.contentResolver, it
            )
            currentProfileImageActivityResultAsBitMap = bitmap
            getImageLoader(imageView, bitmap)
        }


    override fun onResume() {
        super.onResume()
        (requireParentFragment() as FragmentOpenAccount).setSpecifications(
            0,
            resources.getString(R.string.uploadDocumentsScreen),
            0
        )

        binding.submitBtn.setOnClickListener {
            (requireParentFragment() as FragmentOpenAccount)
                .view?.findViewById<ViewPager2>(R.id.viewPager2)?.currentItem = 4
        }
    }

    override fun onPause() {
        super.onPause()
        (requireParentFragment() as FragmentOpenAccount).undoSpecifications()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when {
            requestCode == STORAGE_PERM_REQUEST_CODE -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.storage_permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
            requestCode == CAMERA_PERM_REQUEST_CODE -> {
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
            getDialog(
                requireContext(),
                resources.getStringArray(R.array.cameraOrGalleryStringArray),
                {
                    cameraPermissionHandler(this@UploadScreen, requireContext()) {
                        cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                    }
                },
                {
                    galleryPermissionHandler(this@UploadScreen, requireContext()) {
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
