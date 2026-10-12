# Arona — Jawaban Proposal (Tanpa Mengubah File Repo)

> Deliverable resmi untuk TEST 03. Dokumen ini TIDAK mengubah file proyek — hanya laporan proposal.
> Dibuat oleh **Arona Arcanova** (Chain Caster, Android Software Engineer) pada 2026-10-12.

---

## Ringkasan Keputusan

Proposal ini **mengakomodasi gap desain vs. spec** yang ditemukan di TEST 01/02 dengan strategi
**"implementasi lean, siap ekstensi"**: semua entitas/kontrak di bawah mendukung field desain yang
lebih kaya (due date, tags, dsb.) sebagai field opsional, tapi **UI v1 hanya me-render yang
diwajibkan requirements**. Tidak ada tabel Room tambahan, tidak ada dependency tambahan.

---

## 1. Package Structure

```
com.taskflow.app
├── MainActivity.kt                    # setContent { TaskFlowTheme { NavGraph } }
├── di/                                # Dependency Injection (manual, tanpa Hilt)
│   ├── AppContainer.kt                # interface container (repository)
│   └── DefaultAppContainer.kt         # implementasi manual DI
├── data/
│   ├── model/
│   │   ├── Task.kt                    # data class Task + Priority enum
│   │   └── Result.kt                  # sealed Result<T> (Loading/Success/Error)
│   ├── local/
│   │   ├── TaskDao.kt                 # @Dao Flow<List<TaskEntity>>
│   │   ├── TaskDatabase.kt            # @Database(version=1)
│   │   └── TaskEntity.kt              # @Entity, mapper to/from Task
│   └── repository/
│       ├── TaskRepository.kt          # interface (abstraksi)
│       └── DefaultTaskRepository.kt   # impl: DAO + mapping
├── ui/
│   ├── theme/                         # Color, Type, Shape, Theme (Material 3)
│   ├── navigation/
│   │   ├── TaskFlowNavHost.kt         # composable NavHost + routes
│   │   └── Routes.kt                  # object Routes { const val ... }
│   ├── component/                     # reusable composables
│   │   ├── TaskCard.kt                # ElevatedCard + checkbox + priority chip
│   │   ├── PriorityChip.kt            # pill badge warna + ikon + label
│   │   ├── EmptyState.kt              # orb + headline + CTA + quick starters
│   │   ├── FilterChipsRow.kt          # All/Pending/High/Completed
│   │   ├── ConfirmDialog.kt           # M3 AlertDialog 28dp untuk delete
│   │   └── AdaptiveScaffold.kt        # helper breakpoint 600dp (dual-pane)
│   ├── home/
│   │   ├── HomeScreen.kt              # stateless composable
│   │   └── HomeViewModel.kt           # StateFlow<HomeUiState>
│   ├── detail/
│   │   ├── TaskDetailScreen.kt
│   │   └── TaskDetailViewModel.kt
│   └── form/
│       ├── TaskFormScreen.kt          # dipakai add & edit
│       └── TaskFormViewModel.kt
└── util/
    ├── Extensions.kt                  # mapper helper, formatter tanggal
    └── WindowSize.kt                  # enum WindowSize + calculation helper
```

**Alasan**: repo "mungil tapi profesional". Tidak ada UseCase layer (justifikasi: tidak ada business logic kompleks — hanya CRUD sederhana). Tidak ada Hilt (justifikasi: proyek sandbox; manual DI via AppContainer cukup dan mengurangi 1 dependency). Foldering feature-based (`ui/home`, `ui/detail`, `ui/form`) bukan layer-based agar setiap screen punya ruang yang jelas.

---

## 2. Layer Mapping

```
HomeScreen / TaskDetailScreen / TaskFormScreen (Compose, stateless)
        ↓ event ↑ state
HomeViewModel / TaskDetailViewModel / TaskFormViewModel
        ↓ call
TaskRepository (interface, abstraksi data)
        ↓
TaskDao (Room, Flow)
        ↓
SQLite (Room database)
```

### Tanggung jawab
- **UI**: render `UiState`, kirim aksi user via callback. TIDAK boleh akses DAO/repo langsung.
- **ViewModel**: pegang `StateFlow<UiState>`, handle event UI, panggil repository, survive config change.
- **Repository**: abstraksi data; mengoordinasikan sumber lokal (Room). Tidak ada remote (API_CONTRACT.md: tidak ada backend).
- **Data source**: Room DAO — satu-satunya sumber data.

---

## 3. Navigation

- **Jetpack Navigation Compose**, tanpa library tambahan.
- Routes:
  - `home`
  - `task_detail/{taskId}` (arg: Long)
  - `task_form?taskId={taskId}` (arg opsional; `taskId` ada = edit, kosong = add)

