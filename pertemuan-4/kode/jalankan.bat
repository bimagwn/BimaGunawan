@echo off
rem Membuka RamenKu (nyalakan MySQL di XAMPP lebih dulu)
cd /d "%~dp0"
if not exist "target\ramenku-1.0.jar" (
    echo Belum dibangun. Jalankan bangun.bat dulu.
    pause
    exit /b 1
)
start "" javaw -jar target\ramenku-1.0.jar
