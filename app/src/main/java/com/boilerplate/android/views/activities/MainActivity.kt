package com.boilerplate.android.views.activities

import android.os.Bundle
import com.boilerplate.android.R
import com.boilerplate.android.core.views.activities.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}