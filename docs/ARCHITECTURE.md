# Architecture

## Direction

Gunakan arsitektur sederhana dan maintainable.

```text
Compose UI
    ↓
ViewModel
    ↓
UseCase (jika memang diperlukan)
    ↓
Repository
    ↓
Data Source
```

## Responsibilities

### UI

- Menampilkan state.
- Mengirim user action.
- Tidak menjalankan business logic atau database operation secara langsung.

### ViewModel

- Mengelola screen state.
- Menangani event dari UI.
- Berkomunikasi dengan repository/use case.
- Tidak bergantung langsung pada implementasi UI.

### Repository

- Menjadi abstraction untuk data.
- Mengkoordinasikan local/remote data source jika diperlukan.

### Data Source

Menyediakan data dari sumber yang sesuai.

## Architecture Rules

- Jangan membuat Clean Architecture berlebihan untuk kebutuhan sandbox.
- Jangan membuat layer hanya demi jumlah folder.
- Dependency harus mengalir ke arah yang masuk akal.
- State harus eksplisit.
- Gunakan immutable state bila memungkinkan.
- Hindari global mutable state.
- Hindari giant classes dan giant functions.
