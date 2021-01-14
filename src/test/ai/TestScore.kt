package test.ai

import aibrain.Score
import org.junit.Test

class TestScore {

    @Test
    fun testScoreSubtractionToEmpty(){
        val score1 = Score()
        val score2 = Score()

        score1.add("Pop Change", {value -> ""}, 100.0)
        score2.add("Pop Change", {value -> ""}, 100.0)

        val subtraction = score1.minus(score2)
        assert(subtraction.components().isEmpty())
    }

    @Test
    fun testScoreSubtractionToNegative(){
        val score1 = Score()
        val score2 = Score()

        score1.add("Pop Change", {value -> ""}, 2.0)
        score2.add("Pop Change", {value -> ""}, 100.0)

        val subtraction = score1.minus(score2)
        assert(subtraction.components().first().value == -98.0)
    }
}