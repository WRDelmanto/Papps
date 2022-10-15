package com.wrdelmanto.papps

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity.LEFT
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
import com.wrdelmanto.papps.ui.home.HomeFragment
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.setupNavigationAndStatusBar

class MainActivity :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    NavigationBarView.OnItemReselectedListener
{
    // App Bar
    private lateinit var drawerIcon: ImageView
    private lateinit var appBarTitle: TextView

    // Drawer
    private lateinit var activityMain: DrawerLayout
    private lateinit var homeFragmentContainer: FragmentContainerView
    private lateinit var drawer: NavigationView

    // Drawer Bottom
    private lateinit var drawerBottom: NavigationView

    // Random bottom nav view
    private lateinit var randomBottomNavMenu: NavigationBarView
    private lateinit var randomBottomNavMenuRandomLetter: MenuItem
    private lateinit var randomBottomNavMenuRandomnumber: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Disable Dark Theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // App Bar
        drawerIcon = findViewById(R.id.app_bar_main_drawer)
        appBarTitle = findViewById(R.id.app_bar_main_title)

        // Drawer
        activityMain = findViewById(R.id.activity_main)
        homeFragmentContainer = findViewById(R.id.home_fragment_container)
        drawer = findViewById(R.id.drawer)

        // Drawer Bottom
        drawerBottom = findViewById(R.id.drawer_bottom)

        // Random bottom nav
        randomBottomNavMenu = findViewById(R.id.random_bottom_nav_view)
        randomBottomNavMenuRandomLetter = randomBottomNavMenu.menu.findItem(R.id.random_bottom_nav_menu_random_letter)
        randomBottomNavMenuRandomnumber = randomBottomNavMenu.menu.findItem(R.id.random_bottom_nav_menu_random_number)

        switchFragment(homeFragmentContainer.id, HomeFragment(), "HOME")
        setupNavigationAndStatusBar(applicationContext, window)
        initiateListeners()
    }

    @SuppressLint("RtlHardcoded")
    private fun initiateListeners() {
        // App Bar
        drawerIcon.setOnClickListener { activityMain.openDrawer(LEFT) }

        // Drawer
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
        setupNavigationAndStatusBar(applicationContext, window)
    }

    @SuppressLint("RtlHardcoded")
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            // Drawer
            // Apps
            R.id.drawer_click_counter ->
                switchFragment(homeFragmentContainer.id, ClickCounterFragment(), "CLICK_COUNTER")
            R.id.drawer_random_letter -> {
                switchFragment(homeFragmentContainer.id, RandomLetterFragment(), "RANDOM_LETTER")
                randomBottomNavMenuRandomLetter.isChecked = true
            }
            R.id.drawer_random_number -> {
                switchFragment(homeFragmentContainer.id, RandomNumberFragment(), "RANDOM_NUMBER")
                randomBottomNavMenuRandomnumber.isChecked = true
            }
            R.id.drawer_tip -> switchFragment(homeFragmentContainer.id, TipFragment(), "TIP")
            // Games
            R.id.drawer_coin_flipper ->
                switchFragment(homeFragmentContainer.id, CoinFlipperFragment(), "COIN_FLIPPER")
            R.id.drawer_rock_paper_scissors ->
                switchFragment(homeFragmentContainer.id, RockPaperScissorsFragment(), "RPCK_PAPER_SCISSORS")

            // Drawer Bottom
            R.id.drawer_bottom_privacy_policy -> openPrivacyPolicy()

            // Random bottom nav
            R.id.random_bottom_nav_menu_random_letter ->
                switchFragment(homeFragmentContainer.id, RandomLetterFragment(), "RANDOM_LETTER")
            R.id.random_bottom_nav_menu_random_number ->
                switchFragment(homeFragmentContainer.id, RandomNumberFragment(), "RANDOM_NUMBER")
        }

        // Random bottom nav
        randomBottomNavMenu.isVisible =
            menuItem.itemId == R.id.drawer_random_letter ||
            menuItem.itemId == R.id.drawer_random_number ||
            menuItem.itemId == R.id.random_bottom_nav_menu_random_letter ||
            menuItem.itemId == R.id.random_bottom_nav_menu_random_number

        updateAppBarTitle(menuItem)

        activityMain.closeDrawer(LEFT)
        return true
    }

    override fun onNavigationItemReselected(menuItem: MenuItem) {
        when (menuItem.itemId) {
            // Random bottom nav
            R.id.random_bottom_nav_menu_random_letter ->
                logD { getString(R.string.fragment_already_open, "RANDOM_LETTER") }
            R.id.random_bottom_nav_menu_random_number ->
                logD { getString(R.string.fragment_already_open, "RANDOM_NUMBER") }
        }
    }

    private fun switchFragment(fragmentId: Int, newFragment: Fragment, fragmentName: String) {
        try {
            val ft = supportFragmentManager.beginTransaction()

            if (supportFragmentManager.findFragmentById(fragmentId) == null) {
                ft.add(fragmentId, newFragment, fragmentName)
            } else if (supportFragmentManager.fragments[0].tag != fragmentName)
                ft.replace(fragmentId, newFragment, fragmentName)

            ft.addToBackStack(null)
            ft.commit()
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun updateAppBarTitle(menuItem: MenuItem) {
        appBarTitle.text = when (menuItem.itemId) {
            // Apps
            R.id.drawer_click_counter -> getString(R.string.app_name_click_counter)
            R.id.drawer_random_letter -> getString(R.string.app_name_random_letter)
            R.id.drawer_random_number -> getString(R.string.app_name_random_number)
            R.id.drawer_tip -> getString(R.string.app_name_tip)

            // Games
            R.id.drawer_coin_flipper -> getString(R.string.app_name_coin_flipper)
            R.id.drawer_rock_paper_scissors -> getString(R.string.app_name_rock_paper_scissors)

            // Random bottom nav
            R.id.random_bottom_nav_menu_random_letter -> getString(R.string.app_name_random_letter)
            R.id.random_bottom_nav_menu_random_number -> getString(R.string.app_name_random_number)

            else -> ""
        }
    }

    /**
     * Show Privacy Policy Screen
     */
    private fun openPrivacyPolicy() = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL)))

    companion object {
        /**
         * Privacy Policy URL
         */
        const val PRIVACY_POLICY_URL = "https://docs.google.com/document/d/17-S5qZQoqmxN6jkHTWNhW89zdrjVmwkkGNKwauYqPvY"
    }
}