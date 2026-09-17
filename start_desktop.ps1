# start_desktop.ps1
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  Hospital Management System - Java Desktop GUI   " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

$javaCmd = "java"
$javacCmd = "javac"
if (Test-Path "jdk17\jdk-17.0.10+7\bin\java.exe") {
    $javaCmd = ".\jdk17\jdk-17.0.10+7\bin\java.exe"
    $javacCmd = ".\jdk17\jdk-17.0.10+7\bin\javac.exe"
}

if (-not (Test-Path "out\ui\LoginUI.class")) {
    Write-Host "[INFO] Binaries not found in 'out'. Compiling Java sources..." -ForegroundColor Yellow
    if (-not (Test-Path "out")) { New-Item -ItemType Directory -Path "out" | Out-Null }
    $sources = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }
    & $javacCmd -encoding UTF-8 -cp "lib/mysql-connector-j-26.7.0.jar" -d out $sources
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERROR] Compilation failed. Ensure JDK 17+ is installed." -ForegroundColor Red
        exit 1
    }
    Write-Host "[INFO] Compilation successful!" -ForegroundColor Green
}

Write-Host "[INFO] Launching Desktop Application..." -ForegroundColor Green
& $javaCmd -cp "out;lib\mysql-connector-j-26.7.0.jar;src" ui.LoginUI
