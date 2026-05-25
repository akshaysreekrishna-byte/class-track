package com.classtrack.core.domain.usecase

import org.junit.jupiter.api.Test

class MarkAttendanceUseCaseTest {
    @Test
    fun `insert_autoRecord_when_manualRecordExists_should_ignoreInsertion`() {
        // TODO: Implement with Fakes
    }

    @Test
    fun `insert_manualRecord_when_autoRecordExists_should_overwriteWithManual`() {
        // TODO: Implement with Fakes
    }

    @Test
    fun `insert_record_with_futureTimestamp_should_throwIllegalArgumentException`() {
        // TODO: Implement with Fakes
    }
}
