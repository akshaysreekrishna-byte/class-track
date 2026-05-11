# Implementation Plan - Phase 1, Step 2

Define the core Domain Entities in the `core:domain` module. These entities will be pure Kotlin data classes, free of any framework-specific annotations, as per the "Layer Separation" rule in `AGENTS.md`.

## Proposed Entities

### 1. `Subject`
Represents a course or subject the student is enrolled in.
- `id: String` (UUID)
- `name: String`
- `type: SubjectType` (Theory or Lab)
- `minAttendancePercentage: Float` (Target threshold)
- `geofenceConfig: GeofenceConfig?` (Optional location settings)

### 2. `AttendanceRecord`
Represents a single instance of attendance or absence.
- `id: String`
- `subjectId: String`
- `timestamp: Long` (Epoch milliseconds)
- `status: AttendanceStatus` (Present, Absent, Cancelled, Holiday)
- `isAutoMarked: Boolean` (Whether marked via geofence)

### 3. `GeofenceConfig`
Configuration for background attendance detection.
- `latitude: Double`
- `longitude: Double`
- `radiusMeters: Float`

### 4. Supporting Enums
- `SubjectType`: `THEORY`, `LAB`
- `AttendanceStatus`: `PRESENT`, `ABSENT`, `CANCELLED`, `HOLIDAY`

## File Structure
All files will be located in `core/domain/src/main/kotlin/com/classtrack/core/domain/model/`:
- `Subject.kt`
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
