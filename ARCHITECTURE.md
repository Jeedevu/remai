# Architecture & System Design — REM

## 1. Architectural Philosophy
REM adheres to a **Local-First, Reactive Unidirectional Data Flow (UDF)** with Clean MVVM principles.

```
Incoming Sources (CameraX / Sharesheet / Voice / Text)
                     │
                     ▼
             RemAiEngine (Extraction & Normalization)
                     │
                     ▼
             ExtractionResult (User Review & Verification)
                     │
                     ▼
             RemRepository (Persistence Coordinator)
                     │
                     ▼
             Room SQLite Database (Local Single Source of Truth)
                     │
                     ▼
             Kotlin Coroutines Flow (Reactive State Stream)
                     │
                     ▼
             Jetpack Compose UI (Screens & Neo-Brutalist Components)
```

## 2. Navigation Architecture
- **Root Screen State Management:** Centralized via `AppScreen` in `MainActivity.kt`.
- **Hardware Back Handling:** Full `BackHandler` support ensures intuitive step-back across nested sub-screens (`CAMERA_SCAN`, `FOCUS_MODE`, `ASSISTANT`) and bottom tabs (`MEMORY`, `CALENDAR`, `PROFILE` -> `HOME`).
- **Intent Deep-Linking:** Sharesheet payloads (`Intent.ACTION_SEND` and `ACTION_SEND_MULTIPLE`) pre-populate the ingestion pipeline immediately upon app entry.

## 3. UI System & Design Tokens
- **Theme Color Palette:**
  - `RemPrimaryContainer`: `#FFC700` (Electric Yellow)
  - `RemSecondaryContainer`: `#FE6A34` (Tangerine)
  - `RemInkBlack`: `#1C1B1B` (Pitch Ink outline & shadow)
  - `RemSurface`: `#FCF9F8` (Cream uncoated paper)
- **Hard Shadows & Tactical Borders:**
  - Custom `Modifier` and `HardShadowCard` wrappers create physical tactile depth (3dp–5dp hard shadow offsets).
  - Dynamic `InteractionSource` depression simulates arcade-style physical button travel.
- **Segmented Hazard Progress:**
  - Canvas drawing paths render diagonal warning stripes across active velocity bars.

## 4. Concurrency & Asynchronous Streams
- Database reads return continuous `Flow<List<T>>` emitting on Room's background executor.
- Coroutines scope bounded to `Dispatchers.IO` for heavy extraction, and UI state collected via `collectAsState()`.
