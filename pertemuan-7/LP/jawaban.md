# LP 7: GitHub dan Review

Mata praktikum Rekayasa Perangkat Lunak 2, Pertemuan 7. Jawaban oleh Bima Gunawan (NPM 50423276, 4IA24).

## Soal

![Soal LP 7](soal.jpg)

## Jawaban

**1. Apa yang dimaksud dengan GitHub?**
GitHub adalah layanan Git berbasis cloud (website) untuk menyimpan repository secara gratis dan dapat dipakai lewat tampilan GUI tanpa baris perintah. Fiturnya antara lain branch, pull request, forking repository, dan GitHub Pages.

**2. Sebutkan minimal lima manfaat menggunakan Git bagi seorang developer, beserta penjelasannya!**
1. **Fungsional dan aman**: kinerja, keamanan, dan fleksibilitasnya tinggi untuk mengelola kode.
2. **Riwayat lengkap**: setiap perubahan tercatat, sehingga mudah kembali ke versi sebelumnya.
3. **Populer**: menjadi standar industri, sehingga mudah bekerja dengan tim lain.
4. **Terintegrasi**: terhubung dengan banyak tools dan layanan pihak ketiga.
5. **Open source**: kodenya terbuka dan dikembangkan oleh komunitas luas.
6. **Gratis**: dapat dipakai siapa saja tanpa biaya.

**3. Sebutkan dan jelaskan tiga bagian utama pada proyek Git (Direktori Git, Working Directory, Staging Area)!**
1. **Direktori Git**: menyimpan metadata dan database objek proyek, tempat riwayat commit tersimpan.
2. **Working Directory**: hasil checkout satu versi proyek, tempat berkas diedit.
3. **Staging Area**: berkas yang menyimpan informasi tentang apa saja yang akan masuk ke commit berikutnya.

**4. Apa perbedaan antara perintah git init dan git clone? Berikan contoh penggunaannya!**
`git init` membuat repository baru di folder lokal, dipakai saat memulai proyek dari nol. `git clone` menyalin repository yang sudah ada beserta seluruh riwayatnya, dipakai saat ikut proyek yang sudah ada.

```bash
git init
git clone https://github.com/bimagwn/BimaGunawan.git
```

**5. Jelaskan perbedaan antara git push, git pull, dan git fetch!**
- **git push**: mengirim commit dari repository lokal ke remote (misalnya GitHub).
- **git fetch**: mengambil data terbaru dari remote tanpa menggabungkannya ke pekerjaan lokal.
- **git pull**: fetch lalu langsung merge, sehingga perubahan remote digabung ke direktori kerja.
