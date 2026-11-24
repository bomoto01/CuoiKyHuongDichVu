@echo off
chcp 65001 >nul
echo =====================================================================
echo    DANG BUILD TAT CA CAC MODULE MICROSERVICES (MAVEN)
echo =====================================================================
cd /d "%~dp0\backend"
call mvn clean package -DskipTests
echo.
echo =====================================================================
echo Build hoan tat! Ban co the chay run-microservices.bat de khoi dong.
echo =====================================================================
pause
