# AGENTS.md — AttendEase Project Rulebook
> **Version:** 1.1.0 | **Status:** Authoritative Source of Truth
> **Scope:** Every AI agent, contributor, or automated tool working on this codebase MUST treat this document as law. No exceptions without an explicit, versioned amendment to this file.

---

## Table of Contents

1. [Project Identity](#1-project-identity)
2. [Technical Stack & Constraints](#2-technical-stack--constraints)
3. [Architectural Commandments (Clean Architecture)](#3-architectural-commandments-clean-architecture)
4. [Module & Package Structure](#4-module--package-structure)
5. [FOSS & Privacy Guardrails](#5-foss--privacy-guardrails)
6. [Feature-Specific Logic Rules](#6-feature-specific-logic-rules)
7. [Coding Style & Quality Standards](#7-coding-style--quality-standards)
8. [Testing Mandates](#8-testing-mandates)
9. [Dependency Management](#9-dependency-management)
10. [Contribution & Review Protocol](#10-contribution--review-protocol)

---

## 1. Project Identity

| Field          | Value                                                                 |
|----------------|-----------------------------------------------------------------------|
| **App Name**   | class tracker                                                         |
| **Purpose**    | Student attendance management with bunk calculation and geofencing    |
| **Philosophy** | FOSS-first, Privacy-by-design, Local-first                           |
| **License**    | GPL-3.0 (all contributions must be compatible)                       |
| **Min SDK**    | API 26 (Android 8.0)                                                 |
| **Target SDK** | Latest stable Android SDK                                             |

### 1.1 Non-Negotiable Project Principles

- **P-1 FOSS First:** No proprietary SDK, library, or service may be introduced without a formal amendment to this rulebook.
- **P-2 Privacy by Default:** No user data leaves the device unless explicitly initiated by the user (e.g., manual export).
- **P-3 Local First:** All business logic, calculations, and state live on-device. The app must be fully functional with no network connection.
- **P-4 Offline Capable:** Every core feature (marking attendance, viewing calendar, bunk calculation) must work in airplane mode.

---

## 2. Technical Stack & Constraints

This section is a locked specification. Substitutions require a rulebook amendment.

### 2.1 Language

| Rule | Specification |
|------|---------------|
| **Primary Language** | Kotlin **2.0+** strictly. No Java source files in new code. |
| **Kotlin Features** | Coroutines + Flow for all async work. No `RxJava`. |
| **Null Safety** | Leverage Kotlin's null-safety fully. `!!` (non-null assertion) is **FORBIDDEN** except in test code with a mandatory comment explaining why. |
| **Compiler** | KSP (Kotlin Symbol Processing) for all annotation processors. `kapt` is **FORBIDDEN**. |

### 2.2 UI Layer

| Rule | Specification |
|------|---------------|
| **UI Toolkit** | Jetpack Compose **only**. No XML layouts, no `View`-based widgets in new screens. |
| **Design System** | Material 3 (Material You). Use `MaterialTheme` tokens exclusively for colors, typography, and shapes. No hardcoded color values (e.g., `Color(0xFF...)`) outside the theme file. |
| **Dynamic Color** | Must be supported on Android 12+ via `dynamicColorScheme`. Provide a static fallback palette for older devices. |
| **State Hoisting** | All stateful composables must hoist state upward. Composables must be as stateless as possible. |

### 2.3 Navigation

| Rule | Specification |
|------|---------------|
| **Library** | Jetpack Compose Navigation (`androidx.navigation:navigation-compose`). |
| **Type Safety** | **MANDATORY.** All navigation routes must be defined as `@Serializable` data classes or objects. String-based routes are **FORBIDDEN**. |
| **Nav Graph** | A single, top-level `NavHost` per navigation scope. Nested graphs are permitted for feature modules. |
| **Back Stack** | Use `popUpTo` and `launchSingleTop` correctly to prevent back-stack accumulation on bottom navigation items. |

### 2.4 Mapping

| Rule | Specification |
|------|---------------|
| **Library** | `OSMDroid` **only**. |
| **FORBIDDEN** | Google Maps SDK, Mapbox (proprietary tier), HERE Maps, or any mapping SDK requiring a proprietary API key. |
| **Tile Source** | Default to OpenStreetMap tiles. Allow users to choose alternative FOSS tile providers. Tile cache must be stored on-device. |
| **Permissions** | Location permissions must be requested contextually with a clear rationale dialog before any map interaction. |

### 2.5 Background Processing

| Rule | Specification |
|------|---------------|
| **Library** | `WorkManager` **only** for all deferred and periodic background tasks. |
| **FORBIDDEN** | `AlarmManager` for repeating tasks, foreground services for polling (use only if truly streaming data), `JobScheduler` directly. |
| **Constraints** | All Workers must define appropriate `Constraints` (e.g., network type if needed). |
| **Idempotency** | Every `Worker` class **MUST** be idempotent. Running a Worker twice with the same inputs must produce the same result. |

### 2.6 Database & Persistence

| Rule | Specification |
|------|---------------|
| **Library** | `Room` for all structured data persistence. |
| **FORBIDDEN** | Direct SQLite usage, `SharedPreferences` for complex data (use `DataStore` instead). |
| **DataStore** | `Jetpack DataStore (Proto or Preferences)` for simple key-value settings (e.g., user preferences, theme). |
| **Migrations** | Every schema change **MUST** have an explicit, tested `Migration` object. `fallbackToDestructiveMigration()` is **FORBIDDEN** in production builds. |
| **Thread Safety** | All Room DAO functions must be `suspend` functions or return `Flow`. Blocking database calls on the main thread are **FORBIDDEN**. |

---

## 3. Architectural Commandments (Clean Architecture)

This is the most critical section. Violating these rules will result in rejected contributions.

### 3.1 The Golden Rule — Dependency Rule

> **Dependencies MUST only point inward.**
> `UI` → `Domain` ← `Data`

```
┌──────────────────────────────────────────────────┐
│                    UI Layer                      │
│  (Composables, ViewModels, UI State)             │
│                     │                            │
│                     │  depends on                │
│                     ▼                            │
│              Domain Layer                        │
│  (Use Cases, Repository Interfaces, Models)      │
│                     ▲                            │
│                     │  implements                │
│                     │                            │
│                   Data Layer                     │
│  (Room DAOs, DataStore, OSMDroid, WorkManager)   │
└──────────────────────────────────────────────────┘
```

**The Domain layer has ZERO dependencies on:**
- Android framework classes (`Context`, `Activity`, `Fragment`, etc.)
- Room, DataStore, WorkManager, OSMDroid, or any third-party library
- The `data` or `ui` modules/packages

**Enforcement:** If a `domain` class imports anything from `android.*`, `androidx.*`, or any third-party namespace, it is an architectural violation. Use plain Kotlin/Java types only in the Domain layer.

### 3.2 Layer Definitions & Responsibilities

#### 3.2.1 Domain Layer (`domain/`)

The heart of the application. Contains pure business logic only.

| Component | Rule |
|-----------|------|
| **Entities / Models** | Plain Kotlin data classes representing core business concepts (e.g., `AttendanceRecord`, `Subject`, `GeofenceZone`). No annotations from any framework. |
| **Repository Interfaces** | Define `interface` contracts (e.g., `AttendanceRepository`). The Domain layer owns the interface; the Data layer owns the implementation. |
| **Use Cases** | One public function per class (`operator fun invoke(...)`). Each Use Case encapsulates exactly **one** business operation. |
| **Domain Exceptions** | Define sealed classes for domain-level errors (e.g., `sealed class AttendanceError`). |

```kotlin
// ✅ CORRECT — Domain model. Pure Kotlin. Zero framework dependencies.
data class AttendanceRecord(
    val id: Long = 0,
    val subjectId: Long,
    val date: LocalDate,
    val status: AttendanceStatus,
    val isManualOverride: Boolean = false
)

enum class AttendanceStatus { PRESENT, ABSENT, CANCELLED, HOLIDAY }

// ✅ CORRECT — Repository interface in Domain layer.
interface AttendanceRepository {
    fun getRecordsForSubject(subjectId: Long): Flow<List<AttendanceRecord>>
    suspend fun markAttendance(record: AttendanceRecord): Result<AttendanceRecord>
    suspend fun getAttendanceSummary(subjectId: Long): Result<AttendanceSummary>
}
```

#### 3.2.2 Data Layer (`data/`)

Responsible for all data sourcing. Implements Domain interfaces.

| Component | Rule |
|-----------|------|
| **Repository Implementations** | Implement Domain interfaces. Coordinate between local (Room) and any future remote sources. |
| **Room DAOs** | Only `suspend` functions or `Flow`-returning functions. |
| **Room Entities** | Separate from Domain models. Use `@Entity` annotation. Must have explicit `@TypeConverters` for custom types. |
| **Mappers** | A dedicated `*Mapper.kt` file per entity to convert between `Room Entity ↔ Domain Model`. Mapping logic must not leak into ViewModels or Composables. |
| **DataStore** | All DataStore access must go through a dedicated `*DataSource` or `*Preferences` class. No direct DataStore access from Repository or ViewModel. |

```kotlin
// ✅ CORRECT — Room entity is separate from Domain model.
@Entity(tableName = "attendance_records")
data class AttendanceRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subjectId: Long,
    val date: String, // ISO-8601, converted via TypeConverter
    val statusOrdinal: Int,
    val isManualOverride: Boolean
)

// ✅ CORRECT — Mapper is a separate concern.
object AttendanceRecordMapper {
    fun AttendanceRecordEntity.toDomain(): AttendanceRecord = AttendanceRecord(
        id = id,
        subjectId = subjectId,
        date = LocalDate.parse(date),
        status = AttendanceStatus.entries[statusOrdinal],
        isManualOverride = isManualOverride
    )
    fun AttendanceRecord.toEntity(): AttendanceRecordEntity = AttendanceRecordEntity(...)
}
```

#### 3.2.3 UI Layer (`ui/`)

Responsible for rendering state and capturing user intent.

| Component | Rule |
|-----------|------|
| **ViewModels** | One ViewModel per screen/feature. Inject Use Cases, not Repositories directly. |
| **UI State** | A single `sealed class` or `data class` per screen representing the complete UI state. |
| **StateFlow** | Each ViewModel exposes **one** `StateFlow<ScreenState>`. Multiple `StateFlow` properties on a single ViewModel are **FORBIDDEN**. |
| **Events** | One-time side effects (navigation, snackbars) must use a `Channel<UiEvent>` exposed as `receiveAsFlow()`. |
| **Composables** | Must only call ViewModel functions or pass lambdas. No business logic inside a `@Composable` function. |

### 3.3 Unidirectional Data Flow (UDF) — Mandatory Pattern

```
User Action (Event)
       │
       ▼
  ViewModel.onEvent(event)
       │
       ▼
  Use Case(s) execute
       │
       ▼
  _uiState.update { ... }
       │
       ▼
  StateFlow<ScreenUiState> (observed by Composable)
       │
       ▼
  Composable re-renders
```

**Implementation Contract:**

```kotlin
// ✅ CORRECT — Single StateFlow pattern in ViewModel.

// 1. Define a complete UI state class.
data class AttendanceScreenUiState(
    val isLoading: Boolean = false,
    val subjects: List<SubjectUi> = emptyList(),
    val error: String? = null
)

// 2. Define a sealed class for one-time UI events.
sealed class AttendanceUiEvent {
    data class ShowSnackbar(val message: String) : AttendanceUiEvent()
    data class NavigateToDetail(val subjectId: Long) : AttendanceUiEvent()
}

// 3. ViewModel exposes one StateFlow and one events channel.
@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val getSubjectsUseCase: GetSubjectsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AttendanceScreenUiState())
    val uiState: StateFlow<AttendanceScreenUiState> = _uiState.asStateFlow()

    private val _uiEvents = Channel<AttendanceUiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onEvent(event: AttendanceScreenEvent) { /* handle events here */ }
}

// ❌ FORBIDDEN — Fragmented state properties.
class BadViewModel : ViewModel() {
    val isLoading = MutableStateFlow(false)   // VIOLATION
    val subjects = MutableStateFlow(emptyList<SubjectUi>()) // VIOLATION
    val error = MutableStateFlow<String?>(null) // VIOLATION
}
```

### 3.4 Dependency Injection

| Rule | Specification |
|------|---------------|
| **Framework** | Hilt (`com.google.dagger:hilt-android`). This is the sole DI framework. |
| **Scope** | Use the appropriate scope: `@Singleton` for repositories and data sources; `@ViewModelScoped` for use case instances if needed. |
| **Modules** | Group Hilt modules by layer: `DataModule`, `DomainModule` (if needed for bindings), `WorkerModule`. |
| **Constructor Injection** | Always prefer constructor injection. Field injection (`@Inject lateinit var`) is only permitted in Android framework classes that don't support constructor injection (e.g., `BroadcastReceiver`). |

---

## 4. Module & Package Structure

> **Amendment v1.1.0:** The feature directory has been re-aligned to match the app's actual 3-screen UI architecture. The former `feature/attendance/` and `feature/bunk/` modules have been **consolidated** into `feature/dashboard/`. `BunkCalculator` now lives in `core/domain/` as a shared domain component. There is no standalone `bunk` feature module.

### 4.1 The Three-Screen Architecture

AttendEase has exactly **three primary screens**. Every feature module maps 1-to-1 with a screen.

| Screen | Feature Module | Role |
|--------|---------------|------|
| **Dashboard** | `feature/dashboard/` | Command center — attendance KPIs + bunk analytics |
| **Calendar** | `feature/calendar/` | Monthly view, timetable grid, holiday management |
| **Subjects** | `feature/subjects/` | Subject CRUD, Theory vs Lab classification |

### 4.2 Full Directory Tree

```
com.attendease/
│
├── core/
│   │
│   ├── data/
│   │   ├── database/
│   │   │   ├── AppDatabase.kt
│   │   │   ├── dao/
│   │   │   │   ├── AttendanceDao.kt
│   │   │   │   ├── SubjectDao.kt
│   │   │   │   └── HolidayDao.kt
│   │   │   ├── entity/
│   │   │   │   ├── AttendanceRecordEntity.kt
│   │   │   │   ├── SubjectEntity.kt
│   │   │   │   └── HolidayEntity.kt
│   │   │   └── converter/
│   │   │       ├── DateConverter.kt
│   │   │       └── EnumConverters.kt
│   │   ├── datastore/
│   │   │   └── UserPreferencesDataSource.kt
│   │   ├── mapper/
│   │   │   ├── AttendanceRecordMapper.kt
│   │   │   ├── SubjectMapper.kt
│   │   │   └── HolidayMapper.kt
│   │   └── repository/
│   │       ├── AttendanceRepositoryImpl.kt
│   │       ├── SubjectRepositoryImpl.kt
│   │       └── HolidayRepositoryImpl.kt
│   │
│   ├── domain/
│   │   ├── model/
│   │   │   ├── AttendanceRecord.kt
│   │   │   ├── AttendanceSummary.kt    # Aggregated stats per subject
│   │   │   ├── BunkResult.kt           # Output model of BunkCalculator
│   │   │   ├── Subject.kt
│   │   │   ├── SubjectType.kt          # THEORY | LAB
│   │   │   ├── Holiday.kt
│   │   │   ├── GeofenceZone.kt
│   │   │   └── AttendanceStatus.kt     # PRESENT | ABSENT | CANCELLED | HOLIDAY
│   │   ├── repository/
│   │   │   ├── AttendanceRepository.kt
│   │   │   ├── SubjectRepository.kt
│   │   │   └── HolidayRepository.kt
│   │   ├── calculator/
│   │   │   └── BunkCalculator.kt       # Pure Kotlin object. Shared across all features.
│   │   ├── usecase/
│   │   │   ├── attendance/
│   │   │   │   ├── GetAttendanceSummaryUseCase.kt
│   │   │   │   ├── MarkAttendanceUseCase.kt
│   │   │   │   └── GetRecordsForDateUseCase.kt
│   │   │   ├── bunk/
│   │   │   │   └── CalculateBunkSafetyUseCase.kt  # Orchestrates BunkCalculator
│   │   │   ├── subject/
│   │   │   │   ├── GetAllSubjectsUseCase.kt
│   │   │   │   ├── AddSubjectUseCase.kt
│   │   │   │   └── DeleteSubjectUseCase.kt
│   │   │   └── holiday/
│   │   │       ├── AddHolidayUseCase.kt
│   │   │       └── AddBulkHolidaysUseCase.kt
│   │   └── error/
│   │       └── AttendanceError.kt      # Sealed domain error hierarchy
│   │
│   └── ui/
│       ├── theme/
│       │   ├── Theme.kt
│       │   ├── Color.kt
│       │   ├── Typography.kt
│       │   └── Shape.kt
│       └── component/
│           ├── AttendanceStatusChip.kt
│           ├── SubjectCard.kt
│           └── PercentageRing.kt       # Shared donut/ring progress indicator
│
├── feature/
│   │
│   ├── dashboard/                      # Screen 1: The Command Center
│   │   └── ui/
│   │       ├── DashboardScreen.kt
│   │       ├── DashboardViewModel.kt
│   │       ├── DashboardUiState.kt
│   │       └── component/
│   │           ├── AttendanceKpiCard.kt      # Shows current % per subject
│   │           ├── BunkSafetyCard.kt         # Shows BunkResult advice
│   │           └── OverallSummaryHeader.kt
│   │
│   ├── calendar/                       # Screen 2: Calendar + Timetable + Holidays
│   │   └── ui/
│   │       ├── CalendarScreen.kt
│   │       ├── CalendarViewModel.kt
│   │       ├── CalendarUiState.kt
│   │       └── component/
│   │           ├── MonthGrid.kt              # Monthly navigation + day cells
│   │           ├── CalendarDayCell.kt        # 4-color state cell (see §6.3)
│   │           ├── TimetableGrid.kt          # Daily class-slot grid
│   │           └── HolidayPickerDialog.kt    # Single + Bulk holiday input
│   │
│   └── subjects/                       # Screen 3: Subject Management
│       └── ui/
│           ├── SubjectsScreen.kt
│           ├── SubjectsViewModel.kt
│           ├── SubjectsUiState.kt
│           └── component/
│               ├── SubjectListItem.kt
│               └── AddEditSubjectSheet.kt    # Bottom sheet for add/edit
│
├── worker/
│   └── GeofenceCheckWorker.kt
│
└── di/
    ├── AppModule.kt
    ├── DataModule.kt
    └── WorkerModule.kt
```

### 4.3 Key Structural Rules

**Rule S-1 — No Feature-Level Data or Domain Packages.**
The three feature modules contain **only** `ui/` code (Composables, ViewModels, UiState, and feature-specific Composable components). All domain logic and data access lives in `core/`. Feature modules import from `core/domain/`; they never own repository interfaces or entities.

**Rule S-2 — `BunkCalculator` is a Shared Core Component.**
`BunkCalculator` lives at `core/domain/calculator/BunkCalculator.kt`. It is **not** owned by any single feature. The `DashboardViewModel` consumes it via `CalculateBunkSafetyUseCase`. Any future feature that needs bunk projection data must go through the same Use Case — it must never instantiate `BunkCalculator` directly in a ViewModel.

```
// ✅ CORRECT dependency path for the Dashboard
DashboardViewModel
  → CalculateBunkSafetyUseCase   (core/domain/usecase/bunk/)
    → BunkCalculator              (core/domain/calculator/)
    → AttendanceRepository        (core/domain/repository/)

// ❌ FORBIDDEN — ViewModel touching BunkCalculator directly
class DashboardViewModel : ViewModel() {
    private val bunkCalculator = BunkCalculator  // VIOLATION: skip the Use Case
}
```

**Rule S-3 — No Cross-Feature Imports.**
`feature/dashboard/` must never import from `feature/calendar/` or `feature/subjects/`, and vice versa. Shared UI components live in `core/ui/component/`.

**Rule S-4 — Navigation is Owned by the App Shell.**
The `NavHost`, bottom navigation bar, and route definitions live in the app-level shell (e.g., `MainActivity` / `AppNavigation.kt`), not inside any feature module.

### 4.4 Naming Conventions for Files

| Type | Pattern | Example |
|------|---------|---------|
| Screen Composable | `[Feature]Screen.kt` | `DashboardScreen.kt` |
| ViewModel | `[Feature]ViewModel.kt` | `CalendarViewModel.kt` |
| UI State | `[Feature]UiState.kt` | `DashboardUiState.kt` |
| Use Case | `[Verb][Noun]UseCase.kt` | `CalculateBunkSafetyUseCase.kt` |
| Calculator / Pure Logic | `[Noun]Calculator.kt` | `BunkCalculator.kt` |
| Repository Interface | `[Noun]Repository.kt` | `AttendanceRepository.kt` |
| Repository Impl | `[Noun]RepositoryImpl.kt` | `AttendanceRepositoryImpl.kt` |
| Room DAO | `[Noun]Dao.kt` | `AttendanceDao.kt` |
| Room Entity | `[Noun]Entity.kt` | `AttendanceRecordEntity.kt` |
| Mapper | `[Noun]Mapper.kt` | `AttendanceRecordMapper.kt` |
| Worker | `[Noun]Worker.kt` | `GeofenceCheckWorker.kt` |
| Feature Component | `[Descriptor][Widget].kt` | `BunkSafetyCard.kt`, `CalendarDayCell.kt` |

---

## 5. FOSS & Privacy Guardrails

### 5.1 Zero-Tracking Policy

The following are **permanently and unconditionally FORBIDDEN**:

| Category | Forbidden |
|----------|-----------|
| **Analytics** | Firebase Analytics, Google Analytics, Mixpanel, Amplitude, Segment, or any cloud analytics SDK |
| **Crash Reporting** | Firebase Crashlytics, Sentry (cloud), Bugsnag, or any SDK that transmits crash data to external servers |
| **A/B Testing** | Firebase Remote Config, LaunchDarkly, or any feature-flag service that phones home |
| **Advertising** | Any ad SDK whatsoever |
| **Performance Monitoring** | Firebase Performance Monitoring |

**Permitted Alternatives:**

| Need | FOSS Solution |
|------|---------------|
| Logging (debug) | `Timber` — logs to Logcat only. No log shipping. Tree must be planted only in debug builds. |
| Crash info (local) | Write crash info to a local file in the app's private directory. User can manually export/share. |
| Analytics (local) | On-device aggregation of anonymous usage stats, never transmitted anywhere. |

```kotlin
// ✅ CORRECT — Timber planted only in debug builds.
class AttendEaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        // ❌ FORBIDDEN: Timber.plant(CrashlyticsTree()) — any external reporting tree
    }
}
```

### 5.2 Network Policy

- **No mandatory network calls.** The app must be 100% functional offline.
- Any network call (e.g., fetching OSM tiles for offline caching) must be user-initiated or have explicit user consent shown in Settings.
- All network operations must be auditable. Maintain a `NETWORK_CALLS.md` listing every endpoint the app can contact, why, and the user control for it.
- **No third-party CDNs** for assets that ship with the app.

### 5.3 Permissions Policy

| Principle | Rule |
|-----------|------|
| **Minimal Permissions** | Request only permissions strictly required for a feature that is currently being used. |
| **Contextual Requesting** | Never request permissions at app launch. Request at the point of use with a clear rationale. |
| **Graceful Degradation** | Every feature that uses a permission must function in a degraded-but-useful state if the permission is denied. The app must never crash on permission denial. |
| **Location** | `ACCESS_FINE_LOCATION` and `ACCESS_BACKGROUND_LOCATION` are only requested for the geofence feature. The app must function fully for manual attendance without them. |

---

## 6. Feature-Specific Logic Rules

### 6.1 The Bunk Calculator (`BunkCalculator`)

#### 6.1.1 Location & Type

`BunkCalculator` is a pure Kotlin `object` residing at `core/domain/calculator/BunkCalculator.kt`. It is a **shared core domain component**, not owned by any feature.

- It has **zero** Android framework dependencies and is fully unit-testable with plain JUnit.
- It is **never** instantiated directly by a ViewModel. ViewModels access it exclusively through `CalculateBunkSafetyUseCase` (`core/domain/usecase/bunk/`).
- The `DashboardViewModel` is its primary consumer via the Use Case.

#### 6.1.2 Subject Type Distinction

All calculations must account for subject type. The Subject model must carry this field:

```kotlin
enum class SubjectType {
    THEORY,  // Typically 1 class = 1 attendance unit
    LAB      // Typically 1 session = multiple consecutive attendance units (e.g., 2 or 3)
}
```

#### 6.1.3 Core Calculation Contract

The `BunkCalculator` must implement the following logic, accounting for subject type:

```kotlin
object BunkCalculator {

    /**
     * Calculates the number of classes that can be safely bunked while
     * maintaining the required attendance percentage.
     *
     * @param attended      Total classes attended so far.
     * @param total         Total classes held so far.
     * @param required      Required attendance percentage (e.g., 75.0).
     * @param subjectType   THEORY or LAB, affecting the unit weight.
     * @return              [BunkResult] containing safe bunks and projection data.
     */
    fun calculateSafeBunks(
        attended: Int,
        total: Int,
        required: Double,
        subjectType: SubjectType,
    ): BunkResult

    /**
     * Calculates how many consecutive classes the student must attend
     * to reach the required attendance percentage from their current state.
     *
     * @return Number of classes to attend, or 0 if already meeting requirements.
     */
    fun calculateClassesToAttend(
        attended: Int,
        total: Int,
        required: Double,
    ): Int

    /**
     * Projects attendance percentage after attending or bunking N more classes.
     */
    fun projectAttendance(
        attended: Int,
        total: Int,
        futureAttended: Int,
        futureTotal: Int,
    ): Double
}

// ─── Orchestrating Use Case (sits above BunkCalculator) ──────────────────────
//
// CalculateBunkSafetyUseCase (core/domain/usecase/bunk/) fetches the
// AttendanceSummary from AttendanceRepository, then delegates pure math
// to BunkCalculator. It returns a Result<BunkResult>.
//
// DashboardViewModel calls this Use Case — not BunkCalculator directly.
```

#### 6.1.4 BunkResult Model

```kotlin
data class BunkResult(
    val currentPercentage: Double,
    val safeBunksRemaining: Int,       // 0 if already below threshold
    val classesNeededToRecover: Int,   // 0 if meeting or exceeding threshold
    val projectedPercentageAfterBunk: Double,
    val isOnTrack: Boolean,            // true if currentPercentage >= required
    val subjectType: SubjectType
)
```

#### 6.1.5 Lab Subject Rules

- A Lab session counts as the `labUnitWeight` number of attendance units (default: `2`, but configurable per-subject in the database).
- When the user marks a Lab session as attended, `attended += labUnitWeight` and `total += labUnitWeight`.
- The bunk calculator must receive **pre-computed** `attended` and `total` values (already unit-weighted). The caller (Use Case) is responsible for applying the weight before passing to `BunkCalculator`.
- A Lab session is **never** split — it is either fully attended or fully absent.

#### 6.1.6 Edge Cases (Must Be Handled)

| Scenario | Behaviour |
|----------|-----------|
| `total == 0` | Return a `BunkResult` with 0% and a special `isOnTrack = true` (no classes yet). Do not divide by zero. |
| `attended > total` | Return a domain error: `AttendanceError.DataIntegrityViolation`. |
| `required <= 0` | Return a domain error: `AttendanceError.InvalidConfiguration`. |
| `required > 100` | Return a domain error: `AttendanceError.InvalidConfiguration`. |
| `safeBunksRemaining` would be negative | Clamp to `0` and populate `classesNeededToRecover` instead. |

---

### 6.2 Dashboard — The Command Center

The Dashboard is the app's primary screen and the single point of truth for all attendance and bunk safety information. Its architecture has strict contracts that must not be broken.

#### 6.2.1 Data Flow Contract

```
AttendanceRepository (Room)
         │
         ▼
GetAttendanceSummaryUseCase      ←── fetches per-subject aggregated stats
         │
         ▼
CalculateBunkSafetyUseCase       ←── passes summary into BunkCalculator
         │                           returns List<BunkResult>
         ▼
DashboardViewModel
  → _uiState.update { DashboardUiState(...) }
         │
         ▼
DashboardScreen (Composable)
  ├── OverallSummaryHeader        ←── aggregate KPI (overall %)
  ├── AttendanceKpiCard × N       ←── one card per subject
  └── BunkSafetyCard × N          ←── one bunk advice card per subject
```

#### 6.2.2 `DashboardUiState` Contract

`DashboardUiState` must be a **single, flat data class** that encodes everything the screen needs. It is the only `StateFlow` value exposed by `DashboardViewModel`.

```kotlin
// core location: feature/dashboard/ui/DashboardUiState.kt

data class DashboardUiState(
    val isLoading: Boolean = true,

    // ── Aggregate KPIs ─────────────────────────────────────────────────────
    /** Overall attendance percentage across ALL subjects, weighted by total classes. */
    val overallAttendancePercentage: Double = 0.0,

    /** True if overall percentage meets the user's configured required threshold. */
    val isOverallOnTrack: Boolean = false,

    // ── Per-Subject Data ───────────────────────────────────────────────────
    /**
     * One entry per active subject. Each entry contains the attendance summary
     * AND the bunk safety result, co-located so the UI never has to zip lists.
     */
    val subjectDashboardItems: List<SubjectDashboardItem> = emptyList(),

    // ── Error State ────────────────────────────────────────────────────────
    /** Non-null when a terminal error has occurred that prevents data display. */
    val errorMessage: String? = null,
)

/**
 * A single row of dashboard data for one subject.
 * Combines [AttendanceSummary] output with [BunkResult] output so the
 * Dashboard composable never needs to correlate two separate lists.
 */
data class SubjectDashboardItem(
    val subjectId: Long,
    val subjectName: String,
    val subjectType: SubjectType,           // THEORY | LAB

    // From AttendanceSummary
    val classesAttended: Int,
    val classesTotal: Int,
    val currentPercentage: Double,
    val requiredPercentage: Double,

    // From BunkResult
    val bunkSafety: BunkResult,             // Full BunkResult (see §6.1.4)
)
```

**Rules for `DashboardUiState`:**

| Rule | Specification |
|------|------|
| **Single StateFlow** | `DashboardViewModel` exposes exactly one `val uiState: StateFlow<DashboardUiState>`. |
| **No Split Lists** | The ViewModel must never expose separate `StateFlow<List<AttendanceSummary>>` and `StateFlow<List<BunkResult>>`. They are always combined into `SubjectDashboardItem` before emission. |
| **Loading Gate** | `isLoading = true` is the initial state. It flips to `false` only when **both** the attendance data and bunk calculations are complete. The UI must show a loading indicator while `isLoading == true`. |
| **Error is Non-Fatal by Default** | A single subject's calculation failing must not blank the whole screen. Set `errorMessage` only for unrecoverable failures (e.g., DB corrupt). Per-subject errors populate `BunkResult` with an error state, not the top-level error field. |
| **Reactive Updates** | The ViewModel must collect `AttendanceRepository.getAllSubjectSummaries()` as a `Flow`. Any change in the DB (e.g., geofence marking attendance) must automatically trigger a recalculation and re-emission of `DashboardUiState` without user action. |

#### 6.2.3 `DashboardViewModel` Structure Contract

```kotlin
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAttendanceSummaryUseCase: GetAttendanceSummaryUseCase,
    private val calculateBunkSafetyUseCase: CalculateBunkSafetyUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val _uiEvents = Channel<DashboardUiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        observeAttendanceData()
    }

    private fun observeAttendanceData() {
        viewModelScope.launch {
            getAttendanceSummaryUseCase()
                .map { summaries -> buildDashboardState(summaries) }
                .catch { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
                .collect { state -> _uiState.value = state }
        }
    }

    // buildDashboardState() calls calculateBunkSafetyUseCase per subject
    // and assembles the List<SubjectDashboardItem>.
    private suspend fun buildDashboardState(summaries: List<AttendanceSummary>): DashboardUiState { ... }
}
```

---

### 6.3 Geofencing Logic (`GeofenceCheckWorker`)

#### 6.3.1 Architecture

Geofencing is a **silent, non-interactive background process**. It must:
- Run as a `PeriodicWorkRequest` via WorkManager.
- Produce no UI while running (no notifications during the check itself).
- Only produce a notification as a result of a completed, confirmed attendance mark.

#### 6.3.2 Scheduling Contract

```kotlin
// Check interval: exactly 15 minutes. This is the minimum for PeriodicWorkRequest.
const val GEOFENCE_CHECK_INTERVAL_MINUTES = 15L

// WorkManager tags
const val GEOFENCE_WORKER_TAG = "geofence_check_worker"
const val GEOFENCE_UNIQUE_WORK_NAME = "attendease_geofence_periodic"

// Scheduling (call from Settings when user enables geofencing)
WorkManager.getInstance(context).enqueueUniquePeriodicWork(
    GEOFENCE_UNIQUE_WORK_NAME,
    ExistingPeriodicWorkPolicy.UPDATE, // UPDATE not REPLACE — preserves backoff
    PeriodicWorkRequestBuilder<GeofenceCheckWorker>(
        GEOFENCE_CHECK_INTERVAL_MINUTES, TimeUnit.MINUTES
    )
    .setConstraints(
        Constraints.Builder()
            .setRequiresBatteryNotLow(false) // Must run regardless of battery
            .build()
    )
    .addTag(GEOFENCE_WORKER_TAG)
    .build()
)
```

#### 6.3.3 Worker Logic Contract

The `GeofenceCheckWorker` must follow this exact sequence:

```
1. GET current location (using fused location or Android's LocationManager — FOSS only).
2. GET all active GeofenceZones from the database.
3. FOR EACH zone:
   a. CALCULATE distance between current location and zone center.
   b. IF distance <= zone.radiusMeters → student is INSIDE zone.
4. IF inside any zone AND current time is within an active class slot for a matching subject:
   a. CHECK if attendance has already been marked for this slot today (idempotency check).
   b. IF NOT already marked → Mark attendance as PRESENT via AttendanceRepository.
   c. FIRE a single, non-intrusive notification: "Attendance marked for [Subject Name]".
5. IF location permission is not granted → return Result.success() silently. Do NOT retry. Do NOT crash.
6. LOG the check result locally via Timber (debug only).
```

#### 6.3.4 Geofencing Rules

| Rule | Specification |
|------|------|
| **Silent Operation** | No loading spinners, no UI updates, no blocking of the app thread. |
| **Idempotency** | Marking the same attendance slot twice (identical `subjectId` + `date` + `classSlot`) must be a no-op. The DB must enforce this with a `UNIQUE` constraint. |
| **Notification** | Use a single `NotificationChannel` (`ID: "attendance_marking"`). Notifications must be dismissible and must not use sound by default. |
| **Graceful Failure** | If location is unavailable, return `Result.success()` (not failure, to avoid WorkManager exponential backoff spam). Log the miss locally. |
| **No Auto-Absent** | The Worker marks PRESENT only. It **never** automatically marks a student ABSENT. Absent marking is always a user-initiated action. |
| **Radius** | Default geofence radius is **100 meters**. User must be able to configure this per zone between 50m and 500m. |
| **Battery** | Do NOT use `setRequiresBatteryNotLow(true)`. Attendance is time-critical and must not be skipped due to low battery state. |

---

### 6.4 Calendar State System

#### 6.4.1 The Four-Color State Model

Every date on the attendance calendar must be rendered in exactly one of four states:

| State | Color Token | Meaning | Data Condition |
|-------|-------------|---------|----------------|
| **PRESENT** | `MaterialTheme.colorScheme.primaryContainer` (Green-ish) | Student attended class | `AttendanceStatus.PRESENT` |
| **ABSENT** | `MaterialTheme.colorScheme.errorContainer` (Red-ish) | Student missed class | `AttendanceStatus.ABSENT` |
| **CANCELLED** | `MaterialTheme.colorScheme.surfaceVariant` (Grey) | Class was cancelled by institution | `AttendanceStatus.CANCELLED` |
| **HOLIDAY** | `MaterialTheme.colorScheme.tertiaryContainer` (Yellow-ish) | Official holiday / no class | `AttendanceStatus.HOLIDAY` |

> **Note:** Do not hardcode hex colors. Use Material 3 color scheme tokens. The exact hue will adapt to dynamic color / user theme.

#### 6.4.2 Priority Rules (When Multiple States Could Apply)

If data is ambiguous (e.g., a class was both cancelled and the user has a record), apply this priority order:

```
HOLIDAY > CANCELLED > PRESENT > ABSENT
```

A `HOLIDAY` overrides everything. A `CANCELLED` class is never counted as absent.

#### 6.4.3 Manual Override System

| Rule | Specification |
|------|------|
| **Override Capability** | A user may manually change `ABSENT` → `PRESENT` or vice versa on any past date, and may mark any date as `CANCELLED` or `HOLIDAY`. |
| **Override Indicator** | A date that has been manually overridden must display a distinct visual indicator (e.g., a small dot or icon) on the calendar cell so the user knows it was changed. |
| **Override Persistence** | Overrides are stored as `isManualOverride = true` on the `AttendanceRecord`. They must persist across app restarts. |
| **Override Audit** | The detail view for a date must show whether the record is a manual override and when it was last modified (`lastModifiedAt: Instant`). |
| **Geofence vs Manual** | A manual override always takes precedence over a geofence-auto-marked record. If a geofence marks PRESENT and the user then manually marks ABSENT, the user's choice wins and the record is flagged `isManualOverride = true`. |
| **No Future Marking** | Manual marking of future dates is **FORBIDDEN**. The UI must disable future dates in the calendar. |

#### 6.4.4 Calendar Composable Contract

The Holiday feature also belongs to the Calendar screen. The `CalendarViewModel` owns two additional holiday-related Use Cases:

```kotlin
// CalendarViewModel injects:
//   AddHolidayUseCase       — marks a single date as HOLIDAY
//   AddBulkHolidaysUseCase  — marks a date range as HOLIDAY
//
// HolidayPickerDialog collects a date or range from the user and calls
// the appropriate ViewModel event. The ViewModel writes via Use Case →
// HolidayRepository. The Calendar reacts reactively via Flow.
```

```kotlin
// The CalendarDayCell composable must be pure (stateless).
// It receives state; it does not compute it.
@Composable
fun CalendarDayCell(
    day: Int,
    attendanceState: AttendanceDayState, // PRESENT, ABSENT, CANCELLED, HOLIDAY, or NONE
    isManualOverride: Boolean,
    isToday: Boolean,
    isFuture: Boolean,           // future dates are non-interactive
    onDayClick: (Int) -> Unit    // no-op if isFuture == true
)
```

---

## 7. Coding Style & Quality Standards

### 7.1 Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Classes | `PascalCase` | `AttendanceRecord`, `BunkCalculator` |
| Functions | `camelCase` | `calculateSafeBunks()`, `getSubjects()` |
| Variables | `camelCase` | `currentPercentage`, `isManualOverride` |
| Constants | `UPPER_SNAKE_CASE` | `GEOFENCE_CHECK_INTERVAL_MINUTES` |
| Packages | `lowercase.dotted` | `com.attendease.feature.bunk` |
| Composable functions | `PascalCase` | `CalendarDayCell`, `BunkResultCard` |
| Boolean properties | `is` / `has` / `can` prefix | `isLoading`, `hasError`, `canBunkMore` |

**Domain-Driven Names are MANDATORY.** Generic names are **FORBIDDEN**:

| ❌ FORBIDDEN | ✅ REQUIRED |
|-------------|------------|
| `DataObj`, `Model`, `Item` | `AttendanceRecord`, `Subject`, `GeofenceZone` |
| `doStuff()`, `process()` | `markAttendance()`, `calculateBunks()` |
| `data`, `info`, `temp` | `attendanceSummary`, `subjectConfig` |
| `Mgr`, `Util`, `Helper` | Use specific domain names or Use Cases |

### 7.2 Error Handling — The Result Wrapper Contract

**Every Repository method that can fail MUST return `Result<T>`.**

Use Kotlin's built-in `kotlin.Result<T>` or define a domain-specific sealed class:

```kotlin
// Option A: Kotlin built-in Result<T> (preferred for simplicity)
interface AttendanceRepository {
    suspend fun markAttendance(record: AttendanceRecord): Result<AttendanceRecord>
    suspend fun deleteRecord(recordId: Long): Result<Unit>
}

// Implementation
override suspend fun markAttendance(record: AttendanceRecord): Result<AttendanceRecord> {
    return runCatching {
        val entity = record.toEntity()
        val id = attendanceDao.insert(entity)
        record.copy(id = id)
    }
}

// Option B: Domain-specific sealed Result (use when richer error info is needed)
sealed class AttendanceResult<out T> {
    data class Success<T>(val data: T) : AttendanceResult<T>()
    sealed class Error : AttendanceResult<Nothing>() {
        data class DatabaseError(val cause: Throwable) : Error()
        object DataIntegrityViolation : Error()
        object RecordNotFound : Error()
    }
}
```

**Rules:**
- `try-catch` at the repository implementation level. Never in Use Cases or ViewModels (they handle `Result`, not exceptions).
- Use Cases unwrap `Result` and map to domain errors.
- ViewModels unwrap Use Case results and map to UI state.
- **NEVER** swallow exceptions silently. Every `catch` block must at minimum log via `Timber.e(e)` (debug) or write to the local error log.

### 7.3 Coroutines & Flow

| Rule | Specification |
|------|------|
| **Dispatchers** | Inject `CoroutineDispatcher` via DI. Never call `Dispatchers.IO` directly in production code (hardcodes threading, makes testing hard). |
| **`viewModelScope`** | All ViewModel coroutines use `viewModelScope`. Never create a `CoroutineScope` manually in a ViewModel. |
| **Flow collection** | Use `collectAsStateWithLifecycle()` in Composables (from `androidx.lifecycle:lifecycle-runtime-compose`). Never use `.collectAsState()` (does not respect lifecycle). |
| **Cancellation** | Cooperate with cancellation. Ensure long-running loops check `isActive` or use cancellable functions. |
| **`GlobalScope`** | `GlobalScope` is **FORBIDDEN** everywhere. |

### 7.4 Code Formatting

- Follow the **official Kotlin Coding Conventions**: https://kotlinlang.org/docs/coding-conventions.html
- **`ktlint`** must be configured and passing in CI. No PR merges with ktlint violations.
- Max line length: **120 characters**.
- Trailing commas are **REQUIRED** in multi-line function parameters and collection literals.
- Use `@Suppress` annotations sparingly; every suppression requires a comment explaining why.

### 7.5 Comments & Documentation

| Rule | Specification |
|------|------|
| **KDoc** | All `public` and `internal` functions in `domain/` and `data/` must have KDoc comments. |
| **`TODO`** | Permitted in code, but must include a GitHub Issue number: `// TODO(#42): Handle lab weight edge case`. |
| **`FIXME`** | Treated as a build warning. Must be resolved before a feature is merged to `main`. |
| **Magic Numbers** | **FORBIDDEN.** All magic numbers must be named constants with a KDoc comment explaining their origin. |

```kotlin
// ❌ FORBIDDEN
if (attendance >= 0.75) { ... }

// ✅ REQUIRED
/** Minimum attendance fraction mandated by institutional policy. */
const val MINIMUM_ATTENDANCE_FRACTION = 0.75
if (currentFraction >= MINIMUM_ATTENDANCE_FRACTION) { ... }
```

---

## 8. Testing Mandates

### 8.1 Test Coverage Requirements

| Layer | Test Type | Minimum Coverage Target |
|-------|-----------|------------------------|
| `domain/` (Use Cases, BunkCalculator) | Unit Tests (JUnit 5 + Kotlin Test) | **90%** |
| `data/` (Repository Impls, Mappers) | Unit Tests + Room in-memory DB tests | **80%** |
| `ui/` (ViewModels) | Unit Tests with `kotlinx-coroutines-test` | **75%** |
| Composables | Compose UI Tests (optional for MVP, required post-v1) | **Key user flows** |

### 8.2 Testing Rules

- **`BunkCalculator` must have exhaustive unit tests** covering all edge cases defined in Section 6.1.6.
- **`CalculateBunkSafetyUseCase` must be tested** with mocked `AttendanceRepository` and real `BunkCalculator` (do not mock the calculator — it is pure logic).
- **`DashboardViewModel` must be tested** to verify that a change emitted by the `AttendanceRepository` Flow causes a full recalculation and a new `DashboardUiState` emission with correct `SubjectDashboardItem` values.
- **Repository tests** must use Room's `in-memory` database, not mocks of Room itself.
- **ViewModel tests** must use `TestCoroutineDispatcher` / `UnconfinedTestDispatcher` and `Turbine` for Flow testing.
- **No `Thread.sleep()`** in tests. Use `advanceTimeBy()` or `runTest`.
- Test function names must follow the pattern: `givenCondition_whenAction_thenExpectedResult`.

```kotlin
// ✅ CORRECT test naming
@Test
fun `givenZeroTotalClasses_whenCalculateSafeBunks_thenReturnsOnTrackWithZeroPercentage`() {
    val result = BunkCalculator.calculateSafeBunks(
        attended = 0, total = 0, required = 75.0, subjectType = SubjectType.THEORY
    )
    assertThat(result.isOnTrack).isTrue()
    assertThat(result.currentPercentage).isEqualTo(0.0)
}
```

---

## 9. Dependency Management

### 9.1 Version Catalog

All dependencies **MUST** be declared in `gradle/libs.versions.toml`. No version literals in `build.gradle.kts` files.

```toml
# gradle/libs.versions.toml — example structure
[versions]
kotlin = "2.0.x"          # Kotlin 2.0+
compose-bom = "..."       # Use BOM for all Compose versions
room = "..."
hilt = "..."
osmdroid = "..."
workmanager = "..."
timber = "..."

[libraries]
# All library declarations here

[plugins]
# All plugin declarations here
```

### 9.2 Dependency Audit Process

Before adding any new dependency, the author **MUST** answer these questions in the PR description:

1. **License:** What is the license? Is it GPL-compatible?
2. **Network:** Does it make any network calls? To what endpoints?
3. **Data collection:** Does it collect or transmit any user data?
4. **FOSS alternative:** Is there a FOSS alternative that was considered?
5. **Necessity:** Why is a new dependency needed instead of implementing it ourselves?

Any dependency that fails questions 2, 3, or 4 with a concerning answer is **REJECTED** by default.

### 9.3 Prohibited Dependencies (Permanent Blocklist)

| Dependency | Reason |
|------------|--------|
| `com.google.firebase:*` | Proprietary, phones home |
| `com.google.android.gms:play-services-maps` | Proprietary Google Maps |
| `com.google.android.gms:play-services-location` | Proprietary, use `LocationManager` or FOSS wrapper |
| `io.sentry:*` | Cloud crash reporting |
| `com.mixpanel.android:*` | Analytics |
| Any ad SDK | Advertising |
| `com.squareup.leakcanary:*` | Debug only — must never ship in release builds |

> **Note on `play-services-location`:** Android's `LocationManager` is acceptable as it is part of the AOSP. The `FusedLocationProviderClient` from Play Services is NOT permitted as it requires Google Play Services.

---

## 10. Contribution & Review Protocol

### 10.1 Branching Strategy

| Branch | Purpose | Protection |
|--------|---------|-----------|
| `main` | Stable, release-ready code | Protected. Requires PR + 1 review + CI pass. |
| `develop` | Integration branch | Requires PR + CI pass. |
| `feature/<name>` | New features | Author's branch. |
| `fix/<issue-id>` | Bug fixes | Author's branch. |
| `chore/<name>` | Build/config changes | Author's branch. |

### 10.2 Pull Request Checklist

Every PR **MUST** satisfy all of the following before merge:

- [ ] No new violations of the Dependency Rule (Section 3.1).
- [ ] No new `StateFlow` properties split across a ViewModel (Section 3.3).
- [ ] All new Repository methods return `Result<T>` (Section 7.2).
- [ ] No magic numbers introduced (Section 7.5).
- [ ] `ktlint` passes with zero violations.
- [ ] Unit tests written for all new domain logic.
- [ ] No new proprietary dependencies introduced (Section 9.3).
- [ ] No `GlobalScope` usage.
- [ ] No `!!` operator used outside of test files.
- [ ] `NETWORK_CALLS.md` updated if any network call was added or modified.
- [ ] This `AGENTS.md` file was consulted and the PR does not contradict it.

### 10.3 AI Agent-Specific Rules

When an AI agent (Copilot, Claude, Cursor, etc.) generates code for this project, it **MUST**:

1. **Read this file first** before generating any code.
2. **Never introduce** a dependency not already in `libs.versions.toml` without flagging it for human review.
3. **Validate generated ViewModels** expose exactly one `StateFlow`.
4. **Validate generated Repository methods** return `Result<T>`.
5. **Never generate** a Room `@Entity` that is also a Domain model class.
6. **Generate mappers** whenever generating a new Room entity.
7. **Flag, do not guess** — if a rule in this document is ambiguous for the task at hand, output a comment flagging the ambiguity rather than silently making an assumption.

---

## Changelog

| Version | Date | Summary |
|---------|------|---------|
| **1.1.0** | 2026-05-02 | Re-aligned `feature/` to 3-screen architecture. Consolidated `feature/attendance/` + `feature/bunk/` into `feature/dashboard/`. Promoted `BunkCalculator` to `core/domain/calculator/`. Added `DashboardUiState` contract (§6.2). Added `SubjectDashboardItem` model. Added `feature/calendar/` holiday management spec. Added `feature/subjects/` module. Renumbered §6 subsections. |
| 1.0.0 | 2026-05-02 | Initial rulebook. |

---

*This document is the authoritative source of truth for the AttendEase project. All rules herein supersede any conflicting convention, library default, or AI agent suggestion. To propose an amendment, open a GitHub Discussion tagged `rulebook-amendment`.*

---
**AGENTS.md** | AttendEase | GPL-3.0 | Last updated: 2026-05-02