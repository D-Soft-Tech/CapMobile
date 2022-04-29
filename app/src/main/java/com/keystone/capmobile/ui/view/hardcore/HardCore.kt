package com.keystone.capmobile.ui.view.hardcore

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.keystone.capmobile.R
import com.keystone.capmobile.data.model.GetHardCoreResponseBody
import com.keystone.capmobile.data.model.HardCoreDataObjects
import com.keystone.capmobile.databinding.FragmentHardCoreBinding
import com.keystone.capmobile.ui.adapters.HardCoreRecyclerViewAdapter
import com.keystone.capmobile.ui.interfaces.DrawerLocker
import com.keystone.capmobile.ui.viewModel.MainViewModel
import com.keystone.capmobile.util.AppConstants.customSnackBar
import com.keystone.capmobile.util.AppConstants.getDatePicker
import com.keystone.capmobile.util.AppConstants.longDateToStringFormat
import com.keystone.capmobile.util.Resource
import com.keystone.capmobile.util.Status
import com.keystone.capmobile.util.currencyFormat
import com.keystone.capmobile.util.getCurrentDate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HardCore @Inject constructor() : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentHardCoreBinding
    private lateinit var openingBalanceTv: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var closingBalanceTv: TextView
    private lateinit var movementTv: TextView
    private lateinit var headingTv: TextView
    private lateinit var datePickerET: EditText
    private lateinit var backTextTv: TextView
    private lateinit var srchView: SearchView
    private lateinit var backArrowTv: ImageView
    private lateinit var visibilityToggleView: ImageView
    private lateinit var figuresLayout: ConstraintLayout
    private lateinit var cardSession: CardView
    private lateinit var dataToShowInSpinner: ArrayAdapter<String>
    private lateinit var rView: RecyclerView
    private lateinit var popupMenu: PopupMenu
    @Inject
    lateinit var accountsAdapter: HardCoreRecyclerViewAdapter
    private val datePickerDialog = getDatePicker("Select Date")
    private lateinit var hardCoreData: List<HardCoreDataObjects>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hard_core, container, false)
        dataToShowInSpinner = ArrayAdapter(
            requireContext(),
            R.layout.spinner_drop_down,
            resources.getStringArray(R.array.hardCoreInfoHeadings)
        )
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()

        datePickerET.setText(getCurrentDate())
        viewModel.getHardCore(getCurrentDate().replace(" - ", ""))

        backArrowTv.setOnClickListener {
            findNavController().popBackStack()
        }

        backTextTv.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getHardCore.observe(viewLifecycleOwner) {
            Log.d("dataH", it.toString())
            setValues(it)
        }

        rView.adapter = accountsAdapter

        popupMenu = PopupMenu(requireContext(), headingTv).apply {
            inflate(R.menu.menu_popup_hardcore_portfolio)
        }

        headingTv.text = (resources.getStringArray(R.array.hardCoreInfoHeadings))[0]

        headingTv.setOnClickListener {
            popupMenu.show()
        }

        popupMenu.setOnMenuItemClickListener { it ->
            when (it.itemId) {
                R.id.avgFd -> {
                    headingTv.text = getString(R.string.avgFd)
                    viewModel.getHardCore.observe(viewLifecycleOwner) { hardCoreData ->
                        setValues(hardCoreData)
                    }
                    return@setOnMenuItemClickListener true
                }
                R.id.avgCasa -> {
                    headingTv.text = getString(R.string.avgCasa)
                    viewModel.getHardCore.observe(viewLifecycleOwner) { hardCoreData ->
                        setValues(hardCoreData)
                    }
                    return@setOnMenuItemClickListener true
                }
                R.id.avgDom -> {
                    headingTv.text = getString(R.string.avgDom)
                    viewModel.getHardCore.observe(viewLifecycleOwner) { hardCoreData ->
                        setValues(hardCoreData)
                    }
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    headingTv.text = getString(R.string.avgDeposit)
                    viewModel.getHardCore.observe(viewLifecycleOwner) { hardCoreData ->
                        setValues(hardCoreData)
                    }
                    return@setOnMenuItemClickListener false
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onResume() {
        super.onResume()
        (activity as DrawerLocker).setDrawerEnabled(false)
        visibilityToggleView.setOnClickListener {
            if (figuresLayout.visibility == View.VISIBLE) {
                figuresLayout.visibility = View.GONE
                visibilityToggleView.setImageDrawable(resources.getDrawable(R.drawable.eye_icon))
                // Animates how the card collapses and expands
                TransitionManager.beginDelayedTransition(
                    cardSession,
                    AutoTransition()
                )
            } else {
                TransitionManager.beginDelayedTransition(
                    cardSession,
                    AutoTransition()
                )
                figuresLayout.visibility = View.VISIBLE
                visibilityToggleView.setImageDrawable(resources.getDrawable(R.drawable.hide_icon))
            }
        }

        datePickerET.setOnClickListener {
            datePickerDialog.show(parentFragmentManager, "Material Date Picker")
        }

        datePickerDialog.addOnPositiveButtonClickListener {
            datePickerET.setText(longDateToStringFormat(it))
            viewModel.getHardCore(longDateToStringFormat(it).replace(" - ", ""))
        }

        srchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val newHardCoreData =
                        hardCoreData.filter { it.name.lowercase().contains(newText.lowercase()) }
                    accountsAdapter.setData(newHardCoreData)
                }
                return true
            }
        })
    }

    private fun initializeViews() {
        with(binding) {
            headingTv = heading
            datePickerET = datePicker
            backTextTv = backText
            backArrowTv = backArrow
            visibilityToggleView = visibilityToggle
            figuresLayout = figures
            cardSession = cardView7
            rView = recyclerView
            srchView = searchView
            openingBalanceTv = openingBalance
            closingBalanceTv = closingBalance
            movementTv = movementBalance
            progressBar = callingHardCorePB
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setValues(data: Resource<GetHardCoreResponseBody>) {
        when (data.status) {
            Status.LOADING -> {
                progressBar.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                progressBar.visibility = View.GONE
                customSnackBar(binding.root, getString(R.string.errorPleaseTryAgain))
            }
            Status.TIMEOUT -> {
                progressBar.visibility = View.GONE
                customSnackBar(binding.root, getString(R.string.timeOut))
            }
            Status.SUCCESS -> {
                progressBar.visibility = View.GONE
                when (headingTv.text) {
                    "Avg Deposit Portfolio" -> {
                        data.data?.details?.run {
                            summary?.OpeningAvgBal?.let {
                                setOpeningClosingAndMovementBalances(
                                     it,
                                    summary?.ClosingAvgBalance ?: 0.0,
                                    mapToHardCoreDataObjects()
                                )
                            }?: setOpeningClosingAndMovementBalances(
                                0.0,
                                summary?.ClosingAvgBalance ?: 0.0,
                                mapToHardCoreDataObjects()
                            )
                        }
                    }
                    "Average DOM Portfolio" -> {
                        data.data?.details?.run {
                            setOpeningClosingAndMovementBalances(
                                summary?.DOMOpeningAvgBal?.toDouble() ?: 0.0,
                                summary?.DOMClosingAvgBalance?.toDouble() ?: 0.0,
                                mapToHardCoreDataObjects()
                            )
                        }
                    }
                    "Avg CASA Portfolio" -> {
                        data.data?.details?.run {
                            setOpeningClosingAndMovementBalances(
                                summary?.CASAOpeningAvgBal ?: 0.0,
                                summary?.CASAClosingAvgBalance ?: 0.0,
                                mapToHardCoreDataObjects()
                            )
                        }
                    }
                    "Avg FD Portfolio" -> {
                        data.data?.details?.run {
                            setOpeningClosingAndMovementBalances(
                                summary?.FixedDepositOpeningAvgBal ?: 0.0,
                                summary?.FixedDepositClosingAvgBalance?.toDouble() ?: 0.0,
                                mapToHardCoreDataObjects()
                            )
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setOpeningClosingAndMovementBalances(
        openingBal: Double?,
        closingBal: Double?,
        accountsMappedToUser: List<HardCoreDataObjects>
    ) {
        openingBalanceTv.text = openingBal?.let { currencyFormat(it) } ?: "Not Available"
        closingBalanceTv.text = closingBal?.let { currencyFormat(it) } ?: "Not Available"
        movementTv.text = closingBal?.let { closBal ->
            openingBal?.let { opBal ->
                currencyFormat((closBal - opBal))
            }
        } ?: "Not Available"
        hardCoreData = accountsMappedToUser
        accountsAdapter.setData(hardCoreData)
        accountsAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setOpeningClosingAndMovementBalances(
        openingBal: Int?,
        closingBal: Double?,
        accountsMappedToUser: List<HardCoreDataObjects>
    ) {
        openingBalanceTv.text = openingBal?.let { currencyFormat(it) } ?: "Not Available"
        closingBalanceTv.text = closingBal?.let { currencyFormat(it) } ?: "Not Available"
        movementTv.text = closingBal?.let { closBal ->
            openingBal?.let { opBal ->
                currencyFormat((closBal - opBal))
            }
        } ?: "Not Available"
        hardCoreData = accountsMappedToUser
        accountsAdapter.setData(hardCoreData)
        accountsAdapter.notifyDataSetChanged()
    }
}