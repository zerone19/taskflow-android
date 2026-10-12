# Feature Specification

## Feature: Task Management

### Create Task

Input:
- Title
- Description
- Priority

Expected behavior:
1. User membuka Add Task.
2. User mengisi data.
3. Sistem memvalidasi input.
4. Task disimpan.
5. User kembali ke screen yang sesuai.
6. Daftar task diperbarui.

### Complete Task

1. User membuka task.
2. User memilih complete.
3. Status task berubah menjadi completed.
4. UI memperbarui state.

### Edit Task

1. User membuka task.
2. User memilih edit.
3. Form menampilkan data saat ini.
4. User mengubah data.
5. Sistem menyimpan perubahan.
6. UI memperbarui state.

### Delete Task

1. User memilih delete.
2. Sistem menampilkan konfirmasi bila diperlukan.
3. Setelah dikonfirmasi, task dihapus.
4. UI memperbarui state.

## Acceptance Principle

Feature dianggap selesai hanya setelah implementasi berhasil dibuild dan pengujian yang relevan telah dijalankan.
