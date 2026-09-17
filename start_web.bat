@echo off
title Hospital Management System - Web Edition
echo ===================================================
echo Starting Hospital Management System - Web Edition
echo ===================================================
echo [1/2] Starting Node.js REST API Backend on port 5000...
start "Hospital Backend API" cmd /k "cd backend && npm start"
echo [2/2] Starting React Vite Frontend on port 5173...
start "Hospital Web Frontend" cmd /k "cd frontend && npm run dev"
echo.
echo Both servers started! Open your browser at:
echo http://localhost:5173
echo ===================================================
