@echo off
rem Membangun proyek (butuh internet saat pertama kali untuk mengunduh pustaka tes)
cd /d "%~dp0"
call "C:\Program Files\NetBeans-17\netbeans\java\maven\bin\mvn.cmd" -DskipTests package
pause
