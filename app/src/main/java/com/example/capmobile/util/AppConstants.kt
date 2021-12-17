package com.example.capmobile.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import coil.load
import com.example.capmobile.R
import com.example.capmobile.model.BarChartModel
import com.example.capmobile.model.DaoChannelObject
import com.example.capmobile.model.StatusCardDetails
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pub.devrel.easypermissions.EasyPermissions


object AppConstants {

    fun dormantDataForChannelsObjects() =
        listOf(
            DaoChannelObject("Kolawole Ajao", mobile = true, ussd = true, iBank = false, cards = true, "Active"),
            DaoChannelObject("Kunle Felix", mobile = true, ussd = false, iBank = false, cards = false, "Dormant"),
            DaoChannelObject("Benjamin Anne", mobile = true, ussd = true, iBank = false, cards = true, "Inactive"),
            DaoChannelObject("Kolawole Ajao", mobile = true, ussd = true, iBank = false, cards = true, "Active"),
            DaoChannelObject("Kunle Felix", mobile = true, ussd = false, iBank = false, cards = false, "Dormant"),
            DaoChannelObject("Benjamin Anne", mobile = true, ussd = true, iBank = false, cards = true, "Inactive"),
            DaoChannelObject("Kolawole Ajao", mobile = true, ussd = true, iBank = false, cards = true, "Active"),
            DaoChannelObject("Kunle Felix", mobile = true, ussd = false, iBank = false, cards = false, "Dormant"),
            DaoChannelObject("Benjamin Anne", mobile = true, ussd = true, iBank = false, cards = true, "Inactive"),
            DaoChannelObject("Kolawole Ajao", mobile = true, ussd = true, iBank = false, cards = true, "Active"),
            DaoChannelObject("Kunle Felix", mobile = true, ussd = false, iBank = false, cards = false, "Dormant"),
            DaoChannelObject("Benjamin Anne", mobile = true, ussd = true, iBank = false, cards = true, "Inactive"),
            DaoChannelObject("Kolawole Ajao", mobile = true, ussd = true, iBank = false, cards = true, "Active"),
            DaoChannelObject("Kunle Felix", mobile = true, ussd = false, iBank = false, cards = false, "Dormant"),
            DaoChannelObject("Benjamin Anne", mobile = true, ussd = true, iBank = false, cards = true, "Inactive"),
            DaoChannelObject("Kolawole Ajao", mobile = true, ussd = true, iBank = false, cards = true, "Active"),
            DaoChannelObject("Kunle Felix", mobile = true, ussd = false, iBank = false, cards = false, "Dormant"),
            DaoChannelObject("Benjamin Anne", mobile = true, ussd = true, iBank = false, cards = true, "Inactive"),
            DaoChannelObject("Agunbiade Oladapo", mobile = true, ussd = true, iBank = true, cards = true, "Active")
        )

    fun getStatus() =
        listOf(
            StatusCardDetails(
                "Accounts",
                R.drawable.ic_person_white,
                34,
                R.color.status_card_sample_color,
                null
            ),
            StatusCardDetails(
                "Cards",
                R.drawable.ic_credit_card,
                3,
                R.color.cards,
                8.82
            ),
            StatusCardDetails(
                "Mobile",
                R.drawable.ic_phone,
                5,
                R.color.mobile,
                14.71
            ),
            StatusCardDetails(
                "USSD",
                R.drawable.ic_phone_ussd,
                5,
                R.color.ussd,
                14.71
            ),
            StatusCardDetails(
                "IBank",
                R.drawable.ic_bank,
                5,
                R.color.internet_banking,
                14.71
            )
        )

    fun PieChart.drawChart() {
        val typeAmount: Map<String, Int> = hashMapOf()
        val label = "type"
        val pieEntry: ArrayList<PieEntry> = arrayListOf()
        typeAmount.plus(Pair("Active", 500000))
        typeAmount.plus(Pair("Inactive", 318040))
        typeAmount.plus(Pair("Dormant", 2089))

        val colors: ArrayList<Int> = arrayListOf()
        val colorCodes: ArrayList<String> = arrayListOf("#f6b683", "#ffa5a4", "#93dd80", "#9685fc")
        colorCodes.forEach {
            colors.add(Color.parseColor(it))
        }

        typeAmount.keys.forEach {
            typeAmount[it]?.let { it1 -> PieEntry(it1.toFloat(), it) }
                ?.let { it2 -> pieEntry.add(it2) }
        }

        val pieDataSet = PieDataSet(pieEntry, label).apply {
            valueTextSize = 12f
            setColors(colors)
        }

        val pieData = PieData(pieDataSet).apply { setDrawValues(true) }
        this.data = pieData
        this.invalidate()
    }

