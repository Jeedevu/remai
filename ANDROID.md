# Android Native Integration — REM

## 1. System Integration Overview
REM leverages modern Android platform capabilities while remaining battery-friendly and non-invasive.

---

## 2. CameraX Viewfinder (`CameraCaptureScreen.kt`)
- **Lifecycle Integration:** Binds `PreviewView` with `ProcessCameraProvider` to `LocalLifecycleOwner.current`.
- **Runtime Permissions:** Uses Jetpack Compose `rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission())`.
- **ImageCapture Pipeline:** Writes output files directly to private cache storage (`context.cacheDir`) with timestamped filenames.
- **Hardware Fallback:** On emulators or devices without physical camera sensors, provides quick-selection syllabus presets (*Physics 101*, *CS 101*, *Chemistry Lab*, *Maths Midterm*) to test the extraction pipeline.

---

## 3. Sharesheet Receiver (`AndroidManifest.xml`)
Registered intent filters for inbound sharing:
```xml
<intent-filter>
    <action android:name="android.intent.action.SEND" />
    <category android:name="android.intent.category.DEFAULT" />
    <data android:mimeType="text/plain" />
    <data android:mimeType="image/*" />
    <data android:mimeType="application/pdf" />
</intent-filter>
<intent-filter>
    <action android:name="android.intent.action.SEND_MULTIPLE" />
    <category android:name="android.intent.category.DEFAULT" />
    <data android:mimeType="image/*" />
</intent-filter>
```
When triggered from Chrome, Canvas, or messaging apps, `MainActivity.kt` detects the incoming payload and immediately opens `AppScreen.QUICK_CAPTURE`.

---

## 4. Permissions & Hardware Access
- `INTERNET`: For potential gateway sync and AI models.
- `CAMERA`: For scanning physical syllabus handouts and whiteboard equations.
- `RECORD_AUDIO`: For recording quick voice notes and reminders.
- `POST_NOTIFICATIONS`: Android 13+ permission for urgent deadline notifications and daily flow briefs.
- `VIBRATE`: Tactile feedback on deadline alerts and focus sprint completion.
