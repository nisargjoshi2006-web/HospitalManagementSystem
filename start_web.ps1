# start_web.ps1
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "     Hospital Management System - Web Edition      " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

if (-not (Test-Path "backend\node_modules")) {
    Write-Host "[INFO] Installing backend dependencies..." -ForegroundColor Yellow
    Push-Location backend; npm install; Pop-Location
}

if (-not (Test-Path "frontend\node_modules")) {
    Write-Host "[INFO] Installing frontend dependencies..." -ForegroundColor Yellow
    Push-Location frontend; npm install; Pop-Location
}

Write-Host "[1/2] Starting Node.js REST API Backend on port 5000..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd backend; npm start"

Write-Host "[2/2] Starting React Vite Frontend on port 5173..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd frontend; npm run dev"

Write-Host "`nBoth servers started! Open your browser at http://localhost:5173" -ForegroundColor Cyan
