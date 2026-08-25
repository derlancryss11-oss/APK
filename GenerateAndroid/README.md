# Generate Android

Project Android native yang meniru tampilan aplikasi pada screenshot.

Fitur: Hari, Bulan, Tahun, Device/Key, CONVERT, COPY KEY, RESET.

## Membuat APK
Buka project ini di Android Studio lalu Build > Build APK(s).

Atau push ke GitHub dan jalankan workflow **Build Android APK**. APK hasil build akan tersedia sebagai artifact.

### Catatan converter
Screenshot tidak memberikan algoritma converter asli. Fungsi `makeKey()` saat ini memakai SHA-256 deterministik sebagai placeholder. Jika tersedia APK/source/contoh input-output dari aplikasi asli, fungsi ini bisa diganti agar hasilnya sama persis.
