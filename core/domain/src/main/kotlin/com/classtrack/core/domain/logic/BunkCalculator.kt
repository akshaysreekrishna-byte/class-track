package com.classtrack.core.domain.logic

import kotlin.math.roundToInt

object BunkCalculator {
    fun calculatePercentage(presentCount: Int, totalCount: Int): Float {
        if (totalCount == 0) return 0f
        val raw = (presentCount.toFloat() / totalCount.toFloat()) * 100f
        return (raw * 10f).roundToInt() / 10f
    }

    fun calculateSafeBunks(presentCount: Int, totalCount: Int, targetPercentage: Float): Int {
        if (totalCount == 0) return 0
        val currentPercentage = calculatePercentage(presentCount, totalCount)
        if (currentPercentage < targetPercentage) return 0
        
        var safeBunks = 0
        var simTotal = totalCount
        while (true) {
            simTotal++
            val simPercentage = calculatePercentage(presentCount, simTotal)
            if (simPercentage >= targetPercentage) {
                safeBunks++
            } else {
                break
            }
        }
        return safeBunks
    }

    fun calculateRequiredClasses(presentCount: Int, totalCount: Int, targetPercentage: Float): Int {
        if (totalCount == 0) return 0
        val currentPercentage = calculatePercentage(presentCount, totalCount)
        if (currentPercentage >= targetPercentage) return 0

        var required = 0
        var simPresent = presentCount
        var simTotal = totalCount
        while (true) {
            simPresent++
            simTotal++
            required++
            val simPercentage = calculatePercentage(simPresent, simTotal)
            if (simPercentage >= targetPercentage) {
                break
            }
        }
        return required
    }
}
