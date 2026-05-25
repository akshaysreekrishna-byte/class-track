# AGENTS.md — Engineering Rules for class-track

This file defines the mandatory rules and engineering standards that all AI agents and contributors must follow when implementing any part of this codebase. These are non-negotiable constraints, not suggestions.

---

## 1. Architecture Rules

### 1.1 Layer Separation is Absolute
- The project is divided into three layers: **Domain**, **Data**, and **UI**. No layer may directly import from a layer it does not own.
- Allowed dependency direction: `UI → Domain ← Data`. The Data layer implements Domain interfaces. The UI layer depends on Domain abstractions only.
- **Never** import Android framework classes (`android.*`, `androidx.*`) into the Domain layer. Domain is pure Kotlin.
- **Never** import Room, WorkManager, or any persistence/infrastructure class into the Domain layer.

### 1.2 Domain Layer (`core/domain`)
- Contains only: Entities, Repository interfaces, Use Case classes, and pure business logic (e.g., `BunkCalculator`).
- All business rules live here. If a rule can be unit-tested without Android, it belongs in Domain.
- Use Cases must be single-responsibility. One Use Case = one operation.
- Entities are plain Kotlin data classes. No annotations (Room, Gson, etc.) allowed on Domain entities.

### 1.3 Data Layer (`core/data`)
- Implements Repository interfaces defined in Domain.
- Contains: Room DAOs, Database class, Entity DTOs (with Room annotations), Mappers, and Repository implementations.
- **Mappers are mandatory.** Never pass a Room entity to the Domain or UI layers directly. Always map between Data DTOs and Domain entities.
- Room entities and Domain entities are separate classes. They may share field names but are never the same class.

### 1.4 UI Layer (`feature/*`)
- Contains: Jetpack Compose screens, ViewModels, and UI state classes.
- ViewModels expose `StateFlow<UiState>`. They never expose raw Domain entities directly to the Compose layer — wrap them in a dedicated UI state/model class if the shape differs.
- ViewModels may only interact with the Domain layer through Use Cases. No direct Repository calls from ViewModels.
- Screens are stateless. All state is hoisted into the ViewModel.

---

## 2. Code Quality Rules

### 2.1 No Business Logic in the Wrong Layer
- If logic can change based on a business rule (e.g., attendance thresholds, bunk calculations), it belongs in Domain, not in a ViewModel or Composable.
- ViewModels handle UI state transformation only: formatting, loading states, error mapping.

### 2.2 Single Responsibility
- Every class does one thing. If you need to describe a class with "and", split it.
- Use Cases are named with a verb: `GetSubjectsUseCase`, `MarkAttendanceUseCase`, `CalculateBunkSafetyUseCase`.

### 2.3 Dependency Injection via Hilt
- All dependencies are injected. Never use static instances, singletons accessed directly, or `object` classes that hold mutable state.
- Hilt modules live in the Data layer or app module. Domain has zero knowledge of Hilt.

### 2.4 Kotlin Idioms
- Use `data class` for value-holding types. Use `sealed class` or `sealed interface` for representing states and results.
- Prefer `Result<T>` or a custom `sealed class` (e.g., `Outcome.Success`/`Outcome.Error`) over throwing exceptions across layer boundaries.
- Never use `!!` (non-null assertion). Handle nullability explicitly.
- Avoid mutable state (`var`, `MutableList`) where immutable alternatives exist.

### 2.5 Coroutines and Flow
- All async operations use Kotlin Coroutines. No callbacks, no `Thread`, no `AsyncTask`.
- Repository interfaces return `Flow<T>` for observable data streams and `suspend fun` for one-shot operations.
- ViewModels collect Flows using `viewModelScope`. Never collect in Composables directly from a ViewModel's Flow without `collectAsStateWithLifecycle`.
- Background operations (geofencing, scheduling) use WorkManager. Never use bare coroutine launches for work that must survive process death.

---

## 3. Testing Rules

### 3.1 Domain Must Be 100% Unit Tested
- Every Use Case and every method on `BunkCalculator` must have corresponding unit tests.
- Tests live in the `test/` source set (not `androidTest/`). They run on the JVM with no Android dependencies.
- Use JUnit 5 and `kotlinx-coroutines-test`. No Robolectric in Domain tests.

### 3.2 Data Layer Integration Tests
- Repository implementations are tested with an in-memory Room database using `androidx.test`.
- Mappers are unit tested independently from the Repository.

