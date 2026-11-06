@echo off
rem Menjalankan demo semua konsep OOP di konsol
cd /d "%~dp0"
if not exist "target\classes" (
    echo Belum dibangun. Jalankan bangun.bat dulu.
    pause
    exit /b 1
)
java -cp target\classes id.ramenku.oop.Main --demo
pause
