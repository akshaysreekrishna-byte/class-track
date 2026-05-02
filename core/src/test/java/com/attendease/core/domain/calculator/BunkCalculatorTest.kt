package com.attendease.core.domain.calculator

import com.attendease.core.domain.model.SubjectType
import org.junit.Assert.assertEquals
import org.junit.Test

class BunkCalculatorTest {

    @Test
    fun `0 percent attendance returns safe bunks 0 and computes classes needed`() {
        val result = BunkCalculator.calculate(
            presentClasses = 0,
            absentClasses = 10,
            subjectType = SubjectType.THEORY,
            targetPercentage = 75.0
        )
        assertEquals(0.0, result.currentPercentage, 0.01)
        assertEquals(0, result.safeBunksRemaining)
        assertEquals(30, result.classesNeededToRecover)
    }

    @Test
    fun `100 percent attendance returns correct safe bunks`() {
        val result = BunkCalculator.calculate(
            presentClasses = 10,
            absentClasses = 0,
            subjectType = SubjectType.THEORY,
            targetPercentage = 75.0
        )
        assertEquals(100.0, result.currentPercentage, 0.01)
        assertEquals(3, result.safeBunksRemaining)
        assertEquals(0, result.classesNeededToRecover)
    }

    @Test
    fun `exactly 75 percent attendance returns 0 safe bunks and 0 needed`() {
        val result = BunkCalculator.calculate(
            presentClasses = 75,
            absentClasses = 25,
            subjectType = SubjectType.THEORY,
            targetPercentage = 75.0
        )
        assertEquals(75.0, result.currentPercentage, 0.01)
        assertEquals(0, result.safeBunksRemaining)
        assertEquals(0, result.classesNeededToRecover)
    }

    @Test
    fun `lab weights as 2 units compared to theory`() {
        val theoryResult = BunkCalculator.calculate(
            presentClasses = 10,
            absentClasses = 0,
            subjectType = SubjectType.THEORY,
            targetPercentage = 75.0
        )
        val labResult = BunkCalculator.calculate(
            presentClasses = 5,
            absentClasses = 0,
            subjectType = SubjectType.LAB,
            targetPercentage = 75.0
        )
        assertEquals(theoryResult.currentPercentage, labResult.currentPercentage, 0.01)
        assertEquals(3, theoryResult.safeBunksRemaining)
        assertEquals(1, labResult.safeBunksRemaining) // Floor(3.33 / 2) = 1
    }
}
