@echo off
setlocal
title Hospital Management System - Java Desktop GUI

echo ===================================================
echo   CarePoint Hospital Management System - Java Desktop
echo ===================================================

REM Detect Java executable
set "JAVA_CMD=java"
set "JAVAC_CMD=javac"
if exist "jdk17\jdk-17.0.10+7\bin\java.exe" (
    set "JAVA_CMD=jdk17\jdk-17.0.10+7\bin\java.exe"
    set "JAVAC_CMD=jdk17\jdk-17.0.10+7\bin\javac.exe"
)

REM Always compile Java sources to ensure latest UI updates, RBAC, and Logout features are live
echo [INFO] Compiling latest Java sources with systematic UITheme...
if not exist "out" mkdir "out"
"%JAVAC_CMD%" -encoding UTF-8 -cp "lib\mysql-connector-j-26.7.0.jar;src" -d out src\db\*.java src\model\*.java src\dao\*.java src\ui\*.java src\test\*.java
if errorlevel 1 (
    echo [ERROR] Compilation failed. Please verify JDK 17+ is installed.
    pause
    exit /b 1
)
echo [INFO] Compilation successful!

echo [INFO] Launching Desktop Application with antialiased typography...
"%JAVA_CMD%" -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -cp "out;lib\mysql-connector-j-26.7.0.jar;src" ui.LoginUI

endlocal
