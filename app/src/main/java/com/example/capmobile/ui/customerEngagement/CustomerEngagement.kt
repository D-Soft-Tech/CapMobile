package com.example.capmobile.ui.customerEngagement

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.capmobile.R
import com.example.capmobile.databinding.CustomerEngagementBinding
import com.example.capmobile.ui.interfaces.BottomDialogSheetCommunicationInterface
import com.example.capmobile.ui.interfaces.DrawerLocker
import com.example.capmobile.util.AppConstants.hideKeyboard
import com.github.irshulx.Editor
import com.github.irshulx.models.EditorTextStyle
import com.google.android.material.switchmaterial.SwitchMaterial


class CustomerEngagement : Fragment() {
    private lateinit var backArrow: ImageView
    private lateinit var binding: CustomerEngagementBinding
    private lateinit var switch: SwitchMaterial
    private lateinit var editor2: Editor
    private lateinit var selectCategory: AutoCompleteTextView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var whatsAppIV: ImageView
    private lateinit var emailIV: ImageView
    private lateinit var pushNotIV: ImageView
    private lateinit var editIcon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.customer_engagement,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        inflateEditorToolBar()
        initializeArrayAdapter()

        // Set by levels into the dropdown by default
        arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.exposed_dropdown_menu_layout,
            resources.getStringArray(R.array.categoryByLevel)
        )
        selectCategory.setAdapter(arrayAdapter)

        editIcon.setOnClickListener {
            if (binding.textInputLayoutOptionalMessage.visibility == View.GONE) {
                binding.textInputLayoutOptionalMessage.visibility = View.VISIBLE
                binding.nestedEditor.editorView.visibility = View.VISIBLE
            } else {
                binding.textInputLayoutOptionalMessage.visibility = View.GONE
                binding.nestedEditor.editorView.visibility = View.INVISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Disable drawer
        (activity as DrawerLocker).setDrawerEnabled(false)

        backArrow.setOnClickListener {
            hideKeyboard()
            findNavController().popBackStack()
        }

//        whatsAppIV.sendDataToParent("WhatsApp")
//        emailIV.sendDataToParent("Email")
//        pushNotIV.sendDataToParent("PushNotification")

        // Hide the keyboard by default
        hideKeyboard()
    }

    private fun initializeViews() {
        editor2 = binding.nestedEditor.editorView
        switch = binding.switch1
        selectCategory = binding.autoCompleteTextView
        editIcon = binding.imageView4
        whatsAppIV = binding.imageView6
        emailIV = binding.imageView5
        pushNotIV = binding.imageView7
        backArrow = binding.imageButton
    }

    private fun initializeArrayAdapter() {

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                arrayAdapter = ArrayAdapter(
                    requireContext(),
                    R.layout.exposed_dropdown_menu_layout,
                    resources.getStringArray(R.array.categoryByTransaction)
                )

                // Set Adapter to autoComplete dropdown
                selectCategory.setAdapter(arrayAdapter)
            } else {
                arrayAdapter = ArrayAdapter(
                    requireContext(),
                    R.layout.exposed_dropdown_menu_layout,
                    resources.getStringArray(R.array.categoryByLevel)
                )
                selectCategory.setAdapter(arrayAdapter)
            }
        }
    }

    private fun inflateEditorToolBar() {

        val customInflater = binding.nestedEditor.toolBarScrollView

        customInflater.findViewById<Button>(R.id.action_h1)
            .setOnClickListener {
                editor2.updateTextStyle(
                    EditorTextStyle.H1
                )
            }

        customInflater.findViewById<Button>(R.id.action_h2)
            .setOnClickListener {
                editor2.updateTextStyle(
                    EditorTextStyle.H2
                )
            }

        customInflater.findViewById<Button>(R.id.action_h3)
            .setOnClickListener {
                editor2.updateTextStyle(
                    EditorTextStyle.H3
                )
            }

        customInflater.findViewById<ImageButton>(R.id.action_bold)
            .setOnClickListener {
                editor2.updateTextStyle(
                    EditorTextStyle.BOLD
                )
            }

        customInflater.findViewById<ImageButton>(R.id.action_Italic)
            .setOnClickListener {
                editor2.updateTextStyle(
                    EditorTextStyle.ITALIC
                )
            }

        customInflater.findViewById<ImageButton>(R.id.action_indent)
            .setOnClickListener {
                editor2.updateTextStyle(
                    EditorTextStyle.INDENT
                )
            }

        customInflater.findViewById<ImageButton>(R.id.action_outdent)
            .setOnClickListener {
                editor2.updateTextStyle(
                    EditorTextStyle.OUTDENT
                )
            }

        customInflater.findViewById<ImageButton>(R.id.action_bulleted)
            .setOnClickListener {
                editor2.insertList(
                    false
                )
            }

        customInflater.findViewById<ImageButton>(R.id.action_color)
            .setOnClickListener {
                editor2.updateTextColor(
                    "#FF3333"
                )
            }

        customInflater.findViewById<ImageButton>(R.id.action_unordered_numbered)
            .setOnClickListener {
                editor2.insertList(
                    true
                )
            }

        customInflater.findViewById<ImageButton>(R.id.action_hr)
            .setOnClickListener { editor2.insertDivider() }

        customInflater.findViewById<ImageButton>(R.id.action_insert_image)
            .setOnClickListener { editor2.openImagePicker() }

        customInflater.findViewById<ImageButton>(R.id.action_insert_link)
            .setOnClickListener { editor2.insertLink() }


        customInflater.findViewById<ImageButton>(R.id.action_erase)
            .setOnClickListener { editor2.clearAllContents() }

        customInflater.findViewById<ImageButton>(R.id.action_blockquote)
            .setOnClickListener {
                editor2.updateTextStyle(
                    EditorTextStyle.BLOCKQUOTE
                )
            }

        editor2.render()
    }

    private fun ImageView.sendDataToParent(typeOfButton: String) {
        this.setOnClickListener {
            val editorContent = binding.nestedEditor.editorView.content.toString()
            if (editorContent.isNotEmpty() && editorContent.length > 4) {
//                Toast.makeText(requireContext(), editorContent, Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(
//                    requireContext(),
//                    "Pre-generated message was sent",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
    }
}