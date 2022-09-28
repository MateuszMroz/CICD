package com.mrozm.cicd.domain

import com.mrozm.cicd.domain.ExpressionPart.*
import com.mrozm.cicd.domain.ExpressionPart.Number
import com.mrozm.cicd.domain.Operation.*
import com.mrozm.cicd.domain.ParenthesesType.*

/**
 * Uses the following grammar
 * expression :	term | term + term | term − term
 * term :		factor | factor * factor | factor / factor | factor % factor
 * factor : 	number | ( expression ) | + factor | − factor
 */
class ExpressionEvaluator(
    private val expression: List<ExpressionPart>
) {
    fun evaluate(): Double {
        return evalExpression(expression).value
    }

    private fun evalExpression(expression: List<ExpressionPart>): ExpressionResult {
        val result = evalTerm(expression)
        var remaining = result.remainingExpression
        var sum = result.value
        while (true) {
            when(remaining.firstOrNull()) {
                Op(ADD) -> {
                    val term = evalTerm(remaining.drop(1))
                    sum += term.value
                    remaining = term.remainingExpression
                }
                Op(SUBTRACT) -> {
                    val term = evalTerm(remaining.drop(1))
                    sum -= term.value
                    remaining = term.remainingExpression
                }
                else -> return ExpressionResult(remaining, sum)
            }
        }
    }

    private fun evalTerm(expression: List<ExpressionPart>): ExpressionResult {
        val result = evalFactor(expression)
        var remaining = result.remainingExpression
        var sum = result.value
        while (true) {
            when(remaining.firstOrNull()) {
                Op(MULTIPLY) -> {
                    val factor = evalFactor(remaining.drop(1))
                    sum *= factor.value
                    remaining = factor.remainingExpression
                }
                Op(DIVIDE) -> {
                    val factor = evalFactor(remaining.drop(1))
                    sum /= factor.value
                    remaining = factor.remainingExpression
                }
                Op(PERCENT) -> {
                    val factor = evalFactor(remaining.drop(1))
                    sum *= (factor.value / 100.0)
                    remaining = factor.remainingExpression
                }
                else -> return ExpressionResult(remaining, sum)
            }
        }
    }

    // A factor is either a number or an expression in parentheses
    // e.g. 5.0, -7.5, -(3+4*5)
    // But NOT something like 3 * 5, 4 + 5
    private fun evalFactor(expression: List<ExpressionPart>): ExpressionResult {
        return when (val part = expression.firstOrNull()) {
            Op(ADD) -> {
                evalFactor(expression.drop(1))
            }
            Op(SUBTRACT) -> {
                evalFactor(expression.drop(1)).run {
                    ExpressionResult(remainingExpression, -value)
                }
            }
            Parentheses(Opening) -> {
                evalExpression(expression.drop(1)).run {
                    ExpressionResult(remainingExpression.drop(1), value)
                }
            }
            Op(PERCENT) -> evalTerm(expression.drop(1))
            is Number -> ExpressionResult(
                expression.drop(1),
                part.number
            )
            else -> {
                throw IllegalArgumentException("Invalid part")
            }
        }
    }

    data class ExpressionResult(
        val remainingExpression: List<ExpressionPart>,
        val value: Double
    )
}
