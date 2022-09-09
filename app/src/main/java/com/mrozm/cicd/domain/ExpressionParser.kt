package com.mrozm.cicd.domain

import com.mrozm.cicd.domain.ExpressionPart.*
import com.mrozm.cicd.domain.ExpressionPart.Number
import com.mrozm.cicd.domain.ParenthesesType.Closing
import com.mrozm.cicd.domain.ParenthesesType.Opening

class ExpressionParser(
    private val calculation: String
) {
    fun parse(): List<ExpressionPart> {
        val result = mutableListOf<ExpressionPart>()

        var i = 0
        while (i < calculation.length) {
            val char = calculation[i]
            when {
                char in operationSymbols -> result.add(Op(operationFromSymbol(char)))
                char.isDigit() -> {
                    i = parseNumber(i, result)
                    continue
                }
                char in "()" -> parseParentheses(char, result)
            }
            i++
        }

        return result
    }

    private fun parseNumber(startIndex: Int, result: MutableList<ExpressionPart>): Int {
        var i = startIndex
        val numberAsString = buildString {
            while (i < calculation.length && calculation[i] in "0123456789.") {
                append(calculation[i])
                i++
            }
        }
        result.add(Number(numberAsString.toDouble()))
        return i
    }

    private fun parseParentheses(currentChar: Char, result: MutableList<ExpressionPart>) {
        result.add(
            Parentheses(
                type = when (currentChar) {
                    '(' -> Opening
                    ')' -> Closing
                    else -> throw  IllegalArgumentException("Invalid parentheses type.")
                }
            )
        )
    }
}
