package com.keystone.capmobile.ui.view.dialogFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.keystone.capmobile.R
import com.keystone.capmobile.databinding.FragmentBottomSheetCreateMessageDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject

class BottomSheetCreateMessageDialog @Inject constructor() : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetCreateMessageDialogBinding
    private lateinit var cancelButton: ImageView
    private lateinit var subject: TextInputEditText
    private lateinit var submitBtn: Button

    // This bottomSheetFragment is used enable user to be able to create
    // a message for the levels that doesn't have a message from the backend,
    // however i was asked not to implement this, that all messages should be vetted and approved from backend,
    // hence, i didn't implement but leave it should this be needed in future,
    // this bottomSheet can be opened by the pencil (edit) in the customer engagement screen,
    // this pencil's visibility too has been set to invisible because i have been told not to go ahead with it again

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bottom_sheet_create_message_dialog,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun initViews() {
        with(binding) {
            cancelButton = dismissImageview
            subject = messageSubject
            submitBtn = engageBtn
        }
    }

}