### 3.3 No Logic Goes Untested
- Any class containing conditional logic (`if`, `when`, calculation) must have tests covering all branches, including edge cases (zero attendance, 100% attendance, single class remaining).
- Tests are written before or alongside implementation, not after.

### 3.4 Test Naming Convention
- Use the pattern: `given_<precondition>_when_<action>_then_<expected>`.
- Example: `given_studentHas75Percent_when_oneAbsence_then_safeBunkCountDecreases`.

---

## 4. Privacy and FOSS Rules

### 4.1 No Proprietary SDKs
- Do not add any dependency that requires Google Play Services (e.g., `com.google.android.gms:play-services-location`).
- Location and geofencing must use AOSP `LocationManager` only.
- Maps must use OSMDroid (OpenStreetMap). No Google Maps SDK.

### 4.2 No External Network Calls
- The app must never make a network request at runtime. No analytics, no crash reporters, no remote config.
- All data is stored locally using Room. No Firebase, no Supabase, no any cloud database.

### 4.3 No Tracking or Telemetry
- No event logging, no user identification, no device fingerprinting of any kind.

---

## 5. Dependency Rules

### 5.1 Evaluate Before Adding
- Before adding any new library, confirm: Is it FOSS-licensed (Apache 2, MIT, GPL-compatible)? Does it require network access or proprietary services? Can the feature be implemented without it?
- Prefer standard Jetpack and Kotlin libraries. Avoid third-party libraries for problems the standard library already solves.

### 5.2 Approved Stack
Only the following libraries are approved. Any addition requires explicit justification:

| Purpose | Library |
|---|---|
| Language | Kotlin 2.0+ |
| UI | Jetpack Compose + Material 3 |
| DI | Dagger Hilt |
| Database | Room Persistence Library |
| Background | Jetpack WorkManager |
| Maps/Geofencing | OSMDroid |
| Async | Kotlin Coroutines + Flow |
| Testing | JUnit 5, kotlinx-coroutines-test, androidx.test |

---

## 6. Commit and Implementation Discipline

### 6.1 Implement in Layer Order
- Always implement Domain first, then Data, then UI. Never write a Composable screen before the Use Case it depends on exists and is tested.
- If a Use Case is not yet implemented, use a stub/fake — never skip the layer.

### 6.2 No Placeholder Logic in Production Code
- `TODO()` and `NotImplementedError` are acceptable only in interfaces and abstract stubs during active development of that specific layer. They must not exist in any layer that is marked complete.

### 6.3 Naming Conventions
| Element | Convention |
|---|---|
| Entities | `Subject`, `AttendanceRecord` (nouns) |
| Use Cases | `GetSubjectUseCase`, `MarkAttendanceUseCase` (verb + noun + `UseCase`) |
| Repository interfaces | `SubjectRepository`, `AttendanceRepository` |
| Room DTOs | `SubjectEntity`, `AttendanceRecordEntity` |
| ViewModels | `SubjectViewModel`, `DashboardViewModel` |
| UI State | `SubjectUiState`, `DashboardUiState` |
| Mappers | `SubjectMapper`, `AttendanceMapper` (static/companion functions: `toEntity()`, `toDomain()`) |

### 6.4 No Dead Code
- Remove unused imports, unused parameters, and commented-out code before considering any unit of work complete.

---

## 7. File and Component Size Limits

These limits exist to enforce maintainability. If a file approaches its limit, that is a signal to decompose — not a reason to request an exception.

### 7.1 Hard Line Limits per File Type

| Component Type | Max Lines |
|---|---|
| Domain Entity (`data class`) | 50 |
| Repository Interface | 50 |
| Use Case class | 50 |
| Mapper class/object | 80 |
| Room DAO interface | 80 |
| Room Entity (`@Entity` class) | 60 |
| Repository Implementation | 120 |
| ViewModel | 150 |
| Composable screen file | 200 |
| Individual `@Composable` function | 80 |
| WorkManager Worker class | 120 |
| Hilt Module (`@Module`) | 80 |
| Test class | 200 |
| Any other Kotlin file | 150 |

### 7.2 Function-Level Limits
- No function or method body may exceed **30 lines** (excluding blank lines and comments).
- If a function exceeds this, extract the excess into a private helper with a descriptive name.
- Composable functions follow the same 30-line body limit. Break complex layouts into smaller `@Composable` sub-functions.

