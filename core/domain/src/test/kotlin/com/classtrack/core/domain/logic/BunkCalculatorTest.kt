package com.classtrack.core.domain.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BunkCalculatorTest {

    @Test
    fun `given_totalClassesIs0_when_calculating_then_returnZeroes`() {
        assertEquals(0f, BunkCalculator.calculatePercentage(0, 0))
        assertEquals(0, BunkCalculator.calculateSafeBunks(0, 0, 75f))
        assertEquals(0, BunkCalculator.calculateRequiredClasses(0, 0, 75f))
    }

    @Test
    fun `given_percentageExactlyTarget_when_calculating_then_returnZeroActions`() {
        assertEquals(75.0f, BunkCalculator.calculatePercentage(3, 4))
        assertEquals(0, BunkCalculator.calculateSafeBunks(3, 4, 75.0f))
        assertEquals(0, BunkCalculator.calculateRequiredClasses(3, 4, 75.0f))
    }

    @Test
    fun `given_percentageWayBelowThreshold_when_calculating_then_safeBunksIsZeroNotNegative`() {
        assertEquals(25.0f, BunkCalculator.calculatePercentage(1, 4))
        assertEquals(0, BunkCalculator.calculateSafeBunks(1, 4, 75.0f))
    }
}
