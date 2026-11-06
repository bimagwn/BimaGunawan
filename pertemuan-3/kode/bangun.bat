@echo off
rem Membangun proyek menjadi satu berkas jar (target\ramenku-1.0.jar)
cd /d "%~dp0"
call "C:\Program Files\NetBeans-17\netbeans\java\maven\bin\mvn.cmd" -DskipTests package
pause
