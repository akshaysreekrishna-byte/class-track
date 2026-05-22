# class-track

class-track is an open-source, privacy-focused, offline-first Android application designed to help university students manage their class attendance with precision. It features advanced "bunk safety" analytics and fully local, background geofenced attendance marking.

> [!IMPORTANT]
> **Project Status:** Phases 1–4 are complete. The Domain, Data, Background Intelligence layers, and Core UI Foundation are fully implemented. Development is now moving to **Phase 5: Analytics Dashboard & Visuals**.

## 🛡️ Philosophy

- **FOSS-First**: Built entirely using Free and Open Source Software. No proprietary SDKs, no Google Play Services dependencies, and no vendor lock-in.
- **Privacy by Default**: Zero user tracking, zero external analytics, and zero external cloud databases. Your academic data lives purely on your device.
- **Local-First**: 100% functional offline.

## ✨ Features

- **Predictive Bunk Analytics**: Accurately calculates "Safe Bunks Remaining" and "Classes Needed to Recover" based on subject weightages (Theory vs. Lab).
- **Background Geofencing**: Automatically detects when you are inside a designated class area and marks you as `PRESENT`. Utilizes pure AOSP `LocationManager` and `WorkManager`.
- **Subject Management**: Configure subjects, specify whether they are Lab or Theory modules, and configure custom geofence radii.
- **Visual Timetable & Calendar**: Track daily classes through a 4-color semantic grid (Present, Absent, Cancelled, Holiday).

## 🏗️ Architecture Design

The app follows strict **Clean Architecture** principles as defined in [AGENTS.md](file:///Users/Home/projects/Attendancetracker/class-track/AGENTS.md):

1. **Domain Layer (`core/domain`)**: Pure Kotlin logic. Houses entities, use cases, and the `BunkCalculator`. Zero Android framework imports.
2. **Data Layer (`core/data`)**: Room implementations, Repositories, and Mappers. Handles all persistence logic.
3. **UI Layer (`feature/*`)**: Jetpack Compose screens consuming `StateFlow` from ViewModels. (Currently in planning).

## 🛠️ Technology Stack

- **Language**: Kotlin 2.0+ (Coroutines + Flow)
- **UI Toolkit**: Jetpack Compose & Material Design 3
- **Dependency Injection**: Dagger Hilt
- **Database**: Room Persistence Library
- **Background Processing**: Jetpack WorkManager
- **Maps & Geofencing**: OSMDroid (OpenStreetMap)

## 📈 Development Roadmap

### Phase 1: Project Setup & Domain Foundation
- [x] Initialize Android project structure and `libs.versions.toml` (Version Catalog).
- [x] Define core Domain Entities (`Subject`, `AttendanceRecord`, `GeofenceConfig`).
- [x] Implement `BunkCalculator` logic with 100% Unit Test coverage.
- [x] Create abstract Repository Interfaces for data decoupling.
- [x] Define Use Case interactor classes for core business flows.

### Phase 2: Data Persistence & Infrastructure
- [x] Setup Room Database schemas and Type Converters.
- [x] Implement Room DAOs for all core entities.
- [x] Complete Repository implementations in the Data Layer.
- [x] Implement Data Mappers (Entity <-> DTO) and integration tests.

### Phase 3: Background Intelligence (Geofencing)
- [x] Implement AOSP-compliant `LocationProvider` logic.
- [x] Develop `GeofenceCheckWorker` using Jetpack WorkManager.
- [x] Setup periodic scheduling for background attendance detection.
- [x] Implement local notification system for "Auto-Marked" events.

### Phase 4: Core UI & Navigation Foundation
- [x] Establish Material 3 Design System (Colors, Typography, Shapes).
- [x] Setup Hilt-integrated Navigation Graph.
- [x] Implement Subject Management UI (CRUD operations).
- [x] Build reusable UI components (Attendance cards, status chips).

### Phase 5: Analytics Dashboard & Visuals
- [ ] Create "Bunk Safety" Dashboard with dynamic KPI cards.
- [ ] Implement Semantic Timetable Grid (4-color status tracking).
- [ ] Build History/Calendar View for long-term attendance auditing.
- [ ] Integrate interactive charts for subject-wise performance.

### Phase 6: Refinement & Privacy Features
- [ ] Implement local backup/restore (JSON/CSV export).
- [ ] Optimize background battery consumption for geofencing.
- [ ] Final UI Polish, animations (Framer-motion style), and dark mode.

## 🚀 Building the Project

Ensure you have Gradle properly configured to build the app locally.

```bash
# To build a debug APK
./gradlew assembleDebug

# To run unit tests
./gradlew testDebugUnitTest
```

## 📜 License
This project is licensed under the GPL-3.0 License.
