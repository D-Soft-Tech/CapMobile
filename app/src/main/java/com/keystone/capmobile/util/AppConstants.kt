package com.keystone.capmobile.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.findViewTreeLifecycleOwner
import coil.load
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.keystone.capmobile.R
import com.keystone.capmobile.data.model.BarChartModel
import com.keystone.capmobile.data.model.HardCoreDataObjects
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.tapadoo.alerter.Alerter
import com.tapadoo.alerter.OnHideAlertListener
import com.tapadoo.alerter.OnShowAlertListener
import pub.devrel.easypermissions.EasyPermissions
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startActivity

import android.telephony.PhoneNumberUtils

import android.content.ComponentName
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import dagger.hilt.android.qualifiers.ApplicationContext


object AppConstants {

    data class AccountSummary(
        var accName: String,
        var accNumber: String,
        var accType: String,
        var accOfficer: String,
        var accOfficerNum: String,
        var branchAddress: String,
        var branchCode: String
    )

    fun bitmapToBase64(bitmap: Bitmap): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(byteArray)
        } else {
            android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
        }
    }

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

    fun Fragment.customToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.customSnackBar(rootView: View, message: String) {
        Snackbar.make(
            requireContext(),
            rootView,
            message,
            Snackbar.LENGTH_LONG
        ).show()
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
        SimpleDateFormat("yyyy - MM - dd", Locale.getDefault()).format(dateInLong).split(" - ")
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

    fun customPleaseWaitDialog(context: Context): AlertDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_please_wait, null)
        return AppConstants.getCustomDialog(context, view.rootView).apply {
            setCanceledOnTouchOutside(false)
        }
    }

    fun getBitmapFromUri(uri: Uri, context: Context): Bitmap =
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

    fun getDormieAccountSummary() =
        AccountSummary(
            "OLOYEDE ADEBAYO OLAWALE",
            "1234567890",
            "Individual Savings Account",
            "Olise Elizabeth",
            "09087654321",
            "Keystone Head office",
            "0001"
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

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getBalloonForError(context: Context, text: String, view: View) =
        Balloon.Builder(context).apply {
            height = BalloonSizeSpec.WRAP
            width = BalloonSizeSpec.WRAP // #e06666
            setText(text)
            setTextColorResource(R.color.white)
            setTextSize(15f)
            arrowPositionRules = ArrowPositionRules.ALIGN_ANCHOR
            arrowSize = 30
            setArrowPosition(0.5f)
            setPadding(5)
            autoDismissDuration = 10000L
            backgroundColor = context.resources.getColor(R.color.error_color_2)
            balloonAnimation = BalloonAnimation.ELASTIC
            lifecycleOwner = view.findViewTreeLifecycleOwner()
        }.build().showAlignTop(view)


    fun getHardCoreData() = listOf(
        HardCoreDataObjects(
            "Kunle Felix",
            "3074983501",
            "Dormant",
            234,
            "798475860"
        ),
        HardCoreDataObjects(
            "Nduka Emmanuel",
            "9074903801",
            "Inactive",
            216,
            "31405890"
        ),
        HardCoreDataObjects(
            "Aleme Emenike",
            "0074583521",
            "Dormant",
            247,
            "998575830"
        ),
        HardCoreDataObjects(
            "Kolawole Ajao",
            "6874903509",
            "Active",
            206,
            "28475860"
        ),
        HardCoreDataObjects(
            "Benjamin Anne",
            "0174803579",
            "Active",
            209,
            "8479860"
        ),
        HardCoreDataObjects(
            "Kunle Felix",
            "3074983501",
            "Dormant",
            234,
            "798475860"
        ),
        HardCoreDataObjects(
            "Nduka Emmanuel",
            "9074903801",
            "Inactive",
            216,
            "31405890"
        ),
        HardCoreDataObjects(
            "Aleme Emenike",
            "0074583521",
            "Dormant",
            247,
            "998575830"
        ),
        HardCoreDataObjects(
            "Kolawole Ajao",
            "6874903509",
            "Active",
            206,
            "28475860"
        ),
        HardCoreDataObjects(
            "Benjamin Anne",
            "0174803579",
            "Active",
            209,
            "8479860"
        ),
        HardCoreDataObjects(
            "Kunle Felix",
            "3074983501",
            "Dormant",
            234,
            "798475860"
        ),
        HardCoreDataObjects(
            "Nduka Emmanuel",
            "9074903801",
            "Inactive",
            216,
            "31405890"
        ),
        HardCoreDataObjects(
            "Aleme Emenike",
            "0074583521",
            "Dormant",
            247,
            "998575830"
        ),
        HardCoreDataObjects(
            "Kolawole Ajao",
            "6874903509",
            "Active",
            206,
            "28475860"
        ),
        HardCoreDataObjects(
            "Benjamin Anne",
            "0174803579",
            "Active",
            209,
            "8479860"
        ),
        HardCoreDataObjects(
            "Kunle Felix",
            "3074983501",
            "Dormant",
            234,
            "798475860"
        ),
        HardCoreDataObjects(
            "Nduka Emmanuel",
            "9074903801",
            "Inactive",
            216,
            "31405890"
        ),
        HardCoreDataObjects(
            "Aleme Emenike",
            "0074583521",
            "Dormant",
            247,
            "998575830"
        ),
        HardCoreDataObjects(
            "Kolawole Ajao",
            "6874903509",
            "Active",
            206,
            "28475860"
        ),
        HardCoreDataObjects(
            "Benjamin Anne",
            "0174803579",
            "Active",
            209,
            "8479860"
        )
    )

    const val BASE_URL = "http://41.203.110.24:4011/"

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
    fun BarChart.showBarChart(
        labels: ArrayList<String>,
        cardFunding: ArrayList<BarChartModel>,
        colors: ArrayList<Int>
    ) {
        val entries: ArrayList<BarEntry> = ArrayList()
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
        description = Description().apply { text = "" }
        this.data = data
        legend.isEnabled = false
        xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            labelCount = labels.size
            removeAllLimitLines()
        }
        animateY(1000)
        invalidate()
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

    fun getDialog(
        context: Context,
        items: Array<String>,
        camera: () -> Unit,
        gallery: () -> Unit,
        sign: () -> Unit
    ) =
        MaterialAlertDialogBuilder(context)
            .setTitle(context.resources.getString(R.string.cameraOrGalleryOrSign))
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
                    2 -> {
                        sign()
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

    fun TextInputEditText.validateInput(): Boolean {
        return if (text.toString().trim().isEmpty()) {
            getBalloonForError(this.context, resources.getString(R.string.fieldsCantBeEmpty), this)
            false
        } else {
            true
        }
    }

    fun EditText.validateInput(): Boolean {
        return if (text.toString().trim().isEmpty()) {
            getBalloonForError(this.context, resources.getString(R.string.fieldsCantBeEmpty), this)
            false
        } else {
            true
        }
    }

    fun sendMessageViaEmail(
        rootView: View,
        emailAddress: Array<String>,
        subject: String,
        message: String,
        activity: Activity
    ) {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.apply {
            data = Uri.parse("mailto:")
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, emailAddress)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

        try {
            //start email intent
            startActivity(
                rootView.context,
                Intent.createChooser(mIntent, "Choose Email Client..."),
                null
            )
        } catch (e: Exception) {
            showAlerter(
                rootView,
                activity,
                rootView.resources.getString(R.string.errorPleaseTryAgain)
            ) {}
        }
    }

    fun openWhatsApp(
        view: View,
        subject: String,
        message: String,
        toNumber: String,
        activity: Activity
    ) {
        // PhoneNumberUtils.stripSeparators(formattedNumber) + "@s.whatsapp.net"
        val text = "$subject \n$message"
        val intent = Intent(Intent.ACTION_VIEW)
        val isWhatsappInstalled = isPackageInstalled(view.context, "com.whatsapp")
        intent.apply {
            Log.d("PHONE_NUMBER", toNumber)
            data = Uri.parse("http://api.whatsapp.com/send?phone=$toNumber&text=$text")
            `package` = "com.whatsapp"
        }
        if (isWhatsappInstalled) {
            try {
                startActivity(view.context, intent, null)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                showAlerter(
                    view,
                    activity,
                    view.resources.getString(R.string.anErrorOccurred)
                ) {}
            }
        } else {
            showAlerter(
                view,
                activity,
                view.resources.getString(R.string.pleaseInstallWhatsapp)
            ) {}
        }
    }

    fun isPackageInstalled(context: Context, packageName: String): Boolean =
        try {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (ex: Exception) {
            false
        }

    fun showAlerter(v: View, activity: Activity, alertMessage: String, clickAction: () -> Unit) {
        Alerter.create(activity)
            .setTitle(v.resources.getString(R.string.alert))
            .setText(alertMessage)
            .setIcon(
                AppCompatResources.getDrawable(v.context, R.drawable.ic_alert)!!
            )
            .setBackgroundColorRes(
                R.color.error_color_2
            )
            .setDuration(3000)
            .setOnClickListener {
                clickAction()
            }
            .setOnShowListener {
            }
            .setOnHideListener {
            }
            .show()
    }
}

fun currencyFormat(amount: Double?): String {
    val formatter = DecimalFormat("#,###,##0.00")
    return amount?.let { formatter.format(it) } ?: "0"
}

fun Double.roundToDecimalPlace(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

fun currencyFormat(amount: Int?): String {
    val formatter = DecimalFormat("#,###,##0.00")
    return amount?.let { formatter.format(it) } ?: "0"
}

fun getCurrentDate(): String =
    SimpleDateFormat("yyyy - MM - dd", Locale.getDefault()).format(Date())
