package com.cdek.courier.ui.base.task.mapDialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cdek.courier.R
import com.cdek.courier.ui.base.map.TwoGisHelper

class MapDialog : DialogFragment() {

    companion object {
        var TAG = "ProblemDialog"
        var receiver: String? = ""
        var address: String? = ""
        var code: String? = ""
        var city: String? = ""
        var lat: String? = ""
        var lon: String? = ""

        fun show(
            fragment: Fragment,
            _receiver: String?,
            _address: String?,
            _code: String?,
            _city: String?,
            _lat: String?,
            _lon: String?
        ) {
            MapDialog().apply {
                arguments = Bundle().apply {
                    putString("receiver", receiver)
                    putString("address", address)
                }
                address = _address
                receiver = _receiver
                code = _code
                city = _city
                lat = _lat
                lon = _lon
            }.show(
                fragment.childFragmentManager,
                TAG
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val rootView = LayoutInflater.from(getContext())
            .inflate(R.layout.mapdialog, null, false);

        val yandex = rootView.findViewById(R.id.cv_yandex) as CardView
        val gis = rootView.findViewById(R.id.cv_2gis) as CardView

        return AlertDialog.Builder(context!!)
            .setView(rootView)
            .create()
            .apply {
                setCanceledOnTouchOutside(false);
                setCancelable(false)

                yandex.setOnClickListener({
                    this.dismiss()
                    //findNavController().navigate(R.id.yandexMap)

                    //val bundle = bundleOf("key_address" to address, "city_name" to city )
                    val bundle = bundleOf("mapType" to "route")
                    findNavController().navigate(R.id.yandexMap, bundle)

                })
                gis.setOnClickListener({
                    val twoGisHelper = TwoGisHelper()
                    twoGisHelper.onCreate()
                    if ("".equals(address)) {
                        twoGisHelper.handle2Gis(activity, code, address)
                    } else {
                        twoGisHelper.handle2Gis(
                            activity, code, receiver
                        )
                    }

                })
            }
    }

}