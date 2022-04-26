package com.keystone.capmobile.ui.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.keystone.capmobile.R
import com.keystone.capmobile.data.model.BarChartModel
import com.keystone.capmobile.databinding.CardFundingStatusBinding
import com.keystone.capmobile.ui.viewModel.MainViewModel
import com.keystone.capmobile.util.AppConstants.customSnackBar
import com.keystone.capmobile.util.AppConstants.showBarChart
import com.keystone.capmobile.util.Status
import com.github.mikephil.charting.charts.BarChart
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CardFundingStatus @Inject constructor() : Fragment() {
    private lateinit var binding: CardFundingStatusBinding
    private lateinit var refresh: SwipeRefreshLayout
    private lateinit var backArrow: ImageView
    private lateinit var backTextToGoBackToPreviousPage: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var accountSummaryBarChart: BarChart
    private lateinit var labels: ArrayList<String>
    private lateinit var cardFunding: ArrayList<BarChartModel>
    private lateinit var colors: ArrayList<Int>
    private val viewModel: MainViewModel by activityViewModels()
//    private lateinit var accountStatusAdapter: ArrayAdapter<String>
//    private lateinit var accoutStatusDropDown: AutoCompleteTextView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        labels =
            arrayListOf("IBanking", "USSD", "Mobile", "Cards", "Accounts")

        colors = arrayListOf(
            requireContext().getColor(R.color.lekanColor40Alpha),
            requireContext().getColor(R.color.lekanColor60Alpha),
            requireContext().getColor(R.color.lekanColor75Alpha),
            requireContext().getColor(R.color.lekanColor90Alpha),
            requireContext().getColor(R.color.lekanColor)
        )

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.card_funding_status, container, false)

//        accountStatusAdapter = ArrayAdapter(
//            requireContext(),
//            R.layout.exposed_dropdown_menu_layout,
//            resources.getStringArray(R.array.statuses_with_all)
//        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
//        accoutStatusDropDown.setAdapter(accountStatusAdapter)

        getAccountSummary()
    }

    override fun onResume() {
        super.onResume()
        backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        backTextToGoBackToPreviousPage.setOnClickListener {
            findNavController().popBackStack()
        }

        refresh.setOnRefreshListener {
            getAccountSummary()
        }
    }

    private fun initializeViews() {
        with(binding) {
            backArrow = imageButton
            accountSummaryBarChart = barChart
            progressBar = callingAccountSummaryPB
            refresh = swipeRefreshLayout
            backTextToGoBackToPreviousPage = backText
        }
    }

    private fun getAccountSummary() {
        viewModel.getAccountSummary()
        viewModel.getAccountSummary.observe(viewLifecycleOwner) {
            when(it.status) {
                Status.LOADING -> { progressBar.visibility = View.VISIBLE }
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    binding.accSummary = it.data
                    binding.lifecycleOwner = viewLifecycleOwner
                    it.data?.details?.dashboard?.run {
                        cardFunding = arrayListOf(
                            BarChartModel(labels[0], totalActiveIbank),
                            BarChartModel(labels[1], totalActiveUSSD),
                            BarChartModel(labels[2], totalActiveMobile),
                            BarChartModel(labels[3], totalActiveCards),
                            BarChartModel(labels[4], totalAccounts)
                        )
                    }

                    // populate data into bar chart
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        accountSummaryBarChart.showBarChart(labels, cardFunding, colors)
                    }
                    refresh.isRefreshing = false
                }
                Status.ERROR -> {
                    refresh.isRefreshing = false
                    progressBar.visibility = View.GONE
                    customSnackBar(binding.root, getString(R.string.errorPleaseRefreshToTryAgain))
                }
                Status.TIMEOUT -> {
                    refresh.isRefreshing = false
                    progressBar.visibility = View.GONE
                    customSnackBar(binding.root, getString(R.string.timeoutPleaseRefreshToTryAgain))
                }
            }
        }
    }
}