# Database Specification & Schema — REM

## 1. Database Configuration
- **Engine:** Room 2.7.0 (SQLite)
- **Database File:** `rem_memory_vault.db`
- **Compiler:** KSP (Kotlin Symbol Processing)

---

## 2. Entity Schemas

### `memories` Table
Stores ingested student artifacts, extracted syllabus schedules, notes, and requirements.

| Column | Type | Description |
|---|---|---|
| `id` | `INTEGER PRIMARY KEY AUTOINCREMENT` | Unique memory identifier |
| `title` | `TEXT` | Short descriptive title of the memory |
| `summary` | `TEXT` | AI-generated or raw user summary |
| `originalText` | `TEXT` | Unprocessed input content |
| `category` | `TEXT` | `"assignments"`, `"exams"`, `"projects"`, `"notes"` |
| `sourceType` | `TEXT` | `"screenshot"`, `"pdf"`, `"voice"`, `"text"`, `"link"`, `"photo"` |
| `course` | `TEXT` | Academic course (e.g. `"Physics 101"`, `"CS 101"`) |
| `professor` | `TEXT` | Course instructor name |
| `deadline` | `TEXT` | Human-readable deadline string |
| `deadlineEpochMillis` | `INTEGER` | Normalized epoch timestamp for calendar sorting |
| `importance` | `TEXT` | `"critical"`, `"high"`, `"medium"`, `"low"` |
| `isStarred` | `INTEGER` (Boolean) | Starred indicator |
| `isArchived` | `INTEGER` (Boolean) | Soft deletion / archival state |
| `aiConfidence` | `INTEGER` | Extraction confidence score (e.g. 98) |
| `sourceDetail` | `TEXT` | Provenance label (e.g. `"Screenshot parsed · 98% AI conf."`) |
| `createdAt` | `INTEGER` | System epoch timestamp |

---

### `tasks` Table
Actionable to-dos generated directly from memories or manually created.

| Column | Type | Description |
|---|---|---|
| `id` | `INTEGER PRIMARY KEY AUTOINCREMENT` | Unique task ID |
| `memoryId` | `INTEGER NULL` | Foreign link to source memory |
| `title` | `TEXT` | Task title |
| `subtitle` | `TEXT` | Associated course, slot, or due date |
| `estimatedMinutes` | `INTEGER` | Estimated study/effort duration in minutes |
| `priority` | `TEXT` | `"CRITICAL"`, `"HIGH PRIORITY"`, `"MED PRIORITY"` |
| `status` | `TEXT` | `"TODO"`, `"IN_PROGRESS"`, `"COMPLETED"`, `"SNOOZED"` |
| `isCompleted` | `INTEGER` (Boolean) | Checkbox state |
| `dueDisplay` | `TEXT` | Display date string |
| `recommendedSlot` | `TEXT` | Neural Radar recommended focus time slot |
| `isNext` | `INTEGER` (Boolean) | Active task marker |
| `orderIndex` | `INTEGER` | Sorting sequence order |
| `createdAt` | `INTEGER` | Timestamp |

---

### `chat_messages` Table
Conversation history between the student and the local REM AI assistant.

| Column | Type | Description |
|---|---|---|
| `id` | `INTEGER PRIMARY KEY AUTOINCREMENT` | Chat message identifier |
| `sender` | `TEXT` | `"user"` or `"assistant"` |
| `text` | `TEXT` | Content of message |
| `provenance` | `TEXT NULL` | Citation tag referencing the source memory |
| `timestamp` | `INTEGER` | Time sent |

---

## 3. Data Integrity & Migration Strategy
- Destructive migration fallback enabled during MVP iteration.
- Autonomous pre-seeding pipeline initializes the 42 memories and active tasks on first run if the database is unpopulated.
