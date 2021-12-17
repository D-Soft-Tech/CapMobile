package com.example.capmobile.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentWelcomeScreenBinding

class WelcomeScreen : Fragment() {
    private lateinit var binding: FragmentWelcomeScreenBinding
    private lateinit var next: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                if(shouldInterceptBackPress()){
//                    // in here you can do logic when backPress is clicked
//                }else{
//                    isEnabled = false
//                    activity?.onBackPressed()
//                }
                activity?.finish()
            }
        })
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_welcome_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        next.setOnClickListener {
            // Navigate to welcome Screen
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun initializeViews() {
        next = binding.next
    }
}