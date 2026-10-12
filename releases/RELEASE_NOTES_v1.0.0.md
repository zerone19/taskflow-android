# TaskFlow v1.0.0 — First Release

Android Agent Sandbox — Kotlin + Jetpack Compose + Material 3.

## What's Inside
- Task CRUD (Create, Complete/toggle, Edit, Delete with confirmation)
- Priority (Low / Medium / High) with themed priority chips
- Empty state, Light/Dark theme (follows system)
- Room local persistence, Navigation Compose, manual DI

## Verification (real, not claimed)
- Unit tests: 25/25 passed
- UI tests: 21/21 passed (emulator, Android 17)
- Build: assembleDebug / assembleRelease / bundleRelease SUCCESSFUL

## Downloads
| File | For | Signature |
|------|-----|-----------|
| TaskFlow-v1.0.0-release-unsigned.apk | Direct install / sideload | **Unsigned** |
| TaskFlow-v1.0.0-release.aab | Google Play upload | Unsigned |

## Install the APK
1. Download `TaskFlow-v1.0.0-release-unsigned.apk` to your Android phone
2. Enable "Install unknown apps" for your browser/file manager
3. Open the APK and install
4. Package: `com.taskflow.app` · minSdk 24 (Android 7.0+) · versionName 1.0

> Note: This release APK is **unsigned** — fine for testing/sideloading.
> For Google Play distribution it must be signed with a release keystore
> (keystore is intentionally NOT in this repo).

## Checksums (SHA-256)
```
TaskFlow-v1.0.0-release-unsigned.apk  7ae0543652c0428d226188308797d2af0b952b7507163430d352908a41730a96
TaskFlow-v1.0.0-release.aab           7eb52760a3aebcfac39422a50fc7ae13dfc69fd447906efc121ff180633b8cb7
```
