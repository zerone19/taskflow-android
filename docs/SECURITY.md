# Security

## Rules

- Jangan hardcode API key atau secret.
- Jangan commit credential.
- Jangan log password.
- Jangan log access token atau refresh token.
- Jangan menyimpan password.
- Jangan menonaktifkan TLS/certificate validation.
- Validasi input yang berasal dari user.
- Jangan menambahkan permission Android yang tidak diperlukan.
- Jangan memasukkan secret ke repository Git.

## Destructive Operations

Operasi yang dapat menghapus data, file, database, history Git, atau konfigurasi penting harus diidentifikasi sebelum dijalankan.

Jika operasi destructive membutuhkan keputusan owner, agent harus meminta konfirmasi.
