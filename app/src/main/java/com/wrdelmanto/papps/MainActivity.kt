package com.wrdelmanto.papps

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
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
import com.wrdelmanto.papps.games.tipTacToe.TicTacToeFragment
import com.wrdelmanto.papps.ui.home.HomeFragment
import com.wrdelmanto.papps.ui.settings.SettingsActivity
import com.wrdelmanto.papps.utils.setupNavigationAndStatusBar
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var activityMain: DrawerLayout
    private lateinit var homeFragmentContainer: FragmentContainerView

    // App bar
    private lateinit var drawerIcon: ImageView
    private lateinit var appBarTitle: TextView

    // Drawer header
    private lateinit var drawerHeader: ConstraintLayout
    private var clicksHomeFragment = 0

    // Drawer
    private lateinit var drawerItemsNavView: NavigationView

    // Drawer bottom
    private lateinit var drawerBottomNavView: NavigationView

    // Random bottom nav view
    private lateinit var randomBottomNavMenu: NavigationBarView
    private lateinit var randomBottomNavMenuRandomLetter: MenuItem
    private lateinit var randomBottomNavMenuRandomnumber: MenuItem

    private lateinit var actualFragmentTag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_fragment)

        // Disable dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setupNavigationAndStatusBar(applicationContext, window)

        activityMain = findViewById(R.id.main_fragment)
        homeFragmentContainer = findViewById(R.id.home_fragment_container)

        // App bar
        drawerIcon = findViewById(R.id.app_bar_main_drawer_icon)
        appBarTitle = findViewById(R.id.app_bar_main_title)

        // Drawer header
        drawerHeader = findViewById(R.id.drawer_header)

        // Drawer
        drawerItemsNavView = findViewById(R.id.drawer_items_nav_view)

        // Drawer bottom
        drawerBottomNavView = findViewById(R.id.drawer_bottom_nav_view)

        // Random bottom nav
        randomBottomNavMenu = findViewById(R.id.random_bottom_nav_view)
        randomBottomNavMenuRandomLetter = randomBottomNavMenu.menu.findItem(R.id.random_bottom_nav_menu_random_letter)
        randomBottomNavMenuRandomnumber = randomBottomNavMenu.menu.findItem(R.id.random_bottom_nav_menu_random_number)

        switchFragment(homeFragmentContainer.id, HomeFragment(), "HOME")
    }

    override fun onResume() {
        super.onResume()

        initiateListeners()
    }

    override fun onPause() {
        disableListeners()

        super.onPause()
    }

    private fun initiateListeners() {
        // App bar
        drawerIcon.setOnClickListener { activityMain.openDrawer(GravityCompat.START) }

        // Drawer header
        drawerHeader.setOnClickListener { goHomeFragment() }

        // Drawer
        drawerItemsNavView.setNavigationItemSelectedListener { menuItem -> onNavigationItemSelected(menuItem)}

        // Drawer bottom
        drawerBottomNavView.setNavigationItemSelectedListener { menuItem -> onNavigationItemSelected(menuItem)}

        // Random bottom nav menu
        randomBottomNavMenu.setOnItemSelectedListener { item -> onNavigationItemSelected(item) }
    }

    private fun disableListeners() {
        // App bar
        drawerIcon.setOnClickListener(null)

        // Drawer header
        drawerHeader.setOnClickListener(null)

        // Drawer
        drawerItemsNavView.setNavigationItemSelectedListener(null)

        // Drawer bottom
        drawerBottomNavView.setNavigationItemSelectedListener(null)

        // Random bottom nav menu
        randomBottomNavMenu.setOnItemSelectedListener(null)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        setupNavigationAndStatusBar(applicationContext, window)
    }

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
            R.id.drawer_coin_flipper -> switchFragment(homeFragmentContainer.id, CoinFlipperFragment(), "COIN_FLIPPER")
            R.id.drawer_tic_tac_toe -> switchFragment(homeFragmentContainer.id, TicTacToeFragment(), "TIC_TAC_TOE")
            R.id.drawer_rock_paper_scissors ->
                switchFragment(homeFragmentContainer.id, RockPaperScissorsFragment(), "RPCK_PAPER_SCISSORS")

            // Drawer bottom
            R.id.drawer_bottom_settings -> openSettingsActivity()

            // Random bottom nav menu
            R.id.random_bottom_nav_menu_random_letter ->
                switchFragment(homeFragmentContainer.id, RandomLetterFragment(), "RANDOM_LETTER")
            R.id.random_bottom_nav_menu_random_number ->
                switchFragment(homeFragmentContainer.id, RandomNumberFragment(), "RANDOM_NUMBER")
        }

        updateAppBarTitle(menuItem)

        activityMain.closeDrawer(GravityCompat.START)
        return true
    }

    private fun switchFragment(fragmentId: Int, newFragment: Fragment, fragmentName: String) {
        try {
            val ft = supportFragmentManager.beginTransaction()

            if (supportFragmentManager.findFragmentById(fragmentId) == null) {
                ft.add(fragmentId, newFragment, fragmentName)
            } else if (supportFragmentManager.fragments[0].tag != fragmentName)
                ft.replace(fragmentId, newFragment, fragmentName)

            actualFragmentTag = fragmentName
            ft.addToBackStack(null)
            ft.commit()
        } catch (e: Exception) { e.printStackTrace() }

        shouldShowRandomBottomNavMenu(fragmentName)
    }

    private fun shouldShowRandomBottomNavMenu(fragmentName: String) {
        randomBottomNavMenu.isVisible = fragmentName == "RANDOM_LETTER" || fragmentName == "RANDOM_NUMBER"
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
            R.id.drawer_tic_tac_toe -> getString(R.string.app_name_tic_tac_toe)
            R.id.drawer_rock_paper_scissors -> getString(R.string.app_name_rock_paper_scissors)

            // Random bottom nav
            R.id.random_bottom_nav_menu_random_letter -> getString(R.string.app_name_random_letter)
            R.id.random_bottom_nav_menu_random_number -> getString(R.string.app_name_random_number)

            else -> ""
        }
    }

    private fun goHomeFragment() {
        if (actualFragmentTag == "HOME") return

        clicksHomeFragment++

        if (clicksHomeFragment >= CLICKS_HOME_FRAGMENT) {
            clicksHomeFragment = 0
            disableDrawerMomentarily()
            activityMain.closeDrawer(GravityCompat.START)
            switchFragment(homeFragmentContainer.id, HomeFragment(), "HOME")
        }
    }

    private fun disableDrawerMomentarily() {
        drawerIcon.setOnClickListener(null)
        drawerHeader.setOnClickListener(null)
        drawerItemsNavView.setNavigationItemSelectedListener(null)
        drawerBottomNavView.setNavigationItemSelectedListener(null)

        MainScope().launch {
            delay(ONE_SECOND_IN_MILLIS)
            drawerIcon.setOnClickListener { activityMain.openDrawer(GravityCompat.START) }
            drawerHeader.setOnClickListener { goHomeFragment() }
            drawerItemsNavView.setNavigationItemSelectedListener { menuItem -> onNavigationItemSelected(menuItem)}
            drawerBottomNavView.setNavigationItemSelectedListener { menuItem -> onNavigationItemSelected(menuItem)}
        }
    }

    fun activateEasterEgg() {
        val ticTacToe = drawerItemsNavView.menu.findItem(R.id.drawer_tic_tac_toe)
        ticTacToe.isVisible = true
    }

    private fun openSettingsActivity() = startActivity(Intent(this, SettingsActivity::class.java))

    private companion object {
        const val CLICKS_HOME_FRAGMENT = 10
        const val ONE_SECOND_IN_MILLIS = 1000L
    }
}