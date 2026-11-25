# LP 7: GitHub dan Review

Mata praktikum Rekayasa Perangkat Lunak 2, Pertemuan 7. Jawaban oleh Bima Gunawan (NPM 50423276, 4IA24).

## Soal

![Soal LP 7](soal.jpg)

## Jawaban

**1. Apa yang dimaksud dengan GitHub?**
GitHub adalah layanan berbasis web untuk menyimpan repositori Git secara daring dan berkolaborasi dalam pengembangan kode.

**2. Sebutkan minimal lima manfaat menggunakan Git bagi seorang developer, beserta penjelasannya!**
1. **Riwayat perubahan**: setiap perubahan tercatat sebagai _commit_.
2. **Kembali ke versi lama**: kode dapat dikembalikan bila terjadi kesalahan.
3. **Branching**: fitur baru dikerjakan terpisah tanpa mengganggu kode utama.
4. **Kolaborasi**: banyak developer dapat bekerja pada proyek yang sama.
5. **Cadangan kode**: kode tersimpan aman di server jarak jauh.

**3. Sebutkan dan jelaskan tiga bagian utama pada proyek Git (Direktori Git, Working Directory, Staging Area)!**
1. **Direktori Git**: folder `.git` tempat menyimpan riwayat _commit_ proyek.
2. **Working Directory**: folder berisi berkas yang sedang diedit.
3. **Staging Area**: tempat perubahan yang siap di-_commit_ (diisi dengan `git add`).

**4. Apa perbedaan antara perintah git init dan git clone? Berikan contoh penggunaannya!**
`git init` membuat repositori baru yang kosong, sedangkan `git clone` menyalin repositori yang sudah ada dari _remote_.

```bash
git init
git clone https://github.com/bimagwn/BimaGunawan.git
```

**5. Jelaskan perbedaan antara git push, git pull, dan git fetch!**
- **git push**: mengirim _commit_ lokal ke _remote_.
- **git pull**: mengambil perubahan dari _remote_ lalu langsung menggabungkannya.
- **git fetch**: mengambil perubahan dari _remote_ tanpa menggabungkannya.
