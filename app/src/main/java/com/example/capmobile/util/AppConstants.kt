package com.example.capmobile.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.findViewTreeLifecycleOwner
import coil.load
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.example.capmobile.R
import com.example.capmobile.data.model.BarChartModel
import com.example.capmobile.data.model.DaoChannelObject
import com.example.capmobile.data.model.HardCoreDataObjects
import com.example.capmobile.data.model.StatusCardDetails
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import pub.devrel.easypermissions.EasyPermissions
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


object AppConstants {

    data class AccountSummary(
        var accName: String,
        var accNumber: String,
        var accType: String,
        var accOfficer: String,
        var accOfficerNum: String
    )

    fun View.handleClickThatFirstShowDialog(
        alertDialog: AlertDialog,
        negativeView: View,
        positiveView: View,
        negativeAction: () -> Unit,
        positiveAction: () -> Unit
    ) {
        this.setOnClickListener {
            alertDialog.show()
            negativeView.setOnClickListener {
                alertDialog.dismiss()
                negativeAction()
            }
            positiveView.setOnClickListener {
                alertDialog.dismiss()
                positiveAction()
            }
        }
    }

    fun getDatePicker(title: String): MaterialDatePicker<Long> {
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(title)
            .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
            .setTheme(R.style.ThemeOverlay_App_DatePicker)

        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        calendar.timeInMillis = today

        // Build constraints.
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setEnd(calendar.month.toLong())
                .setOpenAt(calendar.dayOfMonth.toLong())

        return datePicker.setCalendarConstraints(
            CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now()).build()
        ).build()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun createAnotherCustomizableDialog(view: View) =
        getCustomDialog(view.context, view).apply {
            window?.setBackgroundDrawable(view.context.getDrawable(R.drawable.layout_page_with_curved_bg))
            window?.attributes?.windowAnimations = R.style.dialogAnimation
        }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun createAnotherCustomizableDialogWithSlideInRightAnim(view: View, menuIcon: View) =
        getCustomDialog(view.context, view).apply {
            window?.apply {
                setBackgroundDrawable(view.context.getDrawable(R.drawable.layout_page_with_curved_bg))
                attributes?.windowAnimations = R.style.slideInFromRight
                val location = IntArray(2)
                menuIcon.getLocationOnScreen(location)
                val xCoordinate = location[0]
                val yCoordinate = location[1]

                attributes.apply {
                    x = xCoordinate
                    y = yCoordinate - 900 - menuIcon.height
                }
            }
        }

    fun getCustomDialog(context: Context, layoutBinding: View): AlertDialog {
        return AlertDialog.Builder(context).apply {
            setView(layoutBinding)
        }.create()
    }

