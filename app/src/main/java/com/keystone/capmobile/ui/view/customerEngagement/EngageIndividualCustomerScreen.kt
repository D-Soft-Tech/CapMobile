package com.keystone.capmobile.ui.view.customerEngagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.keystone.capmobile.R
import com.keystone.capmobile.databinding.FragmentEngageIndividualCustomerScreenBinding
import com.keystone.capmobile.util.AppConstants.openWhatsApp
import com.keystone.capmobile.util.AppConstants.sendMessageViaEmail
import com.keystone.capmobile.util.AppConstants.showAlerter
import com.keystone.capmobile.util.AppConstants.validateInput

class EngageIndividualCustomerScreen : Fragment() {
    private val arg by navArgs<EngageIndividualCustomerScreenArgs>()
    private lateinit var binding: FragmentEngageIndividualCustomerScreenBinding
    private lateinit var msgSubject: TextInputEditText
    private lateinit var msgBody: EditText
    private lateinit var emailBtn: Button
    private lateinit var whatsAppBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_engage_individual_customer_screen,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        emailBtn.setOnClickListener {
            if (msgSubject.validateInput() && msgBody.validateInput()) {
                if (arg.userToEngage.userEmailAddress.isNotEmpty()) {
                    sendMessageViaEmail(
                        it,
                        arrayOf(arg.userToEngage.userEmailAddress),
                        msgSubject.text.toString().trim(),
                        msgBody.text.toString().trim(),
                        requireActivity()
                    )
                } else {
                    showAlerter(
                        it,
                        requireActivity(),
                        getString(R.string.userEmailIsInvalid)
                    ) {}
                }
            }
        }

        whatsAppBtn.setOnClickListener {
            // This assumes that the user's phone number always starts with '0' and that it is a nigeria phone number
            // This is because the phone number is from the backend like that eg in the form '08012345678'
            val phoneNumber =
                "234${arg.userToEngage.number.replace("\\s".toRegex(), "").substring(1)}"
            if (msgSubject.validateInput() && msgBody.validateInput()) {
                openWhatsApp(
                    binding.root,
                    msgSubject.text.toString(),
                    msgBody.text.toString(),
                    phoneNumber,
                    requireActivity()
                )
            }
        }
    }

    private fun initializeViews() {
        with(binding) {
            msgSubject = messageSubject
            msgBody = messageDetails
            emailBtn = engageViaEmailBtn
            whatsAppBtn = engageViaWhatsAppBtn
        }
    }
}