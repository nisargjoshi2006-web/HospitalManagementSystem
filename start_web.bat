@echo off
setlocal
title Hospital Management System - Web Edition

echo ===================================================
echo   Hospital Management System - Web Edition
echo ===================================================

REM Check backend dependencies
if not exist "backend\node_modules" (
    echo [INFO] Installing backend dependencies...
    cd backend && call npm install && cd ..
)

REM Check frontend dependencies
if not exist "frontend\node_modules" (
    echo [INFO] Installing frontend dependencies...
    cd frontend && call npm install && cd ..
)

echo [1/2] Starting Node.js REST API Backend on port 5000...
start "Hospital Backend API" cmd /k "cd backend && npm start"

echo [2/2] Starting React Vite Frontend on port 5173...
start "Hospital Web Frontend" cmd /k "cd frontend && npm run dev"

echo.
echo Both servers started! Open your browser at:
echo http://localhost:5173
echo ===================================================

endlocal
