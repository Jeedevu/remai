# SETUP & DEPLOYMENT GUIDE — REM

## 1. Prerequisites
- Android Studio / Android SDK 36 (JDK 17 or JDK 21)
- Node.js (v18 or v20 LTS) & npm for Backend AI Gateway

---

## 2. Backend Setup (NVIDIA NIM AI Gateway)
The backend AI gateway decouples the mobile client from the NVIDIA NIM service so secrets are never exposed on client devices.

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Configure environment variables:
   ```bash
   cp .env.example .env
   ```
   Open `.env` and enter your NVIDIA NIM API key:
   ```ini
   NVIDIA_API_KEY=nvapi-YOUR_KEY_HERE
   NVIDIA_BASE_URL=https://integrate.api.nvidia.com/v1
   NVIDIA_MODEL=meta/llama-3.2-11b-vision-instruct
   NVIDIA_TEXT_MODEL=meta/llama-3.1-70b-instruct
   AI_PROVIDER=nvidia
   PORT=3000
   ```
   *(Note: To test without an NVIDIA key, set `AI_PROVIDER=mock`)*.

4. Start development server:
   ```bash
   npm run dev
   ```
   The gateway exposes:
   - `GET /health`
   - `POST /api/ai/analyze/text`
   - `POST /api/ai/analyze/image`
   - `POST /api/ai/analyze/document`
   - `POST /api/ai/chat`

---

## 3. Android Setup & Build
1. Open the project root in Android Studio or compile with Gradle:
   ```bash
   gradle assembleDebug
   ```
2. Run automated unit & Robolectric tests:
   ```bash
   gradle :app:testDebugUnitTest
   ```

---

## 4. Permissions & System Access
The application declares and checks the following system permissions at runtime:
- **`CAMERA`**: Enables the CameraX viewfinder reticle to scan syllabi, whiteboards, and assignment handouts.
- **`RECORD_AUDIO`**: Allows recording quick stream-of-consciousness voice notes.
- **`POST_NOTIFICATIONS`**: Android 13+ permission for urgent 48h deadline alerts and daily flow briefs.
- **`VIBRATE`**: Tactile feedback on alert triggers and focus sprint completion.
- **Sharesheet `ACTION_SEND`**: Listens for incoming images, PDFs, and notes from WhatsApp, Chrome, or Canvas.
