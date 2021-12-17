package com.example.capmobile.ui.reactivateAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentReactivateAccountBinding
import com.example.capmobile.ui.adapters.OpenAccountViewpagerAdapter
import com.example.capmobile.ui.interfaces.DrawerLocker
import com.example.capmobile.ui.viewmodels.UploadViewModel

class ReactivateAccount : Fragment() {

    private val myAdapter by lazy { OpenAccountViewpagerAdapter(childFragmentManager, lifecycle) }
    private lateinit var binding: FragmentReactivateAccountBinding
    private lateinit var previousPageBtn: Button
    private lateinit var nextPageBtn: Button
    private lateinit var viewPager: ViewPager2
    private lateinit var flowPositionTv: TextView
    private lateinit var flowDestinations: ArrayList<Fragment>
    private lateinit var viewModel: UploadViewModel
    private lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(UploadViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reactivate_account, container, false)

        flowDestinations = arrayListOf(
            AccountReactivationFirstPage(),
            AccountReactivationBioData(),
            ReactivateAccountUserAddress(),
            ReactivateAccountDocumentation(),
            ReactivateAccountSuccessPage()
        )

        // Set Data to adapter
        myAdapter.setScreens(flowDestinations)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()

        initializeViewPager()
    }

    private fun initializeViewPager() {
        viewPager.apply {
            adapter = myAdapter
            isUserInputEnabled = false
        }

        nextPageBtn.setOnClickListener {
            viewPager.currentItem += 1
            viewModel.viewPagerPosition = viewPager.currentItem
        }

        previousPageBtn.setOnClickListener {
            viewPager.currentItem -= 1
            viewModel.viewPagerPosition = viewPager.currentItem
        }
    }

    override fun onResume() {
        super.onResume()

//        viewPager.currentItem = viewModel.viewPagerPosition
        (activity as DrawerLocker).setDrawerEnabled(false)
        backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun setSpecifications(toShow: Int, title: String, buttonToEnableAtOnResume: Int = 0) {
        // Set title of current progress
        flowPositionTv.text = title
        when (toShow) {
            0 -> {
                nextPageBtn.visibility = View.INVISIBLE
            }
            1 -> {
                previousPageBtn.visibility = View.INVISIBLE
            }
            2 -> {
                previousPageBtn.visibility = View.VISIBLE
                nextPageBtn.visibility = View.VISIBLE
            }
        }

        when (buttonToEnableAtOnResume) {
            0 -> {
                previousPageBtn.isEnabled = true
            }
            1 -> {
                nextPageBtn.isEnabled = true
            }
        }
    }

    fun undoSpecifications() {
        nextPageBtn.apply {
            visibility = View.VISIBLE
            isEnabled = false
        }
        previousPageBtn.apply {
            visibility = View.VISIBLE
            isEnabled = false
        }
    }

    fun enableAButton(buttonToEnableAfterConditionMet: Int, progressTracker: Int) {
        when (buttonToEnableAfterConditionMet) {
            0 -> {
                previousPageBtn.isEnabled = true
            }
            1 -> {
                nextPageBtn.isEnabled = true
            }
        }
        incrementProgressBar(progressTracker)
    }

    private fun incrementProgressBar(progressTracker: Int) {
        if (progressTracker == 0 && progressTracker < 1) {
            // Update progressbar
            binding.progressBar.incrementProgressBy(25)
        }
    }

    fun downLoadAccountInfo() {
        nextPageBtn.visibility = View.GONE
        previousPageBtn.visibility = View.GONE
        flowPositionTv.visibility = View.GONE
    }

    private fun initializeView() {
        previousPageBtn = binding.previousPage
        nextPageBtn = binding.nextPage
        viewPager = binding.viewPager2
        flowPositionTv = binding.flowPosition
        backArrow = binding.imageButton
    }
}