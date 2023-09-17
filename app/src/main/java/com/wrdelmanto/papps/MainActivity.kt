package com.wrdelmanto.papps

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat.START
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.wrdelmanto.papps.SharedViewModel.Companion.BODY_MASS_INDEX
import com.wrdelmanto.papps.SharedViewModel.Companion.CLICK_COUNTER
import com.wrdelmanto.papps.SharedViewModel.Companion.COIN_FLIPPER
import com.wrdelmanto.papps.SharedViewModel.Companion.DICES
import com.wrdelmanto.papps.SharedViewModel.Companion.HOME
import com.wrdelmanto.papps.SharedViewModel.Companion.MONEY_CONVERTER
import com.wrdelmanto.papps.SharedViewModel.Companion.NASA_PICTURE_OF_THE_DAY
import com.wrdelmanto.papps.SharedViewModel.Companion.RANDOM_LETTER
import com.wrdelmanto.papps.SharedViewModel.Companion.RANDOM_NUMBER
import com.wrdelmanto.papps.SharedViewModel.Companion.RANDOM_QUOTE
import com.wrdelmanto.papps.SharedViewModel.Companion.ROCK_PAPER_SCISSORS
import com.wrdelmanto.papps.SharedViewModel.Companion.SPEED_TEST
import com.wrdelmanto.papps.SharedViewModel.Companion.TIC_TAC_TOE
import com.wrdelmanto.papps.SharedViewModel.Companion.TIP
import com.wrdelmanto.papps.SharedViewModel.Companion.UNSCRAMBLE
import com.wrdelmanto.papps.apps.bodyMassIndex.BodyMassIndexFragment
import com.wrdelmanto.papps.apps.clickCounter.ClickCounterFragment
import com.wrdelmanto.papps.apps.dice.DicesFragment
import com.wrdelmanto.papps.apps.moneyConverter.MoneyConverterFragment
import com.wrdelmanto.papps.apps.nasaPictureOfTheDay.NasaPictureOfTheDayFragment
import com.wrdelmanto.papps.apps.random.randomLetter.RandomLetterFragment
import com.wrdelmanto.papps.apps.random.randomNumber.RandomNumberFragment
import com.wrdelmanto.papps.apps.random.randomQuote.RandomQuoteFragment
import com.wrdelmanto.papps.apps.speedTest.SpeedTestFragment
import com.wrdelmanto.papps.apps.tip.TipFragment
import com.wrdelmanto.papps.databinding.MainFragmentBinding
import com.wrdelmanto.papps.games.coinFlipper.CoinFlipperFragment
import com.wrdelmanto.papps.games.rockPaperScissors.RockPaperScissorsFragment
import com.wrdelmanto.papps.games.tipTacToe.TicTacToeFragment
import com.wrdelmanto.papps.games.unscramble.UnscrambleFragment
import com.wrdelmanto.papps.ui.home.HomeFragment
import com.wrdelmanto.papps.ui.settings.SettingsActivity
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.setupNavigationAndStatusBar

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: MainFragmentBinding

    private val sharedViewModel: SharedViewModel by viewModels()

    private lateinit var activityMain: DrawerLayout
    private lateinit var homeFragmentContainer: FragmentContainerView

    // App bar
    private lateinit var drawerIcon: ImageView
    private lateinit var appBarTitle: TextView

    // Drawer header
    private lateinit var drawerHeader: ConstraintLayout

    // Drawer
    private lateinit var drawerItemsNavView: NavigationView

    // Drawer bottom
    private lateinit var drawerBottomNavView: NavigationView

    // Random bottom nav view
    private lateinit var randomBottomNavMenu: NavigationBarView
    private lateinit var randomBottomNavMenuRandomLetter: MenuItem
    private lateinit var randomBottomNavMenuRandomnumber: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityMain = binding.mainFragment
        homeFragmentContainer = findViewById(R.id.home_fragment_container)

        // Handle onBackPressed
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing
                logD { "onBackPressed" }
            }
        })

        // App bar
        drawerIcon = findViewById(R.id.app_bar_main_drawer_icon)
        appBarTitle = findViewById(R.id.app_bar_main_title)

        // Drawer header
        drawerHeader = binding.drawer

        // Drawer
        drawerItemsNavView = binding.drawerItemsNavView

        // Drawer bottom
        drawerBottomNavView = binding.drawerBottomNavView

        // Random bottom nav
        randomBottomNavMenu = findViewById(R.id.random_bottom_nav_view)
        randomBottomNavMenuRandomLetter =
            randomBottomNavMenu.menu.findItem(R.id.random_bottom_nav_menu_random_letter)
        randomBottomNavMenuRandomnumber =
            randomBottomNavMenu.menu.findItem(R.id.random_bottom_nav_menu_random_number)

        switchFragment(homeFragmentContainer.id, HomeFragment(applicationContext), HOME)
    }

    override fun onResume() {
        super.onResume()

        // Disable dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Adjust navigation and status bar
        setupNavigationAndStatusBar(applicationContext, window)

        initiateListeners()
        initiateObservers()
    }

    override fun onPause() {
        disableListeners()
        disableObservers()

        super.onPause()
    }

    private fun initiateListeners() {
        // App bar
        drawerIcon.setOnClickListener { activityMain.openDrawer(START) }

        // Drawer header
        drawerHeader.setOnClickListener { goHomeFragment() }

        // Drawer
        drawerItemsNavView.setNavigationItemSelectedListener { menuItem ->
            onNavigationItemSelected(
                menuItem
            )
        }

        // Drawer bottom
        drawerBottomNavView.setNavigationItemSelectedListener { menuItem ->
            onNavigationItemSelected(
                menuItem
            )
        }

        // Random bottom nav menu
        randomBottomNavMenu.setOnItemSelectedListener { item -> onNavigationItemSelected(item) }
    }

    private fun initiateObservers() {
        sharedViewModel.easterEggActivated.observe(this) { easterEggActivated ->
            shouldActivateEasterEgg(easterEggActivated)
        }
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

    private fun disableObservers() {
        sharedViewModel.easterEggActivated.removeObserver {}
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        setupNavigationAndStatusBar(applicationContext, window)

        sharedViewModel.checkEasterEgg(applicationContext)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            // Drawer
            // Apps
            R.id.drawer_click_counter -> switchFragment(
                homeFragmentContainer.id, ClickCounterFragment(applicationContext), CLICK_COUNTER
            )

            R.id.drawer_money_converter -> switchFragment(
                homeFragmentContainer.id,
                MoneyConverterFragment(applicationContext),
                MONEY_CONVERTER
            )

            R.id.drawer_body_mass_index -> switchFragment(
                homeFragmentContainer.id, BodyMassIndexFragment(applicationContext), BODY_MASS_INDEX
            )

            R.id.drawer_dices -> switchFragment(
                homeFragmentContainer.id, DicesFragment(applicationContext), DICES
            )

            R.id.drawer_random_letter -> {
                switchFragment(
                    homeFragmentContainer.id,
                    RandomLetterFragment(applicationContext),
                    RANDOM_LETTER
                )
                checkRandomLetterAtRandomBottomNavMenu()
            }

            R.id.drawer_nasa_picture_of_the_day -> {
                switchFragment(
                    homeFragmentContainer.id,
                    NasaPictureOfTheDayFragment(applicationContext),
                    NASA_PICTURE_OF_THE_DAY
                )
            }

            R.id.drawer_random_number -> {
                switchFragment(
                    homeFragmentContainer.id,
                    RandomNumberFragment(applicationContext),
                    RANDOM_NUMBER
                )
                checkRandomNumberAtRandomBottomNavMenu()
            }

            R.id.drawer_tip -> switchFragment(
                homeFragmentContainer.id, TipFragment(applicationContext), TIP
            )

            R.id.drawer_speed_test -> switchFragment(
                homeFragmentContainer.id, SpeedTestFragment(applicationContext), SPEED_TEST
            )

            R.id.drawer_random_quote -> switchFragment(
                homeFragmentContainer.id, RandomQuoteFragment(applicationContext), RANDOM_QUOTE
            )

            // Games
            R.id.drawer_coin_flipper -> switchFragment(
                homeFragmentContainer.id, CoinFlipperFragment(applicationContext), COIN_FLIPPER
            )

            R.id.drawer_tic_tac_toe -> switchFragment(
                homeFragmentContainer.id, TicTacToeFragment(applicationContext), TIC_TAC_TOE
            )

            R.id.drawer_rock_paper_scissors -> switchFragment(
                homeFragmentContainer.id,
                RockPaperScissorsFragment(applicationContext),
                ROCK_PAPER_SCISSORS
            )

            R.id.drawer_unscramble -> switchFragment(
                homeFragmentContainer.id, UnscrambleFragment(applicationContext), UNSCRAMBLE
            )

            // Drawer bottom
            R.id.drawer_bottom_settings -> openSettingsActivity()

            // Random bottom nav menu
            R.id.random_bottom_nav_menu_random_letter -> switchFragment(
                homeFragmentContainer.id, RandomLetterFragment(applicationContext), RANDOM_LETTER
            )

            R.id.random_bottom_nav_menu_random_number -> switchFragment(
                homeFragmentContainer.id, RandomNumberFragment(applicationContext), RANDOM_NUMBER
            )
        }

        activityMain.closeDrawer(START)
        return true
    }

    private fun checkRandomLetterAtRandomBottomNavMenu() {
        randomBottomNavMenuRandomLetter.isChecked = true
    }

    private fun checkRandomNumberAtRandomBottomNavMenu() {
        randomBottomNavMenuRandomnumber.isChecked = true
    }

    private fun switchFragment(fragmentId: Int, newFragment: Fragment, fragmentName: String) {
        try {
            val fragmentManager = supportFragmentManager.beginTransaction()

            if (supportFragmentManager.findFragmentById(fragmentId) == null) {
                fragmentManager.add(fragmentId, newFragment, fragmentName)
                logD { "Fragment added: $fragmentName" }
            } else if (supportFragmentManager.fragments[0].tag != fragmentName) {
                fragmentManager.replace(fragmentId, newFragment, fragmentName)
                logD { "Fragment replaced: $fragmentName" }
            }

            appBarTitle.text = sharedViewModel.getCurrentTitle(applicationContext, fragmentName)
            randomBottomNavMenu.isVisible = sharedViewModel.shouldShowRandomBottomNavMenu()

            fragmentManager.addToBackStack(null)
            fragmentManager.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun goHomeFragment() {
        switchFragment(homeFragmentContainer.id, HomeFragment(applicationContext), HOME)
        activityMain.closeDrawer(START)
    }

    private fun shouldActivateEasterEgg(isEasterEggActivated: Boolean) {
        val ticTacToeDrawerIcon = drawerItemsNavView.menu.findItem(R.id.drawer_tic_tac_toe)
        ticTacToeDrawerIcon.isVisible = isEasterEggActivated

        val moneyConverterDrawerIcon = drawerItemsNavView.menu.findItem(R.id.drawer_money_converter)
        moneyConverterDrawerIcon.isVisible = isEasterEggActivated

        val bodyMassIndexIcon = drawerItemsNavView.menu.findItem(R.id.drawer_body_mass_index)
        bodyMassIndexIcon.isVisible = isEasterEggActivated
    }

    private fun openSettingsActivity() = startActivity(Intent(this, SettingsActivity::class.java))
}