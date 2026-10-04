# REM — You Forget. We Remember.
### The Student AI Productivity & Memory Suite

> **"Just send it. We'll remember."**

REM is a production-grade, local-first Android application designed for Gen-Z and collegiate power users. It acts as an autonomous AI memory for students: students send screenshots, photos, syllabi, voice notes, PDFs, or clipboard fragments, and REM extracts deadlines, schedules, courses, and tasks without tedious manual data entry.

---

## 🚀 Key Features

1. **Autonomous Ingestion Engine (Zero-Effort Capture)**
   - **Screenshot & Document OCR (CameraX):** Interactive live reticle with animated scanning laser, corner brackets, and flash controls to scan physical syllabi, lecture slides, and assignment handouts.
   - **Smart Paste & Quick Type:** Instant clipboard detection for office hours, room assignments, and email announcements.
   - **PDF & Document Importer:** Ingest syllabi, problem sets, and slide decks.
   - **Voice Note Transcriber:** Tap-and-record stream of consciousness with automatic task distillation.
   - **Android Sharesheet Receiver:** Send files directly from WhatsApp, Chrome, or Canvas via `ACTION_SEND` and `ACTION_SEND_MULTIPLE`.

2. **Neo-Brutalist Graphic Novel UI**
   - High-contrast ink outlines (`#1C1B1B`), Electric Taxi Yellow (`#FFC700`), and Energetic Tangerine (`#FE6A34`).
   - Tactile `StompButton` components with physical drop shadow collapse on press.
   - Segmented hazard progress tracking with diagonal warning stripes.
   - 2D expressive mascot companions (Synapse Brain Bot & Mascot Robot).

3. **Neural Radar Gap Analysis**
   - Autonomous schedule scanner that identifies open high-focus study gaps (e.g. 2:00 PM – 4:00 PM).
   - "Auto-Block Focus Window" to immediately convert identified gaps into sprint timers.

4. **Focus Mode (Sprint Timer)**
   - Distraction-free sprint interface with countdown clock, active task details, syllabus source provenance, and celebratory XP rewards upon completion.

5. **Local-First Memory Vault**
   - 100% offline-ready SQLite / Room database.
   - Full-text search and category filtering (*Assignments*, *Exams*, *Projects*, *Notes*).
   - Star, share, and verify AI extraction confidence scores.

6. **Ask REM AI Assistant**
   - Query your academic memory vault conversationally: "When is Physics due?", "Summarize CS 101 spec", "Where is my free time today?".
   - Strict source provenance citations.

7. **Student Data Sovereignty & Privacy**
   - Complete local database persistence.
   - One-tap JSON data export and vault purge controls.

---

## 📁 Project Architecture & Directory Structure

```
├── app/
│   ├── build.gradle.kts                # Android configuration & CameraX/Room dependencies
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml     # Permissions (Camera, Notifications, Audio, Sharesheet)
│   │   │   ├── java/com/example/
│   │   │   │   ├── MainActivity.kt     # Root routing, back-stack & Android intent handling
│   │   │   │   ├── data/
│   │   │   │   │   ├── ai/
│   │   │   │   │   │   └── RemAiEngine.kt       # Local heuristic & AI extraction engine
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── RemDatabase.kt       # Room database definition
│   │   │   │   │   │   ├── dao/
│   │   │   │   │   │   │   ├── MemoryDao.kt     # Reactive Flow queries for memories
│   │   │   │   │   │   │   ├── TaskDao.kt       # Task management & completion queries
│   │   │   │   │   │   │   └── ChatDao.kt       # Assistant chat history
│   │   │   │   │   │   └── entity/
│   │   │   │   │   │       ├── MemoryEntity.kt  # Memory schema
│   │   │   │   │   │       ├── TaskEntity.kt    # Task schema
│   │   │   │   │   │       └── ChatMessageEntity.kt
│   │   │   │   │   └── repository/
│   │   │   │   │       └── RemRepository.kt     # Offline-first data repository & initial seeding
│   │   │   │   └── ui/
│   │   │   │       ├── components/
│   │   │   │       │   └── BrutalistComponents.kt # HardShadowCard, StompButton, HazardBar
│   │   │   │       ├── screens/
│   │   │   │       │   ├── BootScreen.kt        # SYS.V2 ACTIVE splash with mascot
│   │   │   │       │   ├── OnboardingScreen.kt  # Onboarding intro & features
│   │   │   │       │   ├── AuthScreen.kt        # Student SSO & credentials login
│   │   │   │       │   ├── HomeScreen.kt        # Alex's Dashboard, Urgent Card, Today's Flow
│   │   │   │       │   ├── QuickCaptureScreen.kt# 6-channel capture hub & smart paste
│   │   │   │       │   ├── CameraCaptureScreen.kt# CameraX viewfinder scanner
│   │   │   │       │   ├── MemoryVaultScreen.kt # 42 stored memories list & search
│   │   │   │       │   ├── CalendarScreen.kt    # Academic deadline timeline
│   │   │   │       │   ├── FocusModeScreen.kt   # Sprint countdown timer
│   │   │   │       │   ├── AssistantScreen.kt   # Ask REM chatbot
│   │   │   │       │   └── ProfileScreen.kt     # Student stats & reminder settings
│   │   │   │       └── theme/
│   │   │   │           ├── Color.kt             # Neo-brutalist palette
│   │   │   │           ├── Theme.kt             # Material 3 setup
│   │   │   │           └── Type.kt              # Space Grotesk / Typography
│   │   │   └── res/
│   │   │       ├── drawable/                    # Custom vector adaptive icons
│   │   │       └── values/strings.xml
│   │   └── test/java/com/example/
│   │       ├── ExampleUnitTest.kt               # Extraction & schema tests
│   │       └── ExampleRobolectricTest.kt        # Context tests
├── ARCHITECTURE.md
├── DATABASE.md
├── AI.md
├── ANDROID.md
├── metadata.json
└── settings.gradle.kts
```

---

## 🛠️ Setup & Build Instructions

### Android Build
1. Open this repository in Android Studio or compile with Gradle CLI.
2. Build debug APK:
   ```bash
   gradle assembleDebug
   ```
3. Run unit & Robolectric tests:
   ```bash
   gradle :app:testDebugUnitTest
   ```

---

## 🔒 Security & Privacy

- **Local-Only Persistence:** SQLite Room database stores all academic data app-privately.
- **Zero API Key Leakage:** No private keys embedded in client source or build artifacts.
- **Camera Privacy:** CameraX previews do not stream video externally; frames are processed on-device.
