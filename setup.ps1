param(
    [string]$HostsEntry = "127.0.0.1 keycloak",
    [string]$HostsPath = "$env:SystemRoot\System32\drivers\etc\hosts"
)

[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

if (-NOT ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator")) {
    Write-Host "Error: This script must be run as Administrator to modify the hosts file." -ForegroundColor Red
    Write-Host "Please restart PowerShell as Administrator and try again." -ForegroundColor Yellow
    exit 1
}


if (-NOT (Get-Command docker-compose -ErrorAction SilentlyContinue)) {
    Write-Host "Error: docker-compose not found. Make sure Docker is installed and available in PATH." -ForegroundColor Red
    exit 1
}


if (-NOT (Test-Path $HostsPath)) {
    Write-Host "Hosts file not found at $HostsPath. Creating a new one..." -ForegroundColor Yellow
    New-Item -Path $HostsPath -ItemType File -Force | Out-Null
}


$hostsContent = Get-Content $HostsPath -Raw -ErrorAction SilentlyContinue


if ($hostsContent -match [regex]::Escape($HostsEntry)) {
    Write-Host "Entry '$HostsEntry' already present in hosts." -ForegroundColor Green
} else {
    Write-Host "Adding entry '$HostsEntry' to hosts..." -ForegroundColor Yellow
    Add-Content -Path $HostsPath -Value "`n$HostsEntry" -Force
    if ($?) {
        Write-Host "Entry added successfully." -ForegroundColor Green
    } else {
        Write-Host "Failed to add entry. Check file permissions." -ForegroundColor Red
        exit 1
    }
}

Write-Host "Starting docker-compose up -d..." -ForegroundColor Cyan
docker-compose up -d

if ($?) {
    Write-Host "Containers started successfully." -ForegroundColor Green
} else {
    Write-Host "Error starting containers. Check logs." -ForegroundColor Red
    exit 1
}