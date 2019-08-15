package com.company.app.core.navigation

import android.content.Context
import android.content.Intent
import com.company.app.presentation.MainActivity

/**
 * Created on 2019/01/24.
 */
class IActivityNavigation : ActivityNavigation {
    override fun openMain(context: Context?) {
        val intent = Intent(context?.applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.applicationContext?.startActivity(intent)
    }
}