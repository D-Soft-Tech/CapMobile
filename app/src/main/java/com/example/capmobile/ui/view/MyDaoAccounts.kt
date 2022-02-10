package com.example.capmobile.ui.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.capmobile.R
import com.example.capmobile.data.model.DaoChannelObject
import com.example.capmobile.data.model.DataToPassToDaoEngageDialog
import com.example.capmobile.databinding.FragmentMyDaoAccountsBinding
import com.example.capmobile.ui.adapters.DaoAccountAdapters
import com.example.capmobile.ui.interfaces.DrawerLocker
import com.example.capmobile.ui.view.dialogFragments.DaoEngageIndividualDialogFragment
import com.example.capmobile.ui.viewModel.MainViewModel
import com.example.capmobile.util.Resource
import com.example.capmobile.util.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions

@RequiresApi(Build.VERSION_CODES.R)
@AndroidEntryPoint
class MyDaoAccounts : Fragment(), EasyPermissions.PermissionCallbacks {

    private val adapter: DaoAccountAdapters by lazy {
        DaoAccountAdapters { whoToEngage ->
            handleCustomerEngagement(whoToEngage)
        }
    }

    private var engageDaoDialog: DaoEngageIndividualDialogFragment =
        DaoEngageIndividualDialogFragment()
    private lateinit var binding: FragmentMyDaoAccountsBinding
    private lateinit var spinner: Spinner
    private var mainData: ArrayList<DaoChannelObject> = arrayListOf()
    private lateinit var srchView: SearchView
    private val viewModel: MainViewModel by viewModels()
    private lateinit var rcyclrView: RecyclerView
    private lateinit var bckArrow: ImageView
    private lateinit var bckText: TextView
    private lateinit var progBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_dao_accounts, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        // Make call to backend
        viewModel.getAccounts("ALL")
        // OBSERVE THE RESPONSE
        viewModel.getAccountResponse.observe(viewLifecycleOwner, {
            observeViewModel(it)
        })

        rcyclrView.adapter = adapter
        // go back at the onclick of the back arrow
        bckArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        bckText.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        // Disable drawer from showing on this page
        (activity as DrawerLocker).setDrawerEnabled(false)

        srchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val placeHolderDataForAdapter = mainData.filter {
                    it.name.lowercase()
                        .contains(newText.toString().lowercase()) || it.phoneNumber.contains(
                        newText.toString()
                    )
                }
                adapter.setData(placeHolderDataForAdapter as ArrayList<DaoChannelObject>)
                return true
            }
        })

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.getAccounts(parent?.getItemAtPosition(position).toString())

                viewModel.getAccountResponse.observe(viewLifecycleOwner, {
                    observeViewModel(it)
                })

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun initializeViews() {

        with(binding) {
            bckArrow = backArrow
            bckText = backText
            progBar = progressBar
            rcyclrView = recyclerView
            srchView = searchView
            spinner = spinnerView
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(requireContext(), getString(R.string.permission_granted), Toast.LENGTH_SHORT)
            .show()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(requireContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT)
            .show()
    }

    private fun handleCustomerEngagement(whoToDaoEngageDialog: DataToPassToDaoEngageDialog) {
        setFragmentResult(
            "DaoToEngage",
            bundleOf("phoneNumberOfTheIndividual" to whoToDaoEngageDialog)
        )
        engageDaoDialog.show(parentFragmentManager, "EngageDaoDialog")
    }

    private fun observeViewModel(viewModelResponse: Resource<List<DaoChannelObject>>) {
        when (viewModelResponse.status) {
            Status.LOADING -> {
                rcyclrView.visibility = View.INVISIBLE
                progBar.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                rcyclrView.visibility = View.VISIBLE
                progBar.visibility = View.GONE
            }
            Status.SUCCESS -> {
                progBar.visibility = View.GONE
                rcyclrView.visibility = View.VISIBLE
                mainData = (viewModelResponse.data as ArrayList<DaoChannelObject>?)!!
                // Set adapter to recyclerView
                adapter.setData(mainData)
            }
            else -> {
                progBar.visibility = View.GONE
                Snackbar.make(
                    requireContext(),
                    binding.root,
                    getString(R.string.takeTooLong),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}