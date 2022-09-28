package com.mrozm.cicd.domain

import com.mrozm.cicd.domain.CalculatorAction.*
import com.mrozm.cicd.domain.CalculatorAction.Number
import com.mrozm.cicd.domain.Operation.ADD
import com.mrozm.cicd.domain.Operation.SUBTRACT

class ExpressionWriter {

    var expression = ""

    fun processAction(action: CalculatorAction) {
        when (action) {
            Calculate -> {
                val parser = ExpressionParser(prepareForCalculation())
                val evaluator = ExpressionEvaluator(parser.parse())

                expression = evaluator.evaluate().toString()
            }
            Clear -> expression = ""
            Decimal -> {
                if (canEnterDecimal()) {
                    expression += "."
                }
            }
            Delete -> expression.dropLast(1)
            is Op -> {
                if (canEnterOperation(action.operation)) {
                    expression += action.operation.symbol
                }
            }
            Parentheses -> {
                processParentheses()
            }
            is Number -> {
                expression += action.number
            }
        }
    }

    private fun prepareForCalculation(): String {
        val newExpression = expression.takeLastWhile {
            it in "$operationSymbols(."
        }
        if (newExpression.isEmpty()) expression = "0"
        return newExpression
    }

    private fun processParentheses() {
        val openingCount = expression.count { it == '(' }
        val closingCount = expression.count { it == ')' }

        expression += when {
            expression.isEmpty() || expression.last() in "$operationSymbols(" -> "("
            expression.last() in "0123456789)" && openingCount == closingCount -> return
            else -> ")"
        }
    }

    private fun canEnterDecimal(): Boolean {
        if (expression.isEmpty() || expression.last() in "$operationSymbols.()") return false
        return !expression.takeLastWhile {
            it in "0123456789."
        }.contains(".")
    }

    private fun canEnterOperation(operation: Operation): Boolean {
        if (operation in listOf(ADD, SUBTRACT)) {
            return expression.isEmpty() || expression.last() in "$operationSymbols()0123456789"
        }
        return expression.isNotEmpty() || expression.last() in "0123456789)"
    }
}
