# Requirements

## Functional Requirements

### Home

- Menampilkan daftar task.
- Menampilkan status task.
- Menampilkan empty state ketika belum ada task.
- User dapat membuka detail task.
- User dapat menuju form pembuatan task.

### Add Task

- User dapat memasukkan title.
- User dapat memasukkan description.
- User dapat memilih priority.
- User dapat menyimpan task.
- Input harus divalidasi.

### Task Detail

- Menampilkan detail task.
- User dapat menandai task sebagai selesai.
- User dapat mengedit task.
- User dapat menghapus task.
- Operasi destructive harus diperlakukan hati-hati.

## UI States

Screen yang relevan harus mempertimbangkan:

- Loading
- Success
- Empty
- Error
- Offline jika persistence/network behavior membutuhkannya

## Constraints

- Gunakan Kotlin.
- Gunakan Jetpack Compose untuk UI baru.
- Gunakan Material 3.
- Jangan membuat dependency yang tidak diperlukan.
- Jangan mengubah konfigurasi Gradle secara sembarangan.
- Jangan mengarang requirement yang belum ditentukan.
