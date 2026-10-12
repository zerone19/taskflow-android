# Testing Strategy

## Objective

Memastikan agent tidak hanya menghasilkan kode tetapi benar-benar memverifikasi hasil.

## Test Levels

### Unit Test

Uji business logic dan ViewModel yang sesuai.

### UI Test

Uji interaksi penting pada Compose.

### Integration Test

Digunakan jika ada integrasi antar-layer yang perlu diverifikasi.

## Minimum Verification

Setelah perubahan yang relevan:

1. Build project.
2. Jalankan test yang relevan.
3. Jika memungkinkan, jalankan pada device/emulator.
4. Periksa error runtime/logcat jika diperlukan.
5. Laporkan hasil aktual.

## Honesty Rule

Agent tidak boleh mengatakan:

- "build berhasil"
- "test berhasil"
- "aplikasi sudah diverifikasi"

jika command atau verification tersebut belum benar-benar dilakukan.
