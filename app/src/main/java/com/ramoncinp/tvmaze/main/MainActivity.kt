package com.ramoncinp.tvmaze.main

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ramoncinp.tvmaze.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navController by lazy { provideNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNavigationUi()
    }

    private fun setNavigationUi() {
        NavigationUI.setupActionBarWithNavController(this, navController)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(this, R.color.indigo_primary_dark)
            )
        )
        onBackPressedDispatcher.addCallback {
            if (navController.currentDestination?.id == R.id.scheduleFragment) {
                finish()
            } else {
                navController.navigateUp()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun provideNavController(): NavController {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                as NavHostFragment
        return navHostFragment.navController
    }
}
