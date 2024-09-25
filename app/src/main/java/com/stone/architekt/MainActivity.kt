package com.stone.architekt

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setScreenProperties()
    }

    private fun setScreenProperties() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For API level 30 and above
            window.insetsController?.let { controller ->
                // Hide the status bar but leave the navigation bar visible
                controller.hide(WindowInsets.Type.statusBars())
                // Keep the navigation bar visible
                controller.show(WindowInsets.Type.navigationBars())
            }
        } else {
            // For API level 29 and below, use deprecated flags
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  // Layout behind the navigation bar
                            or View.SYSTEM_UI_FLAG_FULLSCREEN  // Hide the status bar
                    )
        }
    }

}
