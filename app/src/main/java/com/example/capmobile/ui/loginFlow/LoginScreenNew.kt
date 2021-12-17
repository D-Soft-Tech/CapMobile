package com.example.capmobile.ui.loginFlow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.capmobile.R
import com.example.capmobile.databinding.LoginScreenNewBinding
import com.example.capmobile.ui.MainActivity

class LoginScreenNew : Fragment() {
    private lateinit var binding: LoginScreenNewBinding
    private lateinit var proceedBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.login_screen_new, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        proceedBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun initializeViews() {
        proceedBtn = binding.proceedBtn
    }
}