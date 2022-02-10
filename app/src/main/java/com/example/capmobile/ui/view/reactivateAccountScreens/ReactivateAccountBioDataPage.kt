package com.example.capmobile.ui.view.reactivateAccountScreens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentReactivateAccountBioDataPageBinding
import com.example.capmobile.ui.view.dialogFragments.DatePicker
import com.google.android.material.textfield.TextInputEditText

class ReactivateAccountBioDataPage : Fragment() {
    private lateinit var binding: FragmentReactivateAccountBioDataPageBinding
    private lateinit var previousBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var dateOfBirth: TextInputEditText
    private lateinit var myAdapter: ArrayAdapter<String>
    private lateinit var gender: AutoCompleteTextView
    private var selectedDate: String = ""
    private var userDateOfBirthFromBvn: String = "19-12-1990"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("selectedDate") { key, bundle ->
            selectedDate = bundle.getString("dateOfBirth", userDateOfBirthFromBvn)
            dateOfBirth.setText(selectedDate)
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.success),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_reactivate_account_bio_data_page,
            container,
            false
        )

        myAdapter = ArrayAdapter(
            requireContext(),
            R.layout.exposed_dropdown_menu_layout,
            resources.getStringArray(R.array.gender)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        gender.setAdapter(myAdapter)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()

        dateOfBirth.setOnTouchListener(OnTouchListener { _, event ->
            val drawableRight = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= dateOfBirth.right - dateOfBirth.compoundDrawables[drawableRight].bounds.width()
                ) {
                    // show Bottom Sheet
                    showBottomSheet()
                    return@OnTouchListener true
                }
            }
            false
        })

        dateOfBirth.setText(selectedDate)

        previousBtn.setOnClickListener {
            (requireParentFragment() as ReactivateAccountViewPager).gotoPreviousStep()
        }

        nextBtn.setOnClickListener {
            (requireParentFragment() as ReactivateAccountViewPager).gotoNextStep()
        }
    }

    private fun showBottomSheet() {
        DatePicker().show(parentFragmentManager, null)
    }

    private fun initializeViews() {
        dateOfBirth = binding.dateOfBirth
        gender = binding.accountTypeTIET
        previousBtn = binding.previousPage
        nextBtn = binding.nextBtn
    }
}