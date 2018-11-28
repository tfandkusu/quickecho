package jp.bellware.echo


import androidx.test.espresso.Espresso.onView
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Test
import org.junit.runner.RunWith

import jp.bellware.echo.main.MainActivity
import org.junit.Rule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.runner.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
@LargeTest
class UITester {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)


    @Test
    fun test() {
        onView(withId(R.id.record)).perform(click())
        onView(withId(R.id.play)).check(matches(isDisplayed()))
        onView(withId(R.id.delete)).perform(click())
        onView(withId(R.id.record)).perform(click())
        onView(withId(R.id.play)).check(matches(isDisplayed()))
        onView(withId(R.id.delete)).perform(click())
        onView(withId(R.id.setting)).perform(click())
        onView(withText("アプリについて")).perform(click())
        pressBack()
        pressBack()
    }


}