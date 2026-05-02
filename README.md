# AttendEase (Class Tracker)

AttendEase is an open-source, privacy-focused, offline-first Android application designed to help university students manage their class attendance with precision. It features advanced "bunk safety" analytics and fully local, background geofenced attendance marking.

## 🛡️ Philosophy

- **FOSS-First**: Built entirely using Free and Open Source Software. No proprietary SDKs, no Google Play Services dependencies, and no vendor lock-in.
- **Privacy by Default**: Zero user tracking, zero external analytics, and zero external cloud databases. Your academic data lives purely on your device.
- **Local-First**: 100% functional offline.

## ✨ Features

- **Predictive Bunk Analytics**: Accurately calculates "Safe Bunks Remaining" and "Classes Needed to Recover" based on subject weightages (Theory vs. Lab).
- **Background Geofencing**: Automatically detects when you are inside a designated class area for more than 5 minutes and marks you as `PRESENT` automatically. Utilizes pure AOSP `LocationManager` and `WorkManager` (No Play Services).
- **Subject Management**: Configure subjects, specify whether they are Lab or Theory modules, and configure custom geofence radii for each location.
- **Visual Timetable & Calendar**: Track daily classes through a 4-color semantic grid (Present, Absent, Cancelled, Pending).

## 🛠️ Technology Stack

- **Language**: Kotlin 2.0
- **UI Toolkit**: Jetpack Compose & Material Design 3 (Material You)
- **Architecture**: Clean Architecture (Domain, Data, UI Layers) with Unidirectional Data Flow (UDF)
- **Dependency Injection**: Dagger Hilt
- **Database**: Room Persistence Library
- **Background Processing**: Jetpack WorkManager
- **Maps & Geofencing**: OSMDroid (OpenStreetMap)

## 🏗️ Architecture Design

The app is built using strict Clean Architecture rules:
1. **Domain Layer (`:core:domain`)**: Pure Kotlin logic. Houses entities, use cases, and `BunkCalculator`. Absolutely zero Android framework imports.
2. **Data Layer (`:core:data`)**: Implements Repositories via Room DAOs. Data is heavily safeguarded using `Result<T>` monad wrappers to prevent UI crashes.
3. **UI Layer (`:feature:xyz`)**: Jetpack Compose screens consuming single `StateFlow` streams from feature-specific ViewModels.

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