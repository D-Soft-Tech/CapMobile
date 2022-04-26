package com.keystone.capmobile.ui.view.dialogFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.keystone.capmobile.R
import com.keystone.capmobile.data.model.GetBankBranchResponse
import com.keystone.capmobile.databinding.FragmentSelectBranchBottomSheetDialogBinding
import com.keystone.capmobile.ui.adapters.GenericRecyclerViewAdapter
import com.keystone.capmobile.ui.adapters.GenericSimpleRecyclerBindingInterface
import com.keystone.capmobile.ui.viewModel.OpenAccountViewModel
import com.keystone.capmobile.util.AppConstants.customSnackBar
import com.keystone.capmobile.util.ConstantsOnly.SELECT_BRANCH_BUNDLE_KEY
import com.keystone.capmobile.util.ConstantsOnly.SELECT_BRANCH_REQUEST_KEY
import com.keystone.capmobile.util.Resource
import com.keystone.capmobile.util.Status
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectBranchBottomSheetDialogFragment @Inject constructor() : BottomSheetDialogFragment() {
    private val viewModel: OpenAccountViewModel by activityViewModels()
    private lateinit var binding: FragmentSelectBranchBottomSheetDialogBinding
    private var mainDataHolder: ArrayList<GetBankBranchResponse> = arrayListOf()
    private var tempDataHolder: ArrayList<GetBankBranchResponse> =
        arrayListOf()// For the purpose of searching via the search view
    private lateinit var branchRecyclerView: RecyclerView
    private lateinit var prgBar: ProgressBar
    private lateinit var searchRv: SearchView
    private lateinit var cancelBtn: ImageView
    private lateinit var rvAdapter: GenericRecyclerViewAdapter<GetBankBranchResponse>
    private val genericBindingInterface: GenericSimpleRecyclerBindingInterface<GetBankBranchResponse> =
        object : GenericSimpleRecyclerBindingInterface<GetBankBranchResponse> {
            override fun bindData(item: GetBankBranchResponse, view: View) {
                val nameOfBranch = view.findViewById<TextView>(R.id.branchName)
                val addressOfBranch = view.findViewById<TextView>(R.id.branchAddress)
                val bankCodeOfBranch = view.findViewById<TextView>(R.id.branchCode)

                with(item) {
                    nameOfBranch.text = BranchName
                    addressOfBranch.text = BranchAddress
                    bankCodeOfBranch.text = BranchCode
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_select_branch_bottom_sheet_dialog,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize RecyclerViewAdapter
        rvAdapter = GenericRecyclerViewAdapter(
            R.layout.select_branch_item_layout,
            genericBindingInterface
        ) {
            handleRecyclerViewCallBack(it)
        }
        // Set data to the recyclerview adapter
        viewModel.getListOfBankBranch()
        viewModel.bankBranchResponse.observe(viewLifecycleOwner, {
            handleResponseFromBackEnd(it)
        })
        // Initialize Views
        initViews()
        // Set adapter on recyclerView
        branchRecyclerView.adapter = rvAdapter
    }

    private fun handleRecyclerViewCallBack(selectedBranch: GetBankBranchResponse) {
        setFragmentResult(
            SELECT_BRANCH_REQUEST_KEY,
            bundleOf(SELECT_BRANCH_BUNDLE_KEY to selectedBranch)
        )
        dismiss()
    }

    private fun initViews() {
        with(binding) {
            branchRecyclerView = recyclerView
            searchRv = searchView
            cancelBtn = close
            prgBar = progressBar
        }
    }

    override fun onResume() {
        super.onResume()

        searchRv.handleSearchingViaSearchView()

        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun handleResponseFromBackEnd(response: Resource<List<GetBankBranchResponse>>) {
        when (response.status) {
            Status.LOADING -> {
                branchRecyclerView.visibility = View.INVISIBLE
                prgBar.visibility = View.VISIBLE
            }

            Status.ERROR -> {
                hidePrgBarShowRv()
                customSnackBar(binding.root, getString(R.string.errorGettingBranchLists))
            }

            Status.TIMEOUT -> {
                hidePrgBarShowRv()
                customSnackBar(binding.root, getString(R.string.timeOut))
            }

            Status.SUCCESS -> {
                hidePrgBarShowRv()
                mainDataHolder = response.data as ArrayList<GetBankBranchResponse>
                tempDataHolder = mainDataHolder
                rvAdapter.updateData(mainDataHolder)
            }
        }
    }

    private fun SearchView.handleSearchingViaSearchView() {
        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val newTempData = tempDataHolder.filter {
                        with(it) {
                            BranchName.lowercase().contains(newText.lowercase()) ||
                                    BranchAddress.lowercase().contains(newText.lowercase()) ||
                                    BranchCode.lowercase().contains(newText.lowercase()) ||
                                    id == newText.toIntOrNull()
                        }
                    }
                    rvAdapter.updateData(newTempData as ArrayList<GetBankBranchResponse>)
                }
                return true
            }

        })
    }

    private fun hidePrgBarShowRv() {
        branchRecyclerView.visibility = View.VISIBLE
        prgBar.visibility = View.INVISIBLE
    }
}