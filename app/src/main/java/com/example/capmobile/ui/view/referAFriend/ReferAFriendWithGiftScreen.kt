package com.example.capmobile.ui.view.referAFriend

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.capmobile.R
import com.example.capmobile.data.model.PhoneContact
import com.example.capmobile.databinding.FragmentReferAFriendWithGiftScreenBinding
import com.example.capmobile.ui.interfaces.DrawerLocker
import com.example.capmobile.ui.view.dialogFragments.PhoneContactPicker
import com.example.capmobile.util.AppConstants.PHONE_CONTACT_PERM_REQUEST_CODE
import com.example.capmobile.util.AppConstants.closeKeyboardFromFragment
import com.example.capmobile.util.AppConstants.genericPermissionHandler
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import pub.devrel.easypermissions.EasyPermissions

class ReferAFriendWithGiftScreen : Fragment(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: FragmentReferAFriendWithGiftScreenBinding
    private lateinit var accountSelectionDropdown: AutoCompleteTextView
    private lateinit var beneficiaryPhoneNumber: TextInputEditText
    private lateinit var beneficiaryName: TextInputEditText
    private lateinit var amount: TextInputEditText
    private lateinit var optionalMessage: TextInputEditText
    private lateinit var sourceAccountAdapter: ArrayAdapter<String>
    private lateinit var shareButton: Button
    private lateinit var backArrow: ImageView
    private lateinit var backText: TextView
    private var selectedPhoneNumber: PhoneContact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("selectedPhone") { _, bundle ->
            selectedPhoneNumber = bundle.getParcelable("selectedBeneficiaryPhoneNumber")
            beneficiaryPhoneNumber.setText(selectedPhoneNumber?.phoneNumber)
            beneficiaryName.setText(selectedPhoneNumber?.name)
            closeKeyboardFromFragment(requireActivity(), beneficiaryPhoneNumber)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_refer_a_friend_with_gift_screen,
            container,
            false
        )

        sourceAccountAdapter = ArrayAdapter(
            requireContext(),
            R.layout.exposed_dropdown_menu_layout,
            resources.getStringArray(R.array.products)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()

        accountSelectionDropdown.setAdapter(sourceAccountAdapter)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()

        (activity as DrawerLocker).setDrawerEnabled(false)

        backText.setOnClickListener {
            upBackPressed()
        }

        backArrow.setOnClickListener {
            upBackPressed()
        }

        beneficiaryPhoneNumber.setOnTouchListener(View.OnTouchListener { _, event ->
            val drawableRight = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= beneficiaryPhoneNumber.right - beneficiaryPhoneNumber.compoundDrawables[drawableRight].bounds.width()) {
                    showPhoneContactAsBottomSheet()
                    return@OnTouchListener true
                }
            }
            false
        })

        shareButton.setOnClickListener {
            if (
                (accountSelectionDropdown.isSelected) ||
                beneficiaryPhoneNumber.text.isNullOrEmpty() ||
                amount.text.isNullOrEmpty() ||
                beneficiaryName.text.isNullOrEmpty()
            ) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.allFieldsAreRequired),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        getString(
                            R.string.refer_a_friend_with_gift_msg,
                            selectedPhoneNumber?.name,
                            amount.text.toString()
                        )
                    )
                }
                startActivity(intent)
            }
        }
    }

    private fun showPhoneContactAsBottomSheet() {
        genericPermissionHandler(
            this@ReferAFriendWithGiftScreen,
            requireContext(),
            Manifest.permission.READ_CONTACTS,
            PHONE_CONTACT_PERM_REQUEST_CODE,
            getString(R.string.phone_contact_perm_rationale)
        ) {
            PhoneContactPicker().show(parentFragmentManager, null)
        }
    }

    private fun initializeViews() {
        accountSelectionDropdown = binding.accountTypeTIET
        beneficiaryPhoneNumber = binding.beneficiaryNumber
        beneficiaryName = binding.beneficiaryName
        amount = binding.amount
        optionalMessage = binding.optionalMessage
        shareButton = binding.shareButton
        backArrow = binding.backArrow
        backText = binding.backText
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(
            requireContext(),
            getString(R.string.phone_contact_perm_granted),
            Toast.LENGTH_SHORT
        ).show()
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

    private fun upBackPressed() {
        findNavController().popBackStack()
    }
}