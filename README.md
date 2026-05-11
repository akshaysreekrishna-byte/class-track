# class-track

class-track is an open-source, privacy-focused, offline-first Android application designed to help university students manage their class attendance with precision. It features advanced "bunk safety" analytics and fully local, background geofenced attendance marking.

> [!IMPORTANT]
> **Project Status:** This project is currently being rebuilt with a **Logic-First** approach. We are prioritizing the Domain and Data layers to ensure a robust foundation before implementing the UI.

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

### Phase 1: Core Logic (Current)
- [ ] Define Domain Entities (`Subject`, `AttendanceRecord`, `Holiday`).
- [ ] Implement `BunkCalculator` with 100% unit test coverage.
- [ ] Create Repository Interfaces.
- [ ] Implement Data Layer (Room DAOs and Entities).

### Phase 2: Background Services
- [ ] Implement `GeofenceCheckWorker`.
- [ ] Setup WorkManager scheduling.

### Phase 3: UI Implementation
- [ ] Dashboard Screen (KPIs & Bunk Safety).
- [ ] Calendar & Timetable View.
- [ ] Subject Management UI.

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