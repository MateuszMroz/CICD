package com.mrozm.cicd.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mrozm.cicd.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CalculatorScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule(MainActivity::class.java)

    private lateinit var viewModel: CalculatorViewModel

    @Before
    fun setUp() {
        viewModel = CalculatorViewModel()
    }

    @Test
    fun enterExpression_correctResultDisplayed() {
        with(composeRule) {
            onNodeWithText("1").performClick()
            onNodeWithText("+").performClick()
            onNodeWithText("2").performClick()
            onNodeWithText("x").performClick()
            onNodeWithText("3").performClick()
            onNodeWithText("-").performClick()
            onNodeWithText("5").performClick()
            onNodeWithText("=").performClick()

            onNodeWithText("2.0").assertIsDisplayed()
        }
    }

}
