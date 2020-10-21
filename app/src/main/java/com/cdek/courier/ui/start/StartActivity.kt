package com.cdek.courier.ui.start

import android.os.Bundle
import com.cdek.courier.R
import dagger.android.support.DaggerAppCompatActivity

class StartActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }
}