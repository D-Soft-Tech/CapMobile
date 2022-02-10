package com.example.capmobile.ui.view.customerEngagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.capmobile.R
import com.example.capmobile.databinding.NewCustomerEngagementBinding
import com.example.capmobile.ui.interfaces.DrawerLocker
import com.example.capmobile.ui.viewModel.MainViewModel
import com.example.capmobile.util.AppConstants.hideKeyboard
import com.example.capmobile.util.Status
import com.github.irshulx.Editor
import com.github.irshulx.models.EditorTextStyle
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CustomerEngagement : Fragment() {
    private lateinit var binding: NewCustomerEngagementBinding
    private lateinit var backArrowImageView: ImageView
    private lateinit var backTextView: TextView
    private lateinit var editor2: Editor
    private lateinit var selectLevel: AutoCompleteTextView
    private lateinit var selectTransactionStatus: AutoCompleteTextView
    private lateinit var levelsAdapter: ArrayAdapter<String>
    private lateinit var statusAdapter: ArrayAdapter<String>
    private lateinit var engageButton: Button
    private val viewModel: MainViewModel by viewModels()

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
        editor2 = binding.nestedEditor.editorView
        initializeViews()
        val transactionStatus = arrayOf("TRANSACTING", "NON-TRANSACTING", "ALL")

        statusAdapter = ArrayAdapter(requireContext(), R.layout.exposed_dropdown_menu_layout, transactionStatus)
        selectTransactionStatus.setAdapter(statusAdapter)

        initializeLevelAdapter()

        inflateEditorToolBar()
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
    }

    private fun initializeViews() {
        with(binding) {
            selectLevel = levelsDropDown
            selectTransactionStatus = statusDropDown
            backTextView = backText
            backArrowImageView = backArrow
            engageButton = engageBtn
        }
    }

    private fun initializeLevelAdapter() {
        viewModel.getLevelsAndDescription()

        viewModel.getLevelsAndDescription.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    // Set adapter to recyclerView
                    levelsAdapter = ArrayAdapter(requireContext(), R.layout.spinner_drop_down, it.data as ArrayList<String>)
                    selectLevel.setAdapter(levelsAdapter)
                }
                else -> {
                    Snackbar.make(
                        requireContext(),
                        binding.root,
                        getString(R.string.takeTooLong),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        })
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
}