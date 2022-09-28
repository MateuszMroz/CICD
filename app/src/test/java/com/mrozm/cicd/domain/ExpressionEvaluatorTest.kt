package com.mrozm.cicd.domain

import com.google.common.truth.Truth.assertThat
import com.mrozm.cicd.domain.ExpressionPart.Number
import com.mrozm.cicd.domain.ExpressionPart.Parentheses
import com.mrozm.cicd.domain.ExpressionPart.Op
import com.mrozm.cicd.domain.Operation.*
import com.mrozm.cicd.domain.ParenthesesType.*
import org.junit.Test

class ExpressionEvaluatorTest {

    private lateinit var evaluator: ExpressionEvaluator

    @Test
    fun `simple expression properly evaluated`() {
        evaluator = ExpressionEvaluator(
            listOf(
                Number(4.0),
                Op(ADD),
                Number(5.0),
                Op(SUBTRACT),
                Number(3.0),
                Op(MULTIPLY),
                Number(5.0),
                Op(DIVIDE),
                Number(3.0),
            )
        )

        assertThat(evaluator.evaluate()).isEqualTo(4)
    }

    @Test
    fun `expression with decimal properly evaluated`() {
        evaluator = ExpressionEvaluator(
            listOf(
                Number(4.5),
                Op(ADD),
                Number(5.5),
                Op(SUBTRACT),
                Number(3.5),
                Op(MULTIPLY),
                Number(5.5),
                Op(DIVIDE),
                Number(3.5),
            )
        )

        assertThat(evaluator.evaluate()).isEqualTo(4.5)
    }

    @Test
    fun `expression with parentheses properly evaluated`() {
        evaluator = ExpressionEvaluator(
            listOf(
                Number(4.0),
                Op(ADD),
                Parentheses(Opening),
                Number(5.0),
                Op(SUBTRACT),
                Number(3.0),
                Parentheses(Closing),
                Op(MULTIPLY),
                Number(5.0),
                Op(DIVIDE),
                Number(4.0),
            )
        )

        assertThat(evaluator.evaluate()).isEqualTo(6.5)
    }
}
