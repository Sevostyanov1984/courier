package com.cdek.courier.ui.start.init

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import com.cdek.courier.R
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import com.nabinbhandari.android.permissions.Permissions
import com.nabinbhandari.android.permissions.PermissionHandler
import android.content.Context
import android.net.Uri
import android.provider.Settings
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController


class InitFragment : DaggerFragment() {

    private val appPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this@InitFragment, viewModelFactory)[InitViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_init, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.checkDefaultServer()

        initObserve()

//        viewModel.getFCMToken()
    }

    private fun initObserve() {
        viewModel.navigateToBase.observe(this@InitFragment, Observer {
            if (it) {
                viewModel.navigateToBaseHandled()
                startBaseActivity()
            }
        })

        viewModel.navigateToAuth.observe(this@InitFragment, Observer {
            if (it) {
                viewModel.navigateToAuthHandled()
                startAuthFragment()
            }
        })

        viewModel.message.observe(this@InitFragment, Observer {
            if (it.isNotEmpty()) {
                showMessage(it)
                viewModel.messageHandled()
            }
        })
    }

    override fun onStart() {
        super.onStart()

        Permissions.check(
            activity, appPermissions, null, null, object : PermissionHandler() {
                override fun onGranted() {
                    viewModel.permissionsGranted()
                }

                override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {
                    runSettingPermission()
                }
            })
    }

    //    if permission not granted
    fun runSettingPermission() {
        showMessage(getString(R.string.need_allow_access))
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", activity?.packageName, null)
        )
        startActivity(intent)
    }

    private fun showMessage(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }

    private fun startAuthFragment() {
        findNavController().navigate(R.id.action_initFragment_to_authFragment)
    }

    private fun startBaseActivity() {
        findNavController().navigate(R.id.action_initFragment_to_baseActivity)
        activity?.finish()
    }
}