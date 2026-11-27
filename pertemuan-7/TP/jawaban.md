# TP 7: GitHub dan Review

Mata praktikum Rekayasa Perangkat Lunak 2, Pertemuan 7. Jawaban oleh Bima Gunawan (NPM 50423276, 4IA24).

## Soal

![Soal TP 7](soal.jpg)

## Jawaban

**1. Jelaskan alur kerja dasar Git mulai dari mengubah file hingga file tersimpan sebagai commit!**
1. **Ubah file** di Working Directory, status berkas menjadi _modified_.
2. **git add**: berkas ditandai ke Staging Area, status menjadi _staged_.
3. **git commit**: snapshot disimpan di database lokal beserta pesan, status menjadi _committed_.

**2. Jelaskan perbedaan istilah Repository, Commit, Clone!**
- **Repository**: folder berisi file dan riwayat perubahan kode proyek.
- **Commit**: rekaman perubahan pada file, yaitu siapa, apa, dan kapan.
- **Clone**: salinan repository di komputer agar dapat diedit secara offline.

**3. Jelaskan perbedaan antara Git dan GitHub!**
**Git** adalah sistem kontrol versi untuk mencatat perubahan kode di komputer lokal. **GitHub** adalah layanan Git berbasis cloud (website) untuk menyimpan repository dan berkolaborasi.

**4. Jelaskan fungsi perintah git config dan berikan contoh penggunaannya!**
`git config` berfungsi mengatur konfigurasi Git, seperti nama, email, algoritma diff, dan format file. Opsi `--global` berlaku untuk semua repository.

```bash
git config --global user.name "Bima Gunawan"
git config --global user.email "email@contoh.com"
```

**5. Jelaskan apa yang dimaksud dengan Version Control System (VCS) dan mengapa tim pengembang software membutuhkannya!**
VCS adalah sistem yang membantu tim mengelola perubahan pada source code dengan mencatat semua modifikasi di database khusus. Tim membutuhkannya karena riwayat perubahan tercatat, mudah kembali ke versi lama saat terjadi error, dan banyak orang dapat bekerja bersama tanpa saling menimpa.
