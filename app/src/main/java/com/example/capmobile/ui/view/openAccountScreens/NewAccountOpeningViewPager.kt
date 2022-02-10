package com.example.capmobile.ui.view.openAccountScreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.aceinteract.android.stepper.StepperNavigationView
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentNewAccountOpeningViewPagerBinding
import com.example.capmobile.ui.view.MainActivity
import com.example.capmobile.ui.adapters.OpenAccountViewpagerAdapter
import com.example.capmobile.ui.interfaces.DrawerLocker
import com.google.android.material.snackbar.Snackbar

class NewAccountOpeningViewPager : Fragment() {
    private val myAdapter by lazy { OpenAccountViewpagerAdapter(childFragmentManager, lifecycle) }
    private lateinit var binding: FragmentNewAccountOpeningViewPagerBinding
    private lateinit var backArrow: ImageView
    private lateinit var backText: TextView
    private lateinit var stepperView: StepperNavigationView
    private lateinit var viewPager: ViewPager2
    private lateinit var flowDestinations: ArrayList<Fragment>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_account_opening_view_pager,
            container,
            false
        )

        flowDestinations = arrayListOf(
            OpenAccountBvnScreen(),
            OpenAccountBioDataScreen(),
            OpenAccountAddressScreen(),
            OpenAccountDocumentationScreen()
        )

        myAdapter.setScreens(flowDestinations)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked

                if (viewPager.currentItem > 0) {
                    gotoPreviousStep()
                } else {
//                    isEnabled = false
//                    activity?.onBackPressed()
                    Snackbar.make(requireContext(), binding.root,getString(R.string.firstPage) , Snackbar.LENGTH_SHORT).show()
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        initializeViewPager()
        stepperView.initializeStepper()
    }

    override fun onResume() {
        super.onResume()

        (activity as DrawerLocker).setDrawerEnabled(false)

        backText.setOnClickListener {
            upBackPressed()
        }

        backArrow.setOnClickListener {
            upBackPressed()
        }
    }

    private fun initializeViews() {
        backArrow = binding.backArrow
        backText = binding.backText
        stepperView = binding.stepperView
        viewPager = binding.viewPager
    }

    fun gotoNextStep() {
        stepperView.goToNextStep()
        viewPager.currentItem += 1
    }

    fun gotoPreviousStep() {
        stepperView.goToPreviousStep()
        viewPager.currentItem -= 1
    }

    private fun StepperNavigationView.initializeStepper() {
        apply {
            stepperNavListener = requireActivity() as MainActivity
            widgetColor = resources.getColor(R.color.lekanColor)
            textColor = resources.getColor(R.color.lekanColor)
        }
    }

    private fun initializeViewPager() {
        viewPager.apply {
            isUserInputEnabled = false
            adapter = myAdapter
        }
    }

    private fun upBackPressed() {
        if (viewPager.currentItem > 0) {
            gotoPreviousStep()
        } else {
            findNavController().popBackStack()
        }
    }
}