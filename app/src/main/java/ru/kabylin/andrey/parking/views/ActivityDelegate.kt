package ru.kabylin.andrey.parking.views

import android.app.Activity
import android.support.annotation.LayoutRes
import android.view.Menu

interface ActivityDelegate<T : Activity> {
    fun setContentView(@LayoutRes layout: Int, activity: T) {
        activity.setContentView(layout)
    }

    fun onCreateOptionsMenu(menu: Menu) {
    }
}
