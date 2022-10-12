package com.mrozm.cicd.domain

import com.google.common.truth.Truth.assertThat
import com.mrozm.cicd.domain.CalculatorAction.*
import com.mrozm.cicd.domain.CalculatorAction.Number
import com.mrozm.cicd.domain.Operation.ADD
import org.junit.Before
import org.junit.Test

class ExpressionWriterTest {
    private lateinit var writter: ExpressionWriter

    @Before
    fun setup() {
        writter = ExpressionWriter()
    }

    @Test
    fun `initial parentheses parsed`() {
        writter.processAction(Parentheses)
        writter.processAction(Number(5))
        writter.processAction(Op(ADD))
        writter.processAction(Number(4))
        writter.processAction(Parentheses)

        assertThat(writter.expression).isEqualTo("(5+4)")
    }

    @Test
    fun `closing parentheses at the start not parsed`() {
        writter.processAction(Parentheses)
        writter.processAction(Parentheses)

        assertThat(writter.expression).isEqualTo("((")
    }

    @Test
    fun `parentheses around a number are parsed`() {
        writter.processAction(Parentheses)
        writter.processAction(Number(6))
        writter.processAction(Parentheses)

        assertThat(writter.expression).isEqualTo("(6)")
    }
}
