-- Basis data RamenKu - tiga fitur: Menu dan Resep, Stok Bahan, Penjualan
-- Dipakai oleh JDBC (Pertemuan 3) dan Hibernate (Pertemuan 4).
-- Jalankan di MySQL/MariaDB (XAMPP). Aman dijalankan ulang: tabel dibuat hanya bila belum ada.

CREATE DATABASE IF NOT EXISTS ramenku_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ramenku_db;

-- Catatan: batasan CHECK sengaja tidak dipakai (MariaDB 10.4 XAMPP crash); nilai divalidasi di kode Java.

-- Login
CREATE TABLE IF NOT EXISTS pengguna (
  id        INT AUTO_INCREMENT PRIMARY KEY,
  username  VARCHAR(30) NOT NULL UNIQUE,
  sandi     CHAR(64)    NOT NULL,   -- SHA-256 heksadesimal
  nama      VARCHAR(60) NOT NULL
) ENGINE=InnoDB;

-- Fitur Stok Bahan
CREATE TABLE IF NOT EXISTS bahan (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  nama        VARCHAR(60)  NOT NULL UNIQUE,
  satuan      VARCHAR(15)  NOT NULL,
  stok        DECIMAL(12,2) NOT NULL DEFAULT 0,
  stok_minimal DECIMAL(12,2) NOT NULL DEFAULT 0
) ENGINE=InnoDB;

-- Fitur Menu dan Resep
CREATE TABLE IF NOT EXISTS menu (
  id     INT AUTO_INCREMENT PRIMARY KEY,
  nama   VARCHAR(60)  NOT NULL UNIQUE,
  harga  DECIMAL(12,2) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS resep (
  id        INT AUTO_INCREMENT PRIMARY KEY,
  menu_id   INT NOT NULL,
  bahan_id  INT NOT NULL,
  jumlah    DECIMAL(12,2) NOT NULL,
  UNIQUE (menu_id, bahan_id),
  FOREIGN KEY (menu_id)  REFERENCES menu(id)  ON DELETE CASCADE,
  FOREIGN KEY (bahan_id) REFERENCES bahan(id)
) ENGINE=InnoDB;

-- Fitur Penjualan
CREATE TABLE IF NOT EXISTS penjualan (
  id       INT AUTO_INCREMENT PRIMARY KEY,
  waktu    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total    DECIMAL(14,2) NOT NULL DEFAULT 0
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS penjualan_item (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  penjualan_id  INT NOT NULL,
  menu_id       INT NOT NULL,
  jumlah        INT NOT NULL,
  harga_satuan  DECIMAL(12,2) NOT NULL,
  FOREIGN KEY (penjualan_id) REFERENCES penjualan(id) ON DELETE CASCADE,
  FOREIGN KEY (menu_id)      REFERENCES menu(id)
) ENGINE=InnoDB;

-- Data contoh (hanya bila tabel masih kosong)
INSERT INTO bahan (nama, satuan, stok, stok_minimal)
SELECT * FROM (
  SELECT 'Mi Ramen' nama, 'porsi' satuan, 50 stok, 10 stok_minimal UNION ALL
  SELECT 'Kaldu Ayam', 'ml', 5000, 1000 UNION ALL
  SELECT 'Telur', 'butir', 40, 10 UNION ALL
  SELECT 'Chashu', 'gram', 2000, 500 UNION ALL
  SELECT 'Daun Bawang', 'gram', 300, 100
) x WHERE NOT EXISTS (SELECT 1 FROM bahan);

INSERT INTO menu (nama, harga)
SELECT * FROM (
  SELECT 'Ramen Shoyu' nama, 28000 harga UNION ALL
  SELECT 'Ramen Pedas', 30000 UNION ALL
  SELECT 'Telur Ajitama', 6000
) x WHERE NOT EXISTS (SELECT 1 FROM menu);

INSERT INTO resep (menu_id, bahan_id, jumlah)
SELECT m.id, b.id, r.jumlah FROM (
  SELECT 'Ramen Shoyu' mn, 'Mi Ramen' bn, 1 jumlah UNION ALL
  SELECT 'Ramen Shoyu', 'Kaldu Ayam', 300 UNION ALL
  SELECT 'Ramen Shoyu', 'Chashu', 60 UNION ALL
  SELECT 'Ramen Shoyu', 'Daun Bawang', 5 UNION ALL
  SELECT 'Ramen Pedas', 'Mi Ramen', 1 UNION ALL
  SELECT 'Ramen Pedas', 'Kaldu Ayam', 300 UNION ALL
  SELECT 'Ramen Pedas', 'Chashu', 60 UNION ALL
  SELECT 'Telur Ajitama', 'Telur', 1
) r JOIN menu m ON m.nama = r.mn JOIN bahan b ON b.nama = r.bn
WHERE NOT EXISTS (SELECT 1 FROM resep);

-- Akun contoh untuk login (ganti sandinya setelah dipakai)
INSERT INTO pengguna (username, sandi, nama)
SELECT 'admin', SHA2('admin123', 256), 'Administrator'
WHERE NOT EXISTS (SELECT 1 FROM pengguna);
