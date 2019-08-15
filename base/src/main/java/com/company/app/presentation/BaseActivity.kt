package com.company.app.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.company.app.R

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.TemplateTheme_Default)
    }
}