    fun PieChart.showPieChart() {
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = "type"


        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap["Active"] = 500000
        typeAmountMap["Inactive"] = 318040
        typeAmountMap["Dormant"] = 20898

        //initializing colors for the entries
        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#94dd80"))
        colors.add(Color.parseColor("#f5b783"))
        colors.add(Color.parseColor("#ffa5a4"))
        colors.add(Color.parseColor("#93d480"))

        //input data and fit data into pie chart entry
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }

        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        pieDataSet.valueTextSize = 12f
        pieDataSet.apply {
            setEntryLabelColor(resources.getColor(R.color.main_color))
        }
        //providing color list for coloring different entries
        pieDataSet.colors = colors
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true)
        this.data = pieData
        this.invalidate()
    }

    fun BarChart.showBarChart() {
        val valueList = ArrayList<Double>()
        val entries: ArrayList<BarEntry> = ArrayList()
        val labels: ArrayList<String> = arrayListOf("Funded", "Unfunded", "Dormant")
        val cardFunding: ArrayList<BarChartModel> = arrayListOf(
            BarChartModel(labels[0], 5000000),
            BarChartModel(labels[1], 3189040),
            BarChartModel(labels[2], 2008598)
        )

        //input data
        for (i in 0..3) {
            valueList.add(i * 100.1)
        }

        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#94dd80"))
        colors.add(Color.parseColor("#f5b783"))
        colors.add(Color.parseColor("#ffa5a4"))
        colors.add(Color.parseColor("#93d480"))

        //fit the data into a bar
        cardFunding.forEachIndexed { index, data ->
            entries.add(
                BarEntry(
                    index.toFloat(),
                    data.num.toFloat()
                )
            )
        }

        val barDataSet = BarDataSet(entries, "Number of Cards")
        val data = BarData(barDataSet)
        barDataSet.colors = colors
        this.description = Description().apply { text = "Cards" }
        this.data = data
        val xAxis = this.xAxis
        xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            setDrawAxisLine(false)
            granularity = 1f
            labelCount = labels.size
            labelRotationAngle = 270F
        }
        this.animateY(2000)
        this.invalidate()
    }

    const val STORAGE_PERM_REQUEST_CODE = 111
    const val CAMERA_PERM_REQUEST_CODE = 112

    fun checkForPermission(context: Context, perms: String) =
        EasyPermissions.hasPermissions(
            context,
            perms
        )

    fun requestForPermission(
        host: Fragment,
        requestCode: Int,
        permissionRationale: String,
        permissionToRequest: String
    ) {
        EasyPermissions.requestPermissions(
            host,
            permissionRationale,
            requestCode,
            permissionToRequest
        )
    }

    fun getImageLoader(imageView: ImageView, imgBitmap: Bitmap) {
        imageView.load(imgBitmap) {
            crossfade(true)
            crossfade(1000)
        }
    }

    fun cameraPermissionHandler(host: Fragment, context: Context, fn: () -> Unit) {
        if (checkForPermission(context, Manifest.permission.CAMERA)) {
            fn()
        } else {
            requestForPermission(
                host,
                CAMERA_PERM_REQUEST_CODE,
                context.resources.getString(R.string.camera_permission_rationale),
                Manifest.permission.CAMERA
            )
        }
    }

    fun galleryPermissionHandler(host: Fragment, context: Context, fn: () -> Unit) {
        if (checkForPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            fn()
        } else {
            requestForPermission(
                host,
                STORAGE_PERM_REQUEST_CODE,
                context.resources.getString(R.string.storage_permission_rationale),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    fun getCustomDialog(context: Context, layoutBinding: View): AlertDialog.Builder {
        return AlertDialog.Builder(context).apply {
            setView(layoutBinding)
            create()
        }
    }

    fun getDialog(context: Context, items: Array<String>, camera: () -> Unit, gallery: () -> Unit) =
        MaterialAlertDialogBuilder(context)
            .setTitle(context.resources.getString(R.string.cameraOrGallery))
            .setItems(items)
            { dialog, which ->
                when (which) {
                    0 -> {
                        dialog.dismiss()
                        camera()
                    }
                    1 -> {
                        dialog.dismiss()
                        gallery()
                    }
                }
            }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}