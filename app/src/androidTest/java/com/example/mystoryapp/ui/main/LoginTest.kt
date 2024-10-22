package com.example.mystoryapp.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.mystoryapp.R
import com.example.mystoryapp.ui.login.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class LoginTest {

    private val validEmailSample = "wibowo@gmail.com"
    private val validPasswordSample = "wibowo@gmail.com"
    private val invalidEmailSample = "satsat@gmail.com"
    private val invalidPasswordSample = "satsat@gmail.com"

    @Before
    fun setup () {
        ActivityScenario.launch(LoginActivity::class.java)
        IdlingRegistry.getInstance().register(IdlingResource.idlingResource)
        disableSystemAnimations()
    }

    @Test
    fun testValidLoginLogout() {
        onView(withId(R.id.ed_login_email)).perform(typeText(validEmailSample))
        closeSoftKeyboard()
        onView(withId(R.id.ed_login_password)).perform(typeText(validPasswordSample))
        closeSoftKeyboard()
        onView(withId(R.id.loginButton)).perform(click())
        onView(withText(R.string.positive_reply)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())
        Espresso.openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.logout)).check(matches(isDisplayed())).perform(click())
    }

    @Test
    fun testInvalidLogin() {
        onView(withId(R.id.ed_login_email)).perform(typeText(invalidEmailSample))
        closeSoftKeyboard()
        onView(withId(R.id.ed_login_password)).perform(typeText(invalidPasswordSample))
        closeSoftKeyboard()
        onView(withId(R.id.loginButton)).perform(click())
    }


    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(IdlingResource.idlingResource)
        enableSystemAnimations()
    }

    private fun enableSystemAnimations() {
        try {
            val instrumentation = getInstrumentation()
            val uiAutomation = instrumentation.uiAutomation
            uiAutomation.executeShellCommand("settings put global window_animation_scale 1.0")
            uiAutomation.executeShellCommand("settings put global transition_animation_scale 1.0")
            uiAutomation.executeShellCommand("settings put global animator_duration_scale 1.0")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun disableSystemAnimations() {
        try {
            val instrumentation = getInstrumentation()
            val uiAutomation = instrumentation.uiAutomation
            uiAutomation.executeShellCommand("settings put global window_animation_scale 0.0")
            uiAutomation.executeShellCommand("settings put global transition_animation_scale 0.0")
            uiAutomation.executeShellCommand("settings put global animator_duration_scale 0.0")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

object IdlingResource {
    private const val RESOURCE = "GLOBAL"
    val idlingResource = CountingIdlingResource(RESOURCE)
}