# API Contract

## Current Status

TaskFlow sandbox tidak membutuhkan backend sebagai baseline awal.

## Rule

Jangan membuat endpoint, authentication flow, atau network dependency hanya karena dianggap mungkin berguna.

Jika tahap pengujian berikutnya membutuhkan backend, API contract harus dibuat sebelum implementasi networking.
