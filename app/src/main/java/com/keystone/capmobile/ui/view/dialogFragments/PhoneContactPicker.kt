package com.keystone.capmobile.ui.view.dialogFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
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
import com.keystone.capmobile.databinding.FragmentPhoneContactPickerBinding
import com.keystone.capmobile.data.model.PhoneContact
import com.keystone.capmobile.ui.adapters.PhoneContactRecyclerViewAdapter
import com.keystone.capmobile.util.AppConstants.closeKeyboardFromFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PhoneContactPicker : BottomSheetDialogFragment(),
    PhoneContactRecyclerViewAdapter.PhoneContactRecyclerViewClickListener {
    private lateinit var binding: FragmentPhoneContactPickerBinding
    private lateinit var rv: RecyclerView
    private val rvAdapter: PhoneContactRecyclerViewAdapter by lazy {
        PhoneContactRecyclerViewAdapter(
            this
        )
    }
    private lateinit var searchView: SearchView
    private lateinit var projection: Array<String>
    private lateinit var contactsFromPhone: ArrayList<PhoneContact>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_phone_contact_picker,
            container,
            false
        )

        projection = listOf(
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        ).toTypedArray()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        readPhoneContact()
        rv.apply {
            adapter = rvAdapter.apply { setData(contactsFromPhone) }
        }
    }

    override fun onResume() {
        super.onResume()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                filterAdapter(query)
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                filterAdapter(newText)
                return true
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filterAdapter(newText: String?) {
        val matcher = contactsFromPhone.filter {
            it.name.lowercase().contains(newText?.lowercase() as CharSequence) || it.phoneNumber.contains(newText)
        }
        rvAdapter.apply {
            setData(matcher as ArrayList<PhoneContact>)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("Recycle")
    private fun readPhoneContact() {
        val phoneContacts: MutableList<PhoneContact> = mutableListOf()
        val cr = requireContext().contentResolver
        val contacts =
            cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )

        if (contacts?.moveToFirst() == true) {

            phoneContacts.add(
                PhoneContact(
                    contacts.getString(2).toString(),
                    contacts.getString(3),
                    contacts.getString(1)
                )
            )

            while (contacts.moveToNext()) {
                phoneContacts.add(
                    PhoneContact(
                        contacts.getString(2).toString(),
                        contacts.getString(3),
                        contacts.getString(1)
                    )
                )
            }

            contactsFromPhone = phoneContacts as ArrayList<PhoneContact>
        } else {
            Toast.makeText(requireContext(), getString(R.string.you_have_no_contact), Toast.LENGTH_SHORT).show()
        }
    }

    override fun getClickedItem(data: PhoneContact) {
        closeKeyboardFromFragment(requireActivity(), searchView)
        setFragmentResult("selectedPhone", bundleOf("selectedBeneficiaryPhoneNumber" to data))
        dismiss()
    }

    private fun initView() {
        searchView = binding.searchView
        rv = binding.recyclerView
    }
}