    fun closeSoftKeyboard(context: Context, activity: Activity) {
        activity.currentFocus?.let { view ->
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun closeKeyboardFromFragment(activity: Activity, view: View) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        // hide the keyboard
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun copyToClipBoard(context: Context, text: String, label: String) {
        val clipboard: ClipboardManager? =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText(label, text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(
            context,
            context.getString(R.string.copiedToClipBoard, label),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun handleClickThatFirstShowDialog(
        alertDialog: AlertDialog,
        negativeView: View,
        positiveView: View,
        negativeAction: () -> Unit,
        positiveAction: () -> Unit
    ) {
        alertDialog.show()
        negativeView.setOnClickListener {
            alertDialog.dismiss()
            negativeAction()
        }
        positiveView.setOnClickListener {
            alertDialog.dismiss()
            positiveAction()
        }
    }

    fun handleClickThatFirstShowDialog(
        fm: FragmentManager,
        alertDialog: DialogFragment,
        negativeView: View,
        positiveView: View,
        negativeAction: () -> Unit,
        positiveAction: () -> Unit
    ) {
        alertDialog.show(fm, "DialogFragment")
        negativeView.setOnClickListener {
            alertDialog.dismiss()
            negativeAction()
        }

        positiveView.setOnClickListener {
            alertDialog.dismiss()
            positiveAction()
        }
    }

    fun outPutDir(activity: Activity): File {
        val mediaDir = activity.externalMediaDirs.firstOrNull()?.let { mPrev ->
            File(mPrev, activity.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }

        return if (mediaDir != null && mediaDir.exists())
            mediaDir else activity.filesDir
    }

    fun longDateToStringFormat(dateInLong: Long) =
        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(dateInLong).split("-")
            .mapIndexed { index, s ->
                if (index == 1) {
                    val holder = s.toInt().toString()
                    if (holder.toCharArray().size <= 1) {
                        "0$holder"
                    } else {
                        holder
                    }
                } else {
                    s
                }
            }.joinToString(" - ")

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val fileName = SimpleDateFormat("yy-MM-dd-HH-mm-ss", Locale.getDefault()).format(
            System.currentTimeMillis()
        )
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, fileName, null)
        return Uri.parse(path.toString())
    }

    @SuppressLint("Range")
    fun getFileName(uri: Uri, context: Context): String? {
        var result: String? = null
        if (uri.scheme.equals("content")) {
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            cursor.use { cursor ->
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        result =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                if (cut != null) {
                    result = result?.substring(cut + 1)
                }
            }
        }
        return result
    }

    fun getBitmapFromUri(uri: Uri, context: Context): Bitmap =
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

    fun getDormieAccountSummary() =
        AccountSummary(
            "OLOYEDE ADEBAYO OLAWALE",
            "1234567890",
            "Individual Savings Account",
            "Olise Elizabeth",
            "09087654321"
        )

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getBalloon(context: Context, text: String, view: View) = Balloon.Builder(context).apply {
        height = BalloonSizeSpec.WRAP
        width = BalloonSizeSpec.WRAP
        setText(text)
        setTextColorResource(R.color.white)
        setTextSize(15f)
        iconDrawable = context.getDrawable(R.drawable.ic_edit)
        arrowPositionRules = ArrowPositionRules.ALIGN_ANCHOR
        arrowSize = 30
        setArrowPosition(0.5f)
        setPadding(5)
        autoDismissDuration = 10000L
        backgroundColor = context.resources.getColor(R.color.lekanColor)
        balloonAnimation = BalloonAnimation.ELASTIC
        lifecycleOwner = view.findViewTreeLifecycleOwner()
    }.build().showAlignTop(view)


    fun getHardCoreData() = listOf(
        HardCoreDataObjects(
            "Kunle Felix",
            "3074983501",
            "Dormant",
            234,
            798475860
        ),
        HardCoreDataObjects(
            "Nduka Emmanuel",
            "9074903801",
            "Inactive",
            216,
            31405890
        ),
        HardCoreDataObjects(
            "Aleme Emenike",
            "0074583521",
            "Dormant",
            247,
            998575830
        ),
        HardCoreDataObjects(
            "Kolawole Ajao",
            "6874903509",
            "Active",
            206,
            28475860
        ),
        HardCoreDataObjects(
            "Benjamin Anne",
            "0174803579",
            "Active",
            209,
            8479860
        ),
        HardCoreDataObjects(
            "Kunle Felix",
            "3074983501",
            "Dormant",
            234,
            798475860
        ),
        HardCoreDataObjects(
            "Nduka Emmanuel",
            "9074903801",
            "Inactive",
            216,
            31405890
        ),
        HardCoreDataObjects(
            "Aleme Emenike",
            "0074583521",
            "Dormant",
            247,
            998575830
        ),
        HardCoreDataObjects(
            "Kolawole Ajao",
            "6874903509",
            "Active",
            206,
            28475860
        ),
        HardCoreDataObjects(
            "Benjamin Anne",
            "0174803579",
            "Active",
            209,
            8479860
        ),
        HardCoreDataObjects(
            "Kunle Felix",
            "3074983501",
            "Dormant",
            234,
            798475860
        ),
        HardCoreDataObjects(
            "Nduka Emmanuel",
            "9074903801",
            "Inactive",
            216,
            31405890
        ),
        HardCoreDataObjects(
            "Aleme Emenike",
            "0074583521",
            "Dormant",
            247,
            998575830
        ),
        HardCoreDataObjects(
            "Kolawole Ajao",
            "6874903509",
            "Active",
            206,
            28475860
        ),
        HardCoreDataObjects(
            "Benjamin Anne",
            "0174803579",
            "Active",
            209,
            8479860
        ),
        HardCoreDataObjects(
            "Kunle Felix",
            "3074983501",
            "Dormant",
            234,
            798475860
        ),
        HardCoreDataObjects(
            "Nduka Emmanuel",
            "9074903801",
            "Inactive",
            216,
            31405890
        ),
        HardCoreDataObjects(
            "Aleme Emenike",
            "0074583521",
            "Dormant",
            247,
            998575830
        ),
        HardCoreDataObjects(
            "Kolawole Ajao",
            "6874903509",
            "Active",
            206,
            28475860
        ),
        HardCoreDataObjects(
            "Benjamin Anne",
            "0174803579",
            "Active",
            209,
            8479860
        )
    )

    fun dormantDataForChannelsObjects() =
        listOf(
            DaoChannelObject(
                "Kolawole Ajao",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = true,
                iBank = false,
                cards = true,
                "Active"
            ),
            DaoChannelObject(
                "Kunle Felix",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = false,
                iBank = false,
                cards = false,
                "Dormant"
            ),
            DaoChannelObject(
                "Benjamin Anne",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = true,
                iBank = false,
                cards = true,
                "Inactive"
            ),
            DaoChannelObject(
                "Kolawole Ajao",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = true,
                iBank = false,
                cards = true,
                "Active"
            ),
            DaoChannelObject(
                "Kunle Felix",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = false,
                iBank = false,
                cards = false,
                "Dormant"
            ),
            DaoChannelObject(
                "Benjamin Anne",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = true,
                iBank = false,
                cards = true,
                "Inactive"
            ),
            DaoChannelObject(
                "Kolawole Ajao",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = true,
                iBank = false,
                cards = true,
                "Active"
            ),
            DaoChannelObject(
                "Kunle Felix",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = false,
                iBank = false,
                cards = false,
                "Dormant"
            ),
            DaoChannelObject(
                "Benjamin Anne",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = true,
                iBank = false,
                cards = true,
                "Inactive"
            ),
            DaoChannelObject(
                "Kolawole Ajao",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = true,
                iBank = false,
                cards = true,
                "Active"
            ),
            DaoChannelObject(
                "Kunle Felix",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = false,
                iBank = false,
                cards = false,
                "Dormant"
            ),
            DaoChannelObject(
                "Benjamin Anne",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = true,
                iBank = false,
                cards = true,
                "Inactive"
            ),
            DaoChannelObject(
                "Kolawole Ajao",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = true,
                iBank = false,
                cards = true,
                "Active"
            ),
            DaoChannelObject(
                "Kunle Felix",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = false,
                iBank = false,
                cards = false,
                "Dormant"
            ),
            DaoChannelObject(
                "Benjamin Anne",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = true,
                iBank = false,
                cards = true,
                "Inactive"
            ),
            DaoChannelObject(
                "Kolawole Ajao",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = true,
                iBank = false,
                cards = true,
                "Active"
            ),
            DaoChannelObject(
                "Kunle Felix",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = false,
                iBank = false,
                cards = false,
                "Dormant"
            ),
            DaoChannelObject(
                "Benjamin Anne",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = true,
                iBank = false,
                cards = true,
                "Inactive"
            ),
            DaoChannelObject(
                "Agunbiade Oladapo",
                phoneNumber = "09075771869",
                mobile = true,
                ussd = true,
                iBank = true,
                cards = true,
                "Active"
            )
        )

    const val BASE_URL = "http://41.203.110.24:4011/"

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

    @RequiresApi(Build.VERSION_CODES.Q)
    fun BarChart.showBarChart() {
        val entries: ArrayList<BarEntry> = ArrayList()
        val labels: ArrayList<String> =
            arrayListOf("IBanking", "USSD", "Mobile", "Cards", "Accounts")
        val cardFunding: ArrayList<BarChartModel> = arrayListOf(
            BarChartModel(labels[0], 164789),
            BarChartModel(labels[1], 264779),
            BarChartModel(labels[2], 364750),
            BarChartModel(labels[3], 464798),
            BarChartModel(labels[4], 564790)
        )

        val colors: Array<Int> = arrayOf(
            context.getColor(R.color.lekanColor40Alpha),
            context.getColor(R.color.lekanColor60Alpha),
            context.getColor(R.color.lekanColor75Alpha),
            context.getColor(R.color.lekanColor90Alpha),
            context.getColor(R.color.lekanColor)
        )

        //fit the data into a bar
        cardFunding.forEachIndexed { index, data ->
            entries.add(
                BarEntry(
                    index.toFloat(),
                    data.num.toFloat()
                )
            )
        }

        axisRight.apply {
            setDrawTopYLabelEntry(false)
            valueFormatter = IndexAxisValueFormatter(arrayListOf())
            gridColor = context.getColor(R.color.white)
            axisLineColor = context.getColor(R.color.white)
        }

        axisLeft.apply {
            setDrawTopYLabelEntry(false)
            valueFormatter = IndexAxisValueFormatter(arrayListOf())
            axisLineWidth = 0f
            gridColor = context.getColor(R.color.white)
            axisLineColor = context.getColor(R.color.white)
        }

        val barDataSet = BarDataSet(entries, "Number of Cards")
        val data = BarData(barDataSet)
        barDataSet.colors = colors.toMutableList()
        this.description = Description().apply { text = "" }
        this.data = data
        legend.isEnabled = false
        xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            labelCount = labels.size
            removeAllLimitLines()
        }
        this.animateY(1000)
        this.invalidate()
    }

    const val STORAGE_PERM_REQUEST_CODE = 111
    const val WRITE_PERMISSION_REQUEST_CODE = 113
    const val CAMERA_PERM_REQUEST_CODE = 112
    const val PHONE_CONTACT_PERM_REQUEST_CODE = 114
    const val PHONE_CALL_PERMISSION_CODE = 115
    const val PROTO_DATASTORE_FILE_NAME = "protoDataStore"

    private fun checkForPermission(context: Context, perms: String) =
        EasyPermissions.hasPermissions(
            context,
            perms
        )

    fun genericPermissionHandler(
        host: Fragment,
        context: Context,
        perm: String,
        permCode: Int,
        permRationale: String,
        fn: () -> Unit
    ) {
        if (checkForPermission(context, perm)) {
            fn()
        } else {
            requestForPermission(
                host,
                permCode,
                permRationale,
                perm
            )
        }
    }

    private fun requestForPermission(
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

    fun getMomentOfTheDay(context: Context): String {

        val formattedDate =
            SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(System.currentTimeMillis())
                .format(Date()).split(" ").mapIndexed { index, s ->
                    if (index == 0) {
                        s.split(":")[0]
                    } else {
                        s
                    }
                }

        return when (formattedDate[1]) {

            "PM" -> {
                if (formattedDate[0].toInt() < 4) {
                    context.getString(R.string.afternoon)
                } else {
                    context.getString(R.string.evening)
                }
            }
            else -> {
                context.getString(R.string.morning)
            }
        }
    }
}