package com.example.capmobile.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentLoginBinding

class LoginPage : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            owner = this@LoginPage
        }
        return binding.root
    }

    fun navigateToWelcomeScreen() {
        findNavController().navigate(R.id.action_loginPage_to_welcomeScreen)
    }
}
