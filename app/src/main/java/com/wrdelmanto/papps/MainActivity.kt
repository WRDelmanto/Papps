package com.wrdelmanto.papps

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity.LEFT
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.wrdelmanto.papps.apps.clickCounter.ClickCounterFragment
import com.wrdelmanto.papps.apps.random.randomLetter.RandomLetterFragment
import com.wrdelmanto.papps.apps.random.randomNumber.RandomNumberFragment
import com.wrdelmanto.papps.apps.tip.TipFragment
import com.wrdelmanto.papps.games.coinFlipper.CoinFlipperFragment
import com.wrdelmanto.papps.games.rockPaperScissors.RockPaperScissorsFragment
import com.wrdelmanto.papps.utils.hideSystemUi
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.switchNavBarColor
import com.wrdelmanto.papps.utils.switchStatusBarColor

class MainActivity :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    NavigationBarView.OnItemReselectedListener
{
    // Drawer
    private lateinit var activityMain: DrawerLayout
    private lateinit var homeFragmentContainer: FragmentContainerView
    private lateinit var drawer: NavigationView
    private lateinit var drawerIcon: ImageView

    // Drawer Bottom
    private lateinit var drawerBottom: NavigationView

    // Random bottom nav view
    private lateinit var randomBottomNavMenu: NavigationBarView
    private lateinit var randomBottomNavMenuRandomLetter: MenuItem
    private lateinit var randomBottomNavMenuRandomnumber: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Drawer
        activityMain = findViewById(R.id.activity_main)
        homeFragmentContainer = findViewById(R.id.home_fragment_container)
        drawer = findViewById(R.id.drawer)
        drawerIcon = findViewById(R.id.imageView_drawer)

        // Drawer Bottom
        drawerBottom = findViewById(R.id.drawer_bottom)

        // Random bottom nav
        randomBottomNavMenu = findViewById(R.id.random_bottom_nav_view)
        randomBottomNavMenuRandomLetter = randomBottomNavMenu.menu.findItem(R.id.random_bottom_nav_menu_random_letter)
        randomBottomNavMenuRandomnumber = randomBottomNavMenu.menu.findItem(R.id.random_bottom_nav_menu_random_number)

        setupNavigationAndStatusBar()
        initiateListeners()
    }

    @SuppressLint("RtlHardcoded")
    private fun initiateListeners() {
        // Drawer
        drawerIcon.setOnClickListener { activityMain.openDrawer(LEFT) }
        drawer.setNavigationItemSelectedListener { menuItem -> onNavigationItemSelected(menuItem)}

        // Drawer Bottom
        drawerBottom.setNavigationItemSelectedListener { menuItem -> onNavigationItemSelected(menuItem)}

        // Random bottom nav
        randomBottomNavMenu.apply {
            setOnItemSelectedListener { item -> onNavigationItemSelected(item) }
            setOnItemReselectedListener { item -> onNavigationItemReselected(item) }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        setupNavigationAndStatusBar()
    }

    @SuppressLint("RtlHardcoded")
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            // Drawer
            // Apps
            R.id.drawer_click_counter -> switchFragment(homeFragmentContainer.id, ClickCounterFragment())
            R.id.drawer_random_letter -> {
                switchFragment(homeFragmentContainer.id, RandomLetterFragment())
                randomBottomNavMenuRandomLetter.isChecked = true
            }
            R.id.drawer_random_number -> {
                switchFragment(homeFragmentContainer.id, RandomNumberFragment())
                randomBottomNavMenuRandomnumber.isChecked = true
            }
            R.id.drawer_tip -> switchFragment(homeFragmentContainer.id, TipFragment())
            // Games
            R.id.drawer_coin_flipper -> switchFragment(homeFragmentContainer.id, CoinFlipperFragment())
            R.id.drawer_rock_paper_scissors -> switchFragment(homeFragmentContainer.id, RockPaperScissorsFragment())

            // Drawer Bottom
            R.id.drawer_bottom_privacy_policy -> openPrivacyPolicy()

            // Random bottom nav
            R.id.random_bottom_nav_menu_random_letter -> switchFragment(homeFragmentContainer.id, RandomLetterFragment())
            R.id.random_bottom_nav_menu_random_number -> switchFragment(homeFragmentContainer.id, RandomNumberFragment())
        }

        // Random bottom nav
        randomBottomNavMenu.isVisible =
            menuItem.itemId == R.id.drawer_random_letter ||
            menuItem.itemId == R.id.drawer_random_number ||
            menuItem.itemId == R.id.random_bottom_nav_menu_random_letter ||
            menuItem.itemId == R.id.random_bottom_nav_menu_random_number

        activityMain.closeDrawer(LEFT)
        return true
    }

    override fun onNavigationItemReselected(menuItem: MenuItem) {
        when (menuItem.itemId) {
            // Random bottom nav
            R.id.random_bottom_nav_menu_random_letter -> logD { getString(R.string.fragment_already_open, "Random Letter") }
            R.id.random_bottom_nav_menu_random_number -> logD { getString(R.string.fragment_already_open, "Random Number") }
        }
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
        switchNavBarColor(applicationContext, window)
        switchStatusBarColor(applicationContext, window)
        hideSystemUi(window)
    }

    companion object {
        /**
         * Privacy Policy URL
         */
        const val PRIVACY_POLICY_URL = "https://docs.google.com/document/d/17-S5qZQoqmxN6jkHTWNhW89zdrjVmwkkGNKwauYqPvY"
    }
}