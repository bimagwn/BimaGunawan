@echo off
rem Menguji desain laporan JasperReports RamenKu tanpa membuka Jaspersoft Studio.
rem Pemakaian:  ekspor.bat stok_bahan
rem             ekspor.bat struk NOTA_ID=1 "WAKTU=13 November 2026, 08:55" TOTAL=84000
rem Syarat: MySQL menyala, database ramenku_db sudah diimpor, dan proyek sudah dibangun (bangun.bat).
rem Hasil PDF, Word, dan Excel ada di folder "hasil" di samping berkas ini.
setlocal
set "AKAR=%~dp0..\.."
set "JAR=%AKAR%\target\ramenku-1.0.jar"
if not exist "%JAR%" (
    echo Belum dibangun. Jalankan bangun.bat di akar proyek dulu.
    exit /b 1
)
set "DESAIN=%~1"
if "%DESAIN%"=="" set "DESAIN=stok_bahan"
shift
java -cp "%JAR%" "%~dp0Ekspor.java" "%AKAR%\src\main\resources\laporan\%DESAIN%.jrxml" "%~dp0hasil" %1 %2 %3 %4 %5 %6 %7 %8
