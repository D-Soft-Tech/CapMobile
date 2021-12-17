package com.example.capmobile.ui.openAccountScreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.capmobile.R
import com.example.capmobile.databinding.BioDataPageBinding

class BioDataScreen : Fragment() {
    private lateinit var binding: BioDataPageBinding
    private lateinit var myAdapter: ArrayAdapter<String>
    private var currentItemPositionInGenderTV: Int = 0
    private var progressTrackKeeper: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.bio_data_page, container, false)
        myAdapter = ArrayAdapter(
            requireContext(),
            R.layout.exposed_dropdown_menu_layout,
            resources.getStringArray(R.array.gender)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.auto.setAdapter(myAdapter)
    }

    override fun onResume() {
        super.onResume()
        (requireParentFragment() as FragmentOpenAccount).setSpecifications(
            2,
            resources.getString(R.string.bioDataSpecification),
            0
        )

        binding.auto.setOnItemClickListener { _, _, position: Int, _ ->
            currentItemPositionInGenderTV = position
            if (position > 0) {
                (requireParentFragment() as FragmentOpenAccount).enableAButton(
                    1,
                    progressTrackKeeper
                )
            }
        }

        if (currentItemPositionInGenderTV > 0) {
            (requireParentFragment() as FragmentOpenAccount).enableAButton(1, progressTrackKeeper)
        }
    }

    override fun onPause() {
        super.onPause()
        (requireParentFragment() as FragmentOpenAccount).undoSpecifications()
    }
}