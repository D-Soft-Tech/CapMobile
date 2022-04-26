package com.keystone.capmobile.ui.view.dialogFragments

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.keystone.capmobile.R
import com.keystone.capmobile.data.model.DataToPassToDaoEngageDialog
import com.keystone.capmobile.databinding.DaoMenuDialogLayoutBinding
import com.keystone.capmobile.ui.view.MyDaoAccountsDirections
import com.keystone.capmobile.util.AppConstants.PHONE_CALL_PERMISSION_CODE
import com.keystone.capmobile.util.AppConstants.genericPermissionHandler
import javax.inject.Inject

class DaoEngageIndividualDialogFragment @Inject constructor(): DialogFragment() {
    private lateinit var callTv: TextView
    private lateinit var engageTv: TextView
    private var _binding: DaoMenuDialogLayoutBinding? = null
    private val binding get() = _binding!!
    private var daoToEngage : DataToPassToDaoEngageDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("DaoToEngage") { _, bundle ->
            daoToEngage = bundle.getParcelable("phoneNumberOfTheIndividual")
            Log.d("ssss", daoToEngage.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DaoMenuDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.apply {
            setBackgroundDrawableResource(R.drawable.layout_page_with_curved_bg)
            attributes?.windowAnimations = R.style.slideInFromRight
            val data = daoToEngage
            val xCoordinate = data?.locationOnScreen?.get(0)
            val yCoordinate = data?.locationOnScreen?.get(1)?.minus(900)?.minus(data.heightOfMenuIcon)
            val p = attributes
            p?.apply {
                softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                xCoordinate?.let { x = it }
                yCoordinate?.let { y = it }
            }
            attributes = p
        }

        callTv.setOnClickListener {
            dismiss()
            genericPermissionHandler(
                this@DaoEngageIndividualDialogFragment,
                requireContext(),
                Manifest.permission.CALL_PHONE,
                PHONE_CALL_PERMISSION_CODE,
                getString(
                    R.string.phoneCallPermissionRationale,
                    daoToEngage?.name
                )
            ) {
                val callIntent = Intent(
                    Intent.ACTION_CALL,
                    Uri.parse(
                        "tel: ${Uri.encode(daoToEngage?.number)}"
                    )
                )
                startActivity(callIntent)
            }
        }

        engageTv.setOnClickListener {
            dismiss()
            val action = MyDaoAccountsDirections.actionMyDaoAccountsToEngageIndividualCustomerScreen(daoToEngage!!)
            findNavController().navigate(action)
        }
    }

    private fun initViews() {
        callTv = binding.callTv
        engageTv = binding.engageTv
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}