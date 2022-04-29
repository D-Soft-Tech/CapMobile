package com.keystone.capmobile.ui.view.dialogFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.keystone.capmobile.R
import com.keystone.capmobile.databinding.LoadingDialogBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadingDialog @Inject constructor() : DialogFragment() {
    private lateinit var binding: LoadingDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.loading_dialog, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.apply {
            setBackgroundDrawableResource(R.drawable.curve_bg)
            isCancelable = false
        }
    }
}