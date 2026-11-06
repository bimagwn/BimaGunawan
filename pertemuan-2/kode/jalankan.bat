@echo off
rem Membuka jendela RamenKu OOP
cd /d "%~dp0"
if not exist "target\classes" (
    echo Belum dibangun. Jalankan bangun.bat dulu.
    pause
    exit /b 1
)
start "" javaw -cp target\classes id.ramenku.oop.Main
