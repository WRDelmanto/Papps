package com.wrdelmanto.papps

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity.LEFT
import android.view.MenuItem
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.navigation.NavigationView
import com.wrdelmanto.papps.apps.clickCounter.ClickCounterFragment
import com.wrdelmanto.papps.apps.randomNumber.RandomNumberFragment
import com.wrdelmanto.papps.apps.tip.TipFragment
import com.wrdelmanto.papps.games.coinFlipper.CoinFlipperFragment
import com.wrdelmanto.papps.games.ticTacToe.TicTacToeFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var activityMain: DrawerLayout
    private lateinit var homeFragmentContainer: FragmentContainerView
    private lateinit var drawer: NavigationView
    private lateinit var drawerBottom: NavigationView
    private lateinit var drawerIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityMain = findViewById(R.id.activity_main)
        homeFragmentContainer = findViewById(R.id.home_fragment_container)
        drawerIcon = findViewById(R.id.imageView_drawer)
        drawer = findViewById(R.id.drawer)
        drawerBottom = findViewById(R.id.drawer_bottom)

        setupNavigationAndStatusBar()
        initiateListeners()
    }

    @SuppressLint("RtlHardcoded")
    private fun initiateListeners() {
        drawerIcon.setOnClickListener { activityMain.openDrawer(LEFT) }
        drawer.setNavigationItemSelectedListener { menuItem -> onNavigationItemSelected(menuItem)}
        drawerBottom.setNavigationItemSelectedListener { menuItem -> onNavigationItemSelected(menuItem)}
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        setupNavigationAndStatusBar()
    }

    @SuppressLint("RtlHardcoded")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Apps
            R.id.item_click_counter -> switchFragment(homeFragmentContainer.id, ClickCounterFragment())
            R.id.item_random_number -> switchFragment(homeFragmentContainer.id, RandomNumberFragment())
            R.id.item_tip -> switchFragment(homeFragmentContainer.id, TipFragment())

            // Games
            R.id.item_coin_flipper -> switchFragment(homeFragmentContainer.id, CoinFlipperFragment())
            R.id.item_tic_tac_toe -> switchFragment(homeFragmentContainer.id, TicTacToeFragment())

            // Drawer Bottom
            R.id.item_privacy_policy -> openPrivacyPolicy()
        }

        activityMain.closeDrawer(LEFT)
        return true
    }

    private fun switchFragment(fragmentId: Int, newFragment: Fragment) {
        try {
            val ft = supportFragmentManager.beginTransaction()

            if (supportFragmentManager.findFragmentById(fragmentId) == null) {
                ft.add(fragmentId, newFragment)
            } else {
                ft.replace(fragmentId, newFragment)
            }

            ft.addToBackStack(null)
            ft.commit()
        } catch (e: Exception) { e.printStackTrace() }
    }

    /**
     * Show Privacy Policy Screen
     */
    private fun openPrivacyPolicy() = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL)))

    /**
     * Setup navigation and status bar
     */
    @Suppress("DEPRECATION")
    private fun setupNavigationAndStatusBar() {
        changeSystemUiColor()

        // Hide navigation bar = SYSTEM_UI_FLAG_HIDE_NAVIGATION and SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        // Hide status bar = SYSTEM_UI_FLAG_FULLSCREEN
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_HIDE_NAVIGATION or SYSTEM_UI_FLAG_IMMERSIVE_STICKY or SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun changeSystemUiColor () {
        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.navigation_bar_color)
        window.navigationBarColor = ContextCompat.getColor(applicationContext, R.color.navigation_bar_color)
    }

    @Suppress("DEPRECATION")
    private fun hideSystemUi() {
        window.decorView.systemUiVisibility = (
            SYSTEM_UI_FLAG_LOW_PROFILE
                or SYSTEM_UI_FLAG_FULLSCREEN
                or SYSTEM_UI_FLAG_LAYOUT_STABLE
                or SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    @Suppress("DEPRECATION")
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (
            SYSTEM_UI_FLAG_LAYOUT_STABLE
                or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    companion object {
        /**
         * Privacy Policy URL
         */
        const val PRIVACY_POLICY_URL = "https://docs.google.com/document/d/17-S5qZQoqmxN6jkHTWNhW89zdrjVmwkkGNKwauYqPvY"
    }
}