package com.mrozm.cicd.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import com.mrozm.cicd.domain.ExpressionPart.Op
import com.mrozm.cicd.domain.Operation.ADD
import com.mrozm.cicd.domain.Operation.SUBTRACT
import com.mrozm.cicd.domain.Operation.DIVIDE
import com.mrozm.cicd.domain.Operation.MULTIPLY
import com.mrozm.cicd.domain.ExpressionPart.Number
import com.mrozm.cicd.domain.ExpressionPart.Parentheses
import com.mrozm.cicd.domain.ParenthesesType.Closing
import com.mrozm.cicd.domain.ParenthesesType.Opening

class ExpressionParserTest {

    private lateinit var parser: ExpressionParser

    @Test
    fun `simple expression is properly parsed`() {
        parser = ExpressionParser("3+5-3x4/3")

        val parts = parser.parse()

        val expected = listOf(
            Number(3.0),
            Op(ADD),
            Number(5.0),
            Op(SUBTRACT),
            Number(3.0),
            Op(MULTIPLY),
            Number(4.0),
            Op(DIVIDE),
            Number(3.0),
        )

        assertThat(expected).isEqualTo(parts)
    }

    @Test
    fun `expression with parentheses is properly parsed`() {
        parser = ExpressionParser("3+5-(3x4)/3")

        val parts = parser.parse()

        val expected = listOf(
            Number(3.0),
            Op(ADD),
            Number(5.0),
            Op(SUBTRACT),
            Parentheses(Opening),
            Number(3.0),
            Op(MULTIPLY),
            Number(4.0),
            Parentheses(Closing),
            Op(DIVIDE),
            Number(3.0),
        )

        assertThat(expected).isEqualTo(parts)
    }

    @Test
    fun `expression with decimal is properly parsed`() {
        parser = ExpressionParser("3.0123+5.599-(3.14x4.09)/3.0004")

        val parts = parser.parse()

        val expected = listOf(
            Number(3.0123),
            Op(ADD),
            Number(5.599),
            Op(SUBTRACT),
            Parentheses(Opening),
            Number(3.14),
            Op(MULTIPLY),
            Number(4.09),
            Parentheses(Closing),
            Op(DIVIDE),
            Number(3.0004),
        )

        assertThat(expected).isEqualTo(parts)
    }
}
