package com.classtrack.core.data.local

import androidx.room.TypeConverter
import com.classtrack.core.domain.model.AttendanceStatus
import com.classtrack.core.domain.model.SubjectType

class Converters {

    @TypeConverter
    fun fromSubjectType(value: SubjectType): String {
        return value.name
    }

    @TypeConverter
    fun toSubjectType(value: String): SubjectType {
        return try {
            SubjectType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            SubjectType.THEORY // Safe fallback
        }
    }

    @TypeConverter
    fun fromAttendanceStatus(value: AttendanceStatus): String {
        return value.name
    }

    @TypeConverter
    fun toAttendanceStatus(value: String): AttendanceStatus {
        return try {
            AttendanceStatus.valueOf(value)
        } catch (e: IllegalArgumentException) {
            AttendanceStatus.ABSENT // Safe fallback
        }
    }
}