- **Pass ID saja**, bukan objek Task penuh (sesuai ARCHITECTURE.md dan Core priority #17 Arona).
- TaskFormScreen dipakai bersama (add & edit) untuk menghindari duplikasi form.
- Bottom navigation (Sprints/Board/Backlog/Analytics/Settings dari desain) **TIDAK diimplementasi di v1** — desain punya 5 tab tapi requirements hanya menyebut Home + Add + Detail. Bottom nav tidak relevan untuk flow evaluasi. **Ini adalah keputusan scope, bukan kelalaian.** Bila Master ingin, bisa ditambahkan.

---

## 4. State Management

```kotlin
// Contoh HomeViewModel
sealed interface HomeUiState {
    data object Loading : HomeUiState
    data object Empty : HomeUiState
    data class Success(
        val tasks: List<Task> = emptyList(),
        val filter: TaskFilter = TaskFilter.ALL
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
```

- ViewModel expose `StateFlow<UiState>`, UI koleksi via `collectAsStateWithLifecycle()`.
- **State hoisting**: screen composable stateless, semua event lewat callback lambda.
- Filter dipertahankan di `Success` (bukan state terpisah) agar UI selalu konsisten.
- **Tidak ada global mutable state** — semua state lokal ViewModel.

---

## 5. Data Layer

```kotlin
data class Task(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
    // Field opsional untuk masa depan (tidak divalidasi/dirender di v1):
    // val dueDate: Long? = null,
    // val tags: List<String> = emptyList(),
    // val assignee: String? = null,
    // val storyPoints: Int? = null,
)

enum class Priority { LOW, MEDIUM, HIGH }
```

- Room `TaskEntity` punya field yang sama. DAO expose `Flow<List<TaskEntity>>`.
- Repository: mapping Entity → Domain via extension function di `util/Extensions.kt`.
- Tidak ada database versioning kompleks — cukup `version = 1`, tanpa migration.
- **Persistence**: Room (justifikasi: TEST 09 butuh repository test + TEST 04 butuh daftar task yang bertahan antar sesi; in-memory list tidak realistis untuk evaluasi "Local persistence" di AGENT_EVALUATION.md).

---

## 6. Testing Strategy

| Level | Cakupan | Framework |
|-------|---------|-----------|
| Unit | ViewModel logic (state transition, filter, error handling) | JUnit 4 + Turbine + kotlinx-coroutines-test |
| Integration | Repository + DAO (Room in-memory) | JUnit 4 + Room in-memory builder |
| UI (Compose) | HomeScreen list, empty state, add task flow, delete dialog | Compose UI Test + createAndroidComposeRule |

**Catatan koreksi dari progress.md**: Proposal sebelumnya menyebut JUnit 5, tapi ekosistem Android modern (AGP 8.x, Compose Testing) paling kompatibel dengan **JUnit 4** sebagai baseline. Turbine untuk Flow testing, `kotlinx-coroutines-test` untuk ViewModel. JUnit 5 bisa ditambahkan jika memang diperlukan, tapi tidak perlu untuk sandbox ini.

---

## 7. Dependencies

```toml
# Hanya yang terjustifikasi oleh requirements/testing:
androidx.core:core-ktx
androidx.lifecycle:lifecycle-runtime-ktx
androidx.lifecycle:lifecycle-viewmodel-compose   # ViewModel + collectAsStateWithLifecycle
androidx.activity:activity-compose
androidx.compose:compose-bom                      # kelola versi Compose
androidx.compose.ui:ui, ui-graphics, ui-tooling-preview
androidx.compose.material3:material3              # Material 3
androidx.navigation:navigation-compose            # Navigation
androidx.room:room-runtime, room-ktx, room-compiler # Local persistence
io.insert-koin:koin-... ❌ TIDAK ADA
```

**Tidak ada**: Hilt/Koin, Retrofit, OkHttp, DataStore (tidak ada settings yang perlu disimpan), Coil/Glide (tidak ada gambar di UI selain ikon built-in), kotlinx-serialization (tidak ada JSON — tanpa backend).

**Alasan**: API_CONTRACT.md eksplisit bilang jangan bikin endpoint/network dependency. PROJECT_CONTEXT.md bilang "sandbox kecil, jangan tambah complexity tanpa alasan evaluasi".

---

## 8. Design System (diambil dari `design/taskflow/DESIGN.md`)

### Warna (sudah lengkap di DESIGN.md — token resmi)
- Primary: `#3525cd` | on-primary: `#ffffff`
- Primary Container: `#4f46e5` | on-primary-container: `#dad7ff`
- Secondary: `#006a61` | on-secondary: `#ffffff`
- Secondary Container: `#86f2e4`
- Tertiary: `#004c76` | on-tertiary: `#ffffff`
- Error: `#ba1a1a` | on-error: `#ffffff`
- Error Container: `#ffdad6`
- Background: `#faf8ff` | on-background: `#131b2e`
- Surface: `#ffffff` | on-surface: `#131b2e`
- Surface Variant: `#dae2fd` | on-surface-variant: `#464555`
- Outline: `#777587` | outline-variant: `#c7c4d8`
- Surface Container Low: `#f2f3ff` | Container: `#eaedff` | High: `#e2e7ff` | Highest: `#dae2fd`
- Priority (bukan dari token M3, dari section "Priority and Semantic Roles"):
  - High: `#EF4444` + ikon `priority_high` + teks "P0 - High"
  - Medium: `#F59E0B` + ikon `remove` + teks "P1 - Medium"
  - Low: `#10B981` + ikon `keyboard_arrow_down` + teks "P2 - Low"

**⚠️ CATATAN KONFLIK**: Section "Surface Elevation System" di DESIGN.md (baris 156-168) menyebut light mode `background: #F8FAFC, surface: #FFFFFF` — sedikit berbeda dari frontmatter YAML (`surface: #faf8ff`). **Aku pilih frontmatter YAML sebagai sumber kebenaran** karena itu adalah token eksplisit dalam format machine-readable, dan progress.md TEST 02 sudah mereferensikan `#faf8ff`. Ini adalah keputusan minor yang perlu dicatat; bukan konflik desain yang material.

### Tipografi — Inter family
| Level | Ukuran | Weight | Line height | Tracking |
|-------|--------|--------|-------------|----------|
| display-lg | 40px | 700 | 48px | -0.02em |
| display-lg-mobile | 32px | 700 | 40px | -0.02em |
| headline-md | 28px | 600 | 36px | -0.01em |
| headline-md-mobile | 24px | 600 | 32px | -0.01em |
| title-lg | 22px | 600 | 28px | — |
| title-md | 16px | 600 | 24px | +0.01em |
| title-sm | 14px | 600 | 20px | +0.01em |
| body-lg | 16px | 400 | 24px | +0.01em |
| body-md | 14px | 400 | 20px | +0.015em |
| body-sm | 12px | 400 | 16px | +0.02em |
| label-lg | 14px | 500 | 20px | +0.01em |
| label-md | 12px | 500 | 16px | +0.02em |
| label-sm | 11px | 500 | 16px | +0.03em |

**Implementasi**: font Inter perlu ditambahkan ke `res/font/` (download dari Google Fonts) atau gunakan `FontFamily.Default` sebagai fallback jika file tidak tersedia. Aku akan sertakan file font Inter jika Master bisa menyediakannya, atau gunakan system font sebagai fallback dan catat sebagai ambiguitas.

### Spacing — 4/8dp grid
| Token | Nilai |
|-------|-------|
| space-xs | 4dp |
| space-sm | 8dp |
| space-md | 16dp |
| space-lg | 24dp |
| space-xl | 32dp |
| margin (mobile) | 16dp |
| margin-tablet | 24dp |
| gutter-mobile | 12dp |

### Shape (Material 3 corner scale)
| Level | Nilai | Penggunaan |
|-------|-------|------------|
| Extra Small | 4dp | status indicator, badge kecil |
| Small | 8dp | text field, chip, dropdown |
| Medium | 12-16dp | ElevatedCard, bottom sheet, snackbar |
| Large | 24-28dp | FAB, search bar, AlertDialog |
| Full | 9999dp (pill) | priority badge, avatar |

### Elevation
- Level 0: flat background (0dp)
- Level 1: `surface-container-low` + shadow `0dp 1dp 3dp rgba(15,23,42,0.08)`
- Level 2: `surface-container` + border `1dp rgba(226,232,240,0.8)`
- Level 3: `surface-container-high` + shadow `0dp 4dp 12dp rgba(79,70,229,0.18)` (FAB, modal)

### Responsive
- <600dp: single column, margin 16dp, bottom sheet untuk modal
- 600–840dp+: dual-pane (list 320dp + detail), margin 24dp

---

## 9. Ambiguitas yang Perlu Keputusan Master

| # | Isu | Rekomendasi Arona |
|---|-----|-------------------|
| 1 | Bottom nav (5 tab) ada di desain tapi tidak di requirements | Skip di v1, tambahkan jika diminta |
| 2 | Field desain kaya (due date, tags, assignee, sprint) vs requirements dasar | Entity siap terima field opsional, UI v1 hanya render yang diwajibkan |
| 3 | Font Inter file .ttf tidak tersedia di repo | Gunakan system font fallback + catat ambiguitas, atau Master sediakan file |
| 4 | Surface elevation light mode (#F8FAFC) vs YAML frontmatter (#faf8ff) | Ikuti frontmatter YAML (#faf8ff) sebagai sumber kebenaran |
| 5 | Dark mode: frontmatter YAML tidak punya token dark, section prose punya | Gunakan token YAML sebagai dasar, dark mode follow platform system |

---

## 10. Ringkasan Verifikasi

| Item | Status |
|------|--------|
| File diubah | TIDAK — laporan saja |
| Build dijalankan | TIDAK (tahap proposal, belum ada kode) |
| Test dijalankan | TIDAK (tahap proposal) |
| Klaim verifikasi | Semua di atas adalah proposal, bukan hasil eksekusi |

---

*Dibuat oleh Arona Arcanova — Chain Caster, Project Recovery.*
*Prinsip: Understand → Inspect → Plan → Implement → Build → Test → Verify → Report.*
