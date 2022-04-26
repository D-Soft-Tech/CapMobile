package com.keystone.capmobile.ui.view.loginFlow

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.keystone.capmobile.R
import com.keystone.capmobile.data.dataStore.AppProtoDataStore
import com.keystone.capmobile.data.model.LoginCredentials
import com.keystone.capmobile.databinding.FragmentNewLoginBinding
import com.keystone.capmobile.databinding.LoadingDialogLayoutBinding
import com.keystone.capmobile.ui.view.MainActivity
import com.keystone.capmobile.ui.viewModel.MainViewModel
import com.keystone.capmobile.util.AppConstants
import com.keystone.capmobile.util.AppConstants.getCustomDialog
import com.keystone.capmobile.util.AppConstants.hideKeyboard
import com.keystone.capmobile.util.DataStoreManager
import com.keystone.capmobile.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewLoginFragment : Fragment() {
    private lateinit var binding: FragmentNewLoginBinding
    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var dataStore: DataStoreManager
    private lateinit var proceedBtn: Button
    private lateinit var userName: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var fingerPrintScannerIV: ImageView
    private lateinit var dialogBinding: LoadingDialogLayoutBinding
    private lateinit var dialog: AlertDialog

    @Inject
    lateinit var protoDataStore: AppProtoDataStore

    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    // If the authentication is successful, navigate user into the main app
                    handleFingerPrintLogin()
                }
            }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        dialogBinding = LoadingDialogLayoutBinding.inflate(layoutInflater)
        dialog = getCustomDialog(requireContext(), dialogBinding.root).apply {
            setCanceledOnTouchOutside(false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onResume() {
        super.onResume()
        proceedBtn.setOnClickListener {

            hideKeyboard()

            if (userName.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()) {
                // PERFORM LOGIN
                login(userName.text.toString(), password.text.toString())
            } else {
                Snackbar.make(
                    requireContext(),
                    proceedBtn,
                    resources.getString(R.string.allFieldsAreRequired),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        // or login with fingerPrint
        fingerPrintScannerIV.setOnClickListener {
            // === I only commented the below out so that i don't have to input username and passwords everytime before logging in ===//
            if (checkBiometricSupport()) {
                val biometricPrompt: BiometricPrompt =
                    BiometricPrompt.Builder(requireContext()).apply {
                        setTitle(resources.getString(R.string.biometricTitle))
                        setSubtitle(resources.getString(R.string.biometricSubTitle))
                        setDescription(resources.getString(R.string.biometricDescription))
                        setNegativeButton(
                            resources.getString(R.string.cancel),
                            requireActivity().mainExecutor,
                            { _, _ -> })
                    }.build()
                biometricPrompt.authenticate(
                    getCancellationSignal(),
                    requireActivity().mainExecutor,
                    authenticationCallback
                )
            }
        }
    }

    private fun initializeViews() {
        proceedBtn = binding.proceedBtn
        userName = binding.userNameTIET
        password = binding.passwordTIET
        fingerPrintScannerIV = binding.fingerPrint
    }

    private fun notifyUser(message: String) {
        Snackbar.make(requireContext(), binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser(resources.getString(R.string.fingerPrintAuthDeclined))
        }
        return cancellationSignal as CancellationSignal
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkBiometricSupport(): Boolean {
        val keyguardManager: KeyguardManager =
            activity?.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardSecure) {
            notifyUser(resources.getString(R.string.fingerPrintNotEnabled))
            return false
        }
        return requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }

    private fun handleFingerPrintLogin() {
        lifecycleScope.launch {
            protoDataStore.readProto.collect {
                Log.d("fromProto", it.userName)
                if (it.userName.isNotEmpty() && it.password.isNotEmpty()) {
                    login(it.userName, it.password)
                } else {
                    Snackbar.make(
                        requireContext(),
                        binding.root,
                        getString(R.string.noAccountFound),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun login(inputtedUserName: String, inputtedPassword: String) {
        viewModel.login(
            LoginCredentials(
                inputtedUserName,
                inputtedPassword
            )
        )

        viewModel.loginResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    dialog.show()
                }
                Status.TIMEOUT -> {
                    dialog.dismiss()
                }
                Status.ERROR -> {
                    dialog.dismiss()
                    Snackbar.make(
                        requireContext(),
                        proceedBtn,
                        resources.getString(R.string.loginErrorMessage),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                Status.SUCCESS -> {
                    dialog.dismiss()

                    Snackbar.make(
                        requireContext(),
                        proceedBtn,
                        it.message,
                        Snackbar.LENGTH_SHORT
                    ).show()

                    lifecycleScope.launch {
                        it.data?.details?.run {
                            dataStore.saveToken(token)
                            Log.d("NameName", this.toString())
                            protoDataStore.updateSavedUser(
                                inputtedUserName,
                                inputtedPassword,
                                name,
                                daocode,
                                if(branches.isNotEmpty()) branches[0].BRANCH_NAME else ""
                            )
                        }
                    }

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        })
    }
}