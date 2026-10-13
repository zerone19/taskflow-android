# TaskFlow — Android Agent Sandbox

<p align="center">
  <img src="logo.png" alt="TaskFlow Logo" width="120" />
</p>

<p align="center">
  Aplikasi Android native (task manager sederhana) yang sekaligus dipakai sebagai
  <strong>sandbox evaluasi kemampuan AI agent</strong> dalam pengembangan Android.
</p>

<p align="center">
  Kotlin + Jetpack Compose + Material 3 · MVVM · Room · offline-first
</p>

Dibangun dengan **Kotlin + Jetpack Compose + Material 3**, arsitektur berlapis
(UI → ViewModel → Repository → Room), dan 100% offline (persistensi lokal).

---

## Fitur

- Create / Edit / Delete task (delete dengan dialog konfirmasi)
- Tandai task selesai (toggle complete)
- Prioritas: Low / Medium / High (dengan priority chip bertema)
- Empty state dengan logo
- Tema Light & Dark (mengikuti sistem)
- Persistensi lokal (Room)
- Launcher icon adaptive (vector) + logo di dalam aplikasi

## Tech Stack

| Area | Teknologi |
|------|-----------|
| Bahasa | Kotlin 2.1.20 |
| UI | Jetpack Compose + Material 3 |
| Arsitektur | MVVM (UI → ViewModel → Repository → Room) |
| State | StateFlow + sealed UiState |
| Navigasi | Navigation Compose |
| Persistensi | Room (KSP) |
| DI | Manual (AppContainer) |
| Build | Gradle 8.11.1 + AGP 8.9.1, Version Catalog |
| Min SDK | 24 (Android 7.0+) · Target/Compile SDK 36 |

## Struktur Project

```
app/src/main/java/com/taskflow/app/
├── MainActivity.kt
├── di/AppContainer.kt              # Manual DI
├── data/
│   ├── model/Task.kt               # Domain model + Priority enum
│   ├── local/                      # Room: TaskDao, TaskDatabase, TaskEntity + mappers
│   └── repository/                 # TaskRepository + DefaultTaskRepository
└── ui/
    ├── theme/                      # Color, Type, Shape, Spacing (design tokens)
    ├── navigation/                 # Routes + NavHost
    ├── component/                  # TaskCard, PriorityChip, EmptyState
    ├── home/                       # HomeScreen + HomeViewModel
    ├── detail/                     # TaskDetailScreen + ViewModel
    └── form/                       # TaskFormScreen + ViewModel (add/edit)
```

## Build & Test

```bash
# Unit test (25 test)
./gradlew testDebugUnitTest

# UI test (21 test, butuh emulator/device)
./gradlew connectedDebugAndroidTest

# Build APK debug (auto-signed dengan debug keystore)
./gradlew assembleDebug

# Build release APK + AAB (butuh keystore.properties — lihat di bawah)
./gradlew assembleRelease bundleRelease
```

## Install APK Release

Unduh dari [Releases](https://github.com/zerone19/taskflow-android/releases) —
file `TaskFlow-v*.apk` (bukan `.aab`; AAB hanya untuk Google Play).

1. Transfer APK ke HP Android
2. Aktifkan "Install unknown apps" untuk browser/file manager
3. Buka APK → install

> APK release **sudah ditandatangani** dengan release keystore, jadi bisa
> langsung di-install dan bisa di-update antar versi (asalkan tetap ditandatangani
> dengan keystore yang sama).

## Signing & CI

- Release APK/AAB ditandatangani via `keystore.properties` di root project
  (di-gitignore, **jangan pernah di-commit**). File keystore disimpan di luar repo.
- Workflow CI (`.github/workflows/release-apk.yml`) otomatis build + sign +
  upload APK & AAB ke GitHub Release setiap kali push tag `v*`:
  ```bash
  git tag v1.0.2
  git push origin v1.0.2
  ```
  Kredensial signing di-set sebagai repo secrets (`ANDROID_KEYSTORE_*`).


## Verifikasi

- Unit test: **25/25 lulus**
- UI test: **21/21 lulus** (emulator, Android 17)
- Semua klaim verifikasi di `progress.md` berbasis eksekusi nyata, bukan asumsi.

## Tujuan

Project ini bukan produk final. Tujuan utamanya adalah mengevaluasi apakah agent mampu:

- memahami project Android yang sudah ada;
- mempelajari desain yang diberikan;
- merancang arsitektur sebelum coding;
- mengimplementasikan Jetpack Compose;
- menggunakan Gradle dan Android SDK dengan benar;
- melakukan build, test, install, dan debugging;
- menjaga scope perubahan;
- memverifikasi hasil sebelum menyatakan pekerjaan selesai.

## Prinsip

> Understand → Inspect → Plan → Implement → Build → Test → Verify → Report

Agent tidak boleh mengarang requirement, endpoint, desain, atau hasil verifikasi.

## Status

Sandbox / Agent Evaluation Project — semua checkpoint TEST 01–11 selesai,
aplikasi rilis **v1.0.0**.

## Dokumentasi & Riwayat

- [`progress.md`](progress.md) — tracker lengkap TEST 01–11, audit, & release
- [`docs/`](docs/) — requirement, architecture, design system, laporan evaluasi
- [`design/`](design/) — referensi desain (DESIGN.md + HTML + app icon)
- [Releases](https://github.com/zerone19/taskflow-android/releases) — APK & AAB v1.0.0
