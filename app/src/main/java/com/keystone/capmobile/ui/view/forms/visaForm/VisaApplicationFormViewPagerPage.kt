package com.keystone.capmobile.ui.view.forms.visaForm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keystone.capmobile.R

class VisaApplicationFormViewPagerPage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_visa_application_form_view_pager_page,
            container,
            false
        )
    }
}