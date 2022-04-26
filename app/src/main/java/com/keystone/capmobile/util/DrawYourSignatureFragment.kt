package com.keystone.capmobile.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.keystone.capmobile.R
import com.keystone.capmobile.databinding.FragmentDrawSignatureBinding
import com.keystone.capmobile.ui.adapters.OpenAccountViewpagerAdapter
import com.keystone.capmobile.ui.interfaces.ViewPagerAdapterFactory
import com.keystone.capmobile.ui.signature.SignatureFragment
import com.keystone.capmobile.ui.viewModel.OpenAccountViewModel
import com.keystone.capmobile.util.AppConstants.customSnackBar
import com.keystone.capmobile.util.ConstantsOnly.SET_SIGNATURE_BUNDLE_KEY
import com.keystone.capmobile.util.ConstantsOnly.SET_SIGNATURE_REQUEST_KEY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DrawYourSignatureFragment @Inject constructor() : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var backArrowIV: ImageView
    private lateinit var backTextView: TextView
    private lateinit var eraserIV: ImageView
    private lateinit var uploadSignatureIV: ImageView
    private val viewModel: OpenAccountViewModel by activityViewModels()

    @Inject
    lateinit var signatureFragment: SignatureFragment
    lateinit var appViewPagerAdapter: OpenAccountViewpagerAdapter

    @Inject
    lateinit var viewPagerAdapterFactory: ViewPagerAdapterFactory
    private lateinit var binding: FragmentDrawSignatureBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_draw_signature, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appViewPagerAdapter = viewPagerAdapterFactory.createViewPagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle
        )
        appViewPagerAdapter.setScreens(arrayListOf(signatureFragment))
        // Initialize Views
        initViews()
        // set adapter to viewPager
        viewPager.apply {
            isUserInputEnabled = false
            adapter = appViewPagerAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        // Clear the signature to sign again
        eraserIV.setOnClickListener {
            signatureFragment.clear()
            customSnackBar(binding.root, getString(R.string.clearedSignAgain))
        }
        uploadSignatureIV.setOnClickListener {
            signatureFragment.getSignatureInBase64()?.let { base64SignatureString ->
                viewModel.setUserSignature(base64SignatureString, true)
                val signature = "uploaded"
                setFragmentResult(
                    SET_SIGNATURE_REQUEST_KEY,
                    bundleOf(SET_SIGNATURE_BUNDLE_KEY to signature)
                )
                findNavController().popBackStack()
            }
        }

        backTextView.setOnClickListener {
            findNavController().popBackStack()
        }

        backArrowIV.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initViews() {
        with(binding) {
            viewPager = viewPagerView
            backArrowIV = backArrow
            backTextView = backText
            eraserIV = eraser
            uploadSignatureIV = uploadSignature
        }
    }
}