# AI Engine & Extraction Pipeline — REM

## 1. Overview
The REM AI Ingestion Engine (`RemAiEngine.kt`) converts unstructured chaos (screenshots, camera photos, syllabus snippets, voice dictation, URLs) into normalized, actionable memories and tasks.

---

## 2. Ingestion & Extraction Architecture

```
[Raw Input] ──► [Normalizer] ──► [Entity Classification] ──► [Schema Validation] ──► [Extraction Review]
```

### Extraction Schema
The engine outputs structured `ExtractionResult`:
```kotlin
data class ExtractionResult(
    val title: String,
    val summary: String,
    val course: String,
    val professor: String,
    val deadline: String,
    val priority: String,
    val category: String,
    val estimatedMinutes: Int,
    val confidence: Int,
    val candidateTasks: List<String>
)
```

---

## 3. Heuristic Rules & Safety Guardrails
- **No Hallucinations:** When information is ambiguous, relative dates are normalized against device time or flagged for user review.
- **Fact vs. Interpretation Separation:** Every memory card highlights its origin (e.g. `Syllabus.pdf (Page 4)`, `Voice Memo (0:45s)`, `Screenshot parsed · 98% AI conf.`).
- **User Confirmation:** Extracted entities are presented in an interactive dialog before committing to the Room database.

---

## 4. NVIDIA NIM & Gateway Integration Architecture
For production cloud AI enhancement:
- The mobile client interacts with an AI Gateway abstraction rather than embedding private keys.
- Gateway endpoint schema:
  - `POST /ai/analyze/text`
  - `POST /ai/analyze/image`
  - `POST /ai/transcribe`
- Client validates all responses against the local schema before insertion into SQLite.
