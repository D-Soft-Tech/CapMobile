package com.keystone.capmobile.ui.view.customerConfirmation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.keystone.capmobile.R
import com.keystone.capmobile.data.model.DaoChannelObject
import com.keystone.capmobile.data.model.DataToPassToDaoEngageDialog
import com.keystone.capmobile.databinding.FragmentCustomerConfirmationBinding
import com.keystone.capmobile.ui.adapters.DaoAccountAdapters
import com.keystone.capmobile.ui.interfaces.DaoAccountAdapterFactory
import com.keystone.capmobile.ui.view.dialogFragments.DaoEngageIndividualDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class CustomerConfirmation @Inject constructor() : Fragment(), EasyPermissions.PermissionCallbacks {
    private lateinit var adapter: DaoAccountAdapters
    @Inject
    lateinit var engageDaoDialog: DaoEngageIndividualDialogFragment
    @Inject
    lateinit var daoAccountFactory: DaoAccountAdapterFactory
    private lateinit var accountDetailsSearchView: SearchView
    private lateinit var rv: RecyclerView
    private lateinit var binding: FragmentCustomerConfirmationBinding
    private lateinit var mainData: ArrayList<DaoChannelObject>
    private val dummyData = arrayListOf<DaoChannelObject>(
        DaoChannelObject("Emeka Paul", "08131655022", "emaka@gmail.com", mobile = true, ussd = true, iBank = false, cards = true, "ACTIVE"),
        DaoChannelObject("Kunle Felix", "08131655022", "kunlefelix@gmail.com", mobile =true, ussd = true, iBank =false, true, "DORMANT"),
        DaoChannelObject("Emeka Paul", "08131655022", "emekapaul@gmail.com", mobile =true, ussd = true, iBank =false, true, "ACTIVE"),
        DaoChannelObject("Olaosebikan Olakunle Oluwajoba", "08131655022", "oluwajoba@gmail.com", mobile =true, ussd = true, iBank =false, true, "INACTIVE"),
        DaoChannelObject("Tolani Ikechukwu", "08131655022", "tolani@gmail.com", mobile =true, ussd = true, iBank =false, true, "ACTIVE"),
        DaoChannelObject("Emeka Paul", "08131655022", "emekapaul@gmail.com", mobile =true, ussd = true, iBank =false, true, "DORMANT")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_customer_confirmation,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        mainData = dummyData // Assumes that dummyData was fetched from backend and it's used to initialize mainData
        adapter = daoAccountFactory.createDaoAccountAdapters { dataOfWhoToEngage ->
            handleCustomerEngagement(dataOfWhoToEngage as DataToPassToDaoEngageDialog)
        }
        adapter.setData(mainData)
    }

    override fun onResume() {
        super.onResume()
        accountDetailsSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val placeHolderDataForAdapter = mainData.filter {
                    it.name.lowercase()
                        .contains(newText.toString().lowercase()) || it.number.contains(
                        newText.toString()
                    )
                }
                adapter.setData(placeHolderDataForAdapter as ArrayList<DaoChannelObject>)
                return true
            }
        })
    }

    private fun initializeViews() {
        with(binding) {
            rv = searchResultRv
            accountDetailsSearchView = searchView
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
}