### 7.3 What to Do When a Limit Is Reached
- **Use Case is too large**: the use case is doing more than one thing — split into two Use Cases.
- **ViewModel is too large**: extract a separate `UiStateMapper` class, or split the screen into sub-screens each with their own ViewModel.
- **Composable screen is too large**: decompose into smaller `@Composable` functions, each in their own file if they are reusable components.
- **Repository implementation is too large**: the repository likely handles too many entity types — split by domain concern.
- **Mapper is too large**: likely mapping too many fields with complex transformations — introduce intermediate value objects or split into focused mappers.
- **Never increase a limit to accommodate growing code.** Decompose instead.

---

## 8. Worktree Workflow

All code changes must be implemented inside a dedicated Git worktree. This isolates in-progress work from the main checkout and keeps the repo state clean.

### 8.1 Always Create Worktrees Manually
- Create every worktree at `~/class-track-worktrees/<branch-name>/` — outside the main repository directory.
- **Never** create a worktree inside the project directory (e.g. `.claude/worktrees/` or any subdirectory of the repo). The Metro bundler watches the entire project tree and will break if a nested worktree is present.

```bash
# Correct — worktree lives outside the repo
git worktree add ~/class-track-worktrees/<branch-name> -b <branch-name>
cd ~/class-track-worktrees/<branch-name>
```

### 8.2 Never Use `isolation: "worktree"` When Launching Agents
- The built-in Claude agent harness creates worktrees inside `.claude/worktrees/` (inside the repo), which violates rule 8.1.
- **Never** pass `isolation: "worktree"` when spawning an Agent.
- Instead: create the worktree manually first (per 8.1), `cd` into it, and then launch the Agent from that directory — or launch the Agent without an isolation flag and let it operate in the already-checked-out worktree.

### 8.3 One Worktree Per Branch
- Each feature branch or task gets its own worktree directory. Do not reuse a worktree across unrelated branches.
- When work is merged and the branch is deleted, remove the worktree:

```bash
git worktree remove ~/class-track-worktrees/<branch-name>
```

### 8.4 Worktree Naming
- Name worktrees after their branch: `~/class-track-worktrees/feature-attendance-ui/`, `~/class-track-worktrees/fix-bunk-calculator/`, etc.
- Branch names follow kebab-case: `<type>-<short-description>` (e.g. `feature-subject-list`, `fix-mapper-null-crash`, `refactor-domain-entities`).

---

## 9. CodeGraph Navigation

All agents must use the CodeGraph skill for structural codebase exploration. Raw filesystem scanning (`grep -r`, `find`) is a last resort, not a first step.

### 9.1 When CodeGraph Is Mandatory
Use `codegraph_explore` before touching any file in these situations:
- Any task that spans more than one layer (e.g. adding a Use Case that requires a new DAO and a new screen).
- Tracing where a Domain interface is implemented in the Data layer, or consumed in the UI layer.
- Identifying all callers of a Use Case before modifying its signature or return type.
- Answering structural questions: "What depends on `BunkCalculator`?", "Which ViewModels consume `AttendanceRepository`?", "What mappers exist for `SubjectEntity`?".
- Before refactoring any class that appears in more than one layer.

### 9.2 Prohibited Without CodeGraph First
- **Never** run a global `grep` across the workspace to locate a class, function, or symbol without first checking the CodeGraph index.
- **Never** open and scan files speculatively (opening five files hoping one contains what you need). Use CodeGraph to pinpoint the exact file, then open it.

### 9.3 Required Workflow
1. **Query first.** Invoke `codegraph_explore` to get the structural map for the relevant module, class, or symbol.
2. **Narrow scope.** Use the graph result to identify the exact file paths and function names relevant to the task.
3. **Then read.** Only open files (`cat`, `view`) once their relevance is confirmed by CodeGraph.
4. **Re-query on changes.** After adding or renaming a class, notify the user if the CodeGraph index needs re-indexing, or trigger the indexing process if available.

### 9.4 When CodeGraph Does Not Apply
- Single-file edits where the file path is already known (e.g. fixing a typo in a specific Composable).
- Simple variable renaming scoped entirely within one file.
- Standalone bash commands unrelated to navigating the codebase.

### 9.5 Rationale
This codebase enforces strict layer separation (§1.1). Violations — importing a Room entity into Domain, calling a Repository directly from a ViewModel — are easy to introduce when navigating by grep alone. CodeGraph makes the dependency graph explicit and machine-queryable, so structural violations are caught before any file is written.