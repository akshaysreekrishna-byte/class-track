# Implementation Plan - Phase 1, Step 2

Define the core Domain Entities in the `core:domain` module. These entities will be pure Kotlin data classes, free of any framework-specific annotations, as per the "Layer Separation" rule in `AGENTS.md`.

## Proposed Entities

### 1. `AcademicTerm`
Keeps data separated by semester or school year to prevent history bleeding and performance issues.
- `id: String` (UUID)
- `name: String` (e.g., "Fall 2026", "Semester 3")
- `startDate: Long`
- `endDate: Long`
- `isCurrent: Boolean`

### 2. `Subject`
Represents a course or subject the student is enrolled in during a specific term.
- `id: String` (UUID)
- `termId: String` (FK to AcademicTerm)
- `name: String`
- `type: SubjectType` (Theory or Lab)
- `minAttendancePercentage: Float` (Target threshold)
- `geofenceConfig: GeofenceConfig?` (Optional location settings)

### 3. `ScheduleSlot`
Defines the recurring weekly timetable. Crucial for telling the background worker exactly when to check the geofence to prevent squatting.
- `id: String`
- `subjectId: String` (FK to Subject)
- `dayOfWeek: Int` (1 = Monday, 7 = Sunday)
- `startTime: String` (e.g., "09:00")
- `endTime: String` (e.g., "09:50")
- `roomNumber: String?` (Optional metadata)

### 4. `AttendanceRecord`
Represents a single instance of attendance or absence. Note: "Cancelled" or "Holiday" statuses should be ignored in percentage calculation logic.
- `id: String`
- `subjectId: String`
- `scheduleSlotId: String?` (Optional link to the slot it belongs to, helpful for analytics)
- `timestamp: Long` (Epoch milliseconds)
- `status: AttendanceStatus` (Present, Absent, Cancelled, Holiday)
- `isAutoMarked: Boolean` (Whether marked via geofence)
- `notes: String?` (e.g., "Geofence failed - manual override" or "Sick note submitted")

### 5. `GeofenceConfig`
Configuration for background attendance detection.
- `latitude: Double`
- `longitude: Double`
- `radiusMeters: Float`

### 6. Supporting Enums
- `SubjectType`: `THEORY`, `LAB`
- `AttendanceStatus`: `PRESENT`, `ABSENT`, `CANCELLED`, `HOLIDAY`

## File Structure
All files will be located in `core/domain/src/main/kotlin/com/classtrack/core/domain/model/`:
- `AcademicTerm.kt`
- `Subject.kt`
- `ScheduleSlot.kt`
- `AttendanceRecord.kt`
- `GeofenceConfig.kt`
- `AttendanceStatus.kt`
- `SubjectType.kt`

## Rules Compliance
- **Rule 1.2**: Pure Kotlin, no Android imports.
- **Rule 1.5**: Entity names are nouns.
- **Rule 7.1**: All files will be well under the 50-line limit.

## Questions for User
- Should `GeofenceConfig` be an independent entity with its own ID, or is it strictly tied 1:1 with a `Subject`? (Plan assumes 1:1 tied to Subject).
- Do you want to include "Credits" in the `Subject` entity for weighted GPA/Attendance calculations later?
