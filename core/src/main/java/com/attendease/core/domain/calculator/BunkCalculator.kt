package com.attendease.core.domain.calculator

import com.attendease.core.domain.model.BunkResult
import com.attendease.core.domain.model.SubjectType
import kotlin.math.ceil
import kotlin.math.floor

object BunkCalculator {

    fun calculate(
        presentClasses: Int,
        absentClasses: Int,
        subjectType: SubjectType,
        targetPercentage: Double
    ): BunkResult {
        val weight = subjectType.unitWeight
        val presentUnits = presentClasses * weight
        val absentUnits = absentClasses * weight
        val totalUnits = presentUnits + absentUnits

        if (totalUnits == 0) {
            return BunkResult(
                currentPercentage = 0.0,
                safeBunksRemaining = 0,
                classesNeededToRecover = 0
            )
        }

        val currentPercentage = (presentUnits.toDouble() / totalUnits.toDouble()) * 100.0

        var safeBunks = 0
        if (currentPercentage >= targetPercentage && targetPercentage > 0.0) {
            val targetFraction = targetPercentage / 100.0
            val maxTotalUnitsAllowed = presentUnits / targetFraction
            val additionalUnitsAllowed = maxTotalUnitsAllowed - totalUnits
            safeBunks = floor(additionalUnitsAllowed / weight).toInt()
            if (safeBunks < 0) safeBunks = 0
        }

        var classesNeeded = 0
        if (currentPercentage < targetPercentage) {
            if (targetPercentage >= 100.0) {
                // Impossible to reach 100% if currently below 100%
                classesNeeded = Int.MAX_VALUE
            } else {
                val targetFraction = targetPercentage / 100.0
                val requiredAdditionalUnits = (targetFraction * totalUnits - presentUnits) / (1.0 - targetFraction)
                classesNeeded = ceil(requiredAdditionalUnits / weight).toInt()
                if (classesNeeded < 0) classesNeeded = 0
            }
        }

        return BunkResult(
            currentPercentage = currentPercentage,
            safeBunksRemaining = safeBunks,
            classesNeededToRecover = classesNeeded
        )
    }
}
