package com.company.app.presentation

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.company.app.R
import com.company.app.presentation.BaseActivity
import kotlinx.android.synthetic.main.include_toolbar.view.*

/**
 * Created on 2018/12/03.
 */
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {
        fun setUpToolbar(view: View, activity: Activity?, title: String? = null, enableHomeButton: Boolean = false) {
            val thisActivity = activity as? BaseActivity
            thisActivity?.setSupportActionBar(view.toolbar)
            val actionBar = thisActivity?.supportActionBar
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(enableHomeButton)
                actionBar.setDisplayHomeAsUpEnabled(enableHomeButton)
                if (title != null) actionBar.title = title
            }
        }
    }
}