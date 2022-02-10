package com.example.capmobile.ui.view.hardcore

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentHardCoreBinding
import com.example.capmobile.data.model.HardCoreDataObjects
import com.example.capmobile.ui.adapters.HardCoreRecyclerViewAdapter
import com.example.capmobile.ui.interfaces.DrawerLocker
import com.example.capmobile.util.AppConstants.getDatePicker
import com.example.capmobile.util.AppConstants.getHardCoreData
import com.example.capmobile.util.AppConstants.longDateToStringFormat


class HardCore : Fragment() {

    private lateinit var binding: FragmentHardCoreBinding
    private lateinit var headingTv: TextView
    private lateinit var datePicker: EditText
    private lateinit var backText: TextView
    private lateinit var searchView: SearchView
    private lateinit var backArrow: ImageView
    private lateinit var visibilityToggle: ImageView
    private lateinit var figuresLayout: ConstraintLayout
    private lateinit var cardSession: CardView
    private lateinit var dataToShowInSpinner: ArrayAdapter<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var popupMenu: PopupMenu
    private val accountsAdapter: HardCoreRecyclerViewAdapter by lazy { HardCoreRecyclerViewAdapter() }
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

        hardCoreData = getHardCoreData()

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()

        backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        backText.setOnClickListener {
            findNavController().popBackStack()
        }

        var alert = AlertDialog.Builder(requireContext()).create()
        alert.apply {
            setMessage("This is an Aleart")
        }

        accountsAdapter.setData(hardCoreData)
        recyclerView.adapter = accountsAdapter

        popupMenu = PopupMenu(requireContext(), headingTv).apply {
            inflate(R.menu.menu_popup_hardcore_portfolio)
        }

        headingTv.text = (resources.getStringArray(R.array.hardCoreInfoHeadings))[0]

        headingTv.setOnClickListener {
            popupMenu.show()
        }

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.avgFd -> {
                    headingTv.text = getString(R.string.avgFd)
                    return@setOnMenuItemClickListener true
                }
                R.id.avgCasa -> {
                    headingTv.text = getString(R.string.avgCasa)
                    return@setOnMenuItemClickListener true
                }
                R.id.avgDom -> {
                    headingTv.text = getString(R.string.avgDom)
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    headingTv.text = getString(R.string.avgDeposit)
                    return@setOnMenuItemClickListener false
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onResume() {
        super.onResume()

        (activity as DrawerLocker).setDrawerEnabled(false)

        visibilityToggle.setOnClickListener {
            if (figuresLayout.visibility == View.VISIBLE) {
                figuresLayout.visibility = View.GONE
                visibilityToggle.setImageDrawable(resources.getDrawable(R.drawable.eye_icon))
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
                visibilityToggle.setImageDrawable(resources.getDrawable(R.drawable.hide_icon))
            }
        }

        datePicker.setOnClickListener {
            datePickerDialog.show(parentFragmentManager, "Material Date Picker")
        }

        datePickerDialog.addOnPositiveButtonClickListener {
            datePicker.setText(longDateToStringFormat(it))
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        headingTv = binding.heading
        datePicker = binding.datePicker
        backText = binding.backText
        backArrow = binding.backArrow
        visibilityToggle = binding.visibilityToggle
        figuresLayout = binding.figures
        cardSession = binding.cardView7
        recyclerView = binding.recyclerView
        searchView = binding.searchView
    }
}