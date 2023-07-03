package com.wrdelmanto.papps.apps.clickCounter

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wrdelmanto.papps.MainActivity
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClickCounterTests {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun beginTests() {
        openClickCounter()

//        click("click_counter_addition_button")
//        check("click_counter_addition_button", "1")
    }

    private fun openClickCounter() {
//        click("app_bar_main_drawer_icon")
//        click("drawer_click_counter")
    }

    private fun click(referenceId: String) {
        click(referenceId)
    }

    private fun check(referenceId: String, expectedResult: String) {
        assertEquals(expectedResult, referenceId)
    }

    private fun checkHighScore() {}
}