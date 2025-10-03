#!/usr/bin/env pwsh
# run-tests.ps1 — PowerShell test runner for Java projects
# Handles diffs with Compare-Object and uses native colors.

# ==============================
# Configuration
# ==============================
$JAR         = "po-uilib.jar"
$MAIN_CLASS  = "bci.app.App"
$SRC_DIR     = "src"
$BIN_DIR     = "bin"
$TEST_DIR    = "tests"
$SHOW_DIFFS  = ($args -contains "--show-diffs")

# Platform-aware classpath
$CPSEP       = [System.IO.Path]::PathSeparator
$CLASSPATH   = "$JAR$CPSEP$BIN_DIR"

# ==============================
# Output helpers
# ==============================
function Write-Info    ($msg) { Write-Host "→ $msg" -ForegroundColor Cyan   }
function Write-Success ($msg) { Write-Host "✓ $msg" -ForegroundColor Green  }
function Write-Warn    ($msg) { Write-Host "⚠ $msg" -ForegroundColor Yellow }
function Write-Err     ($msg) { Write-Host "✗ $msg" -ForegroundColor Red    }

# ==============================
# Diff helper (optimized)
# ==============================
function Get-DiffSnippet {
    param(
        [string]$expectedFile,
        [string]$actualFile,
        [int]   $maxLines = 15
    )

    if (-not (Test-Path $expectedFile)) { return "Expected file '$expectedFile' not found." }
    if (-not (Test-Path $actualFile))   { return "Actual file '$actualFile' not found." }

    $expected = Get-Content -LiteralPath $expectedFile
    $actual   = Get-Content -LiteralPath $actualFile

    $diff = Compare-Object -ReferenceObject $expected -DifferenceObject $actual -IncludeEqual:$false |
            Select-Object -First $maxLines

    if (-not $diff) { return "" }

    $sb = New-Object System.Text.StringBuilder
    foreach ($entry in $diff) {
        switch ($entry.SideIndicator) {
            '<=' { [void]$sb.AppendLine("- $($entry.InputObject)") }
            '=>' { [void]$sb.AppendLine("+ $($entry.InputObject)") }
        }
    }
    return $sb.ToString()
}

# ==============================
# Ensure bin directory
# ==============================
if (-not (Test-Path $BIN_DIR)) { New-Item -ItemType Directory -Path $BIN_DIR | Out-Null }

# ==============================
# Compile sources
# ==============================
Write-Info "Compiling Java sources..."
$javaFiles = Get-ChildItem -Path $SRC_DIR -Recurse -Filter *.java -File -ErrorAction SilentlyContinue |
             ForEach-Object { $_.FullName }

if (-not $javaFiles) {
    Write-Warn "No Java source files found in '$SRC_DIR'."
    exit 1
}

& javac -cp $CLASSPATH -d $BIN_DIR $javaFiles *> $null
if ($LASTEXITCODE -ne 0) {
    Write-Err "Compilation failed. Fix errors and rerun."
    exit 1
}

Write-Success "Compilation successful."
Write-Host ""

# ==============================
# Run tests
# ==============================
Write-Info "Running tests in '$TEST_DIR'..."
Write-Host ""

$total = 0
$passed = 0
$failedTests = @()

Get-ChildItem -Path $TEST_DIR -Filter '*.in' -File -ErrorAction SilentlyContinue | ForEach-Object {
    $baseName     = [System.IO.Path]::GetFileNameWithoutExtension($_.Name)
    $inputFile    = $_.FullName
    $importFile   = Join-Path $TEST_DIR "$baseName.import"
    $expectedFile = Join-Path $TEST_DIR "$baseName.out"
    $outputFile   = Join-Path $TEST_DIR "$baseName.outhyp"
    $diffFile     = Join-Path $TEST_DIR "$baseName.diff"

    # Run program
    $javaArgs = @("-cp", $CLASSPATH, "-Din=$inputFile", "-DwriteInput=true", "-Dout=$outputFile")
    if (Test-Path $importFile) { $javaArgs += "-Dimport=$importFile" }
    & java @javaArgs $MAIN_CLASS *> $null

    $total++

    if (-not (Test-Path $outputFile)) {
        Write-Warn "${baseName}: No output produced."
        $failedTests += "$baseName (no output)"
        return
    }

    $diffText = Get-DiffSnippet $expectedFile $outputFile -maxLines 30
    if ([string]::IsNullOrEmpty($diffText)) {
        Write-Success $baseName
        $passed++
        Remove-Item $diffFile, $outputFile -ErrorAction SilentlyContinue
    } else {
        Write-Err $baseName
        $failedTests += $baseName

        if ($SHOW_DIFFS) {
            Write-Host "`n--- Diff snippet ---" -ForegroundColor Yellow
            $diffText -split "`n" | Select-Object -First 15 | ForEach-Object {
                switch -Regex ($_) {
                    '^\+ ' { Write-Host $_ -ForegroundColor Green }
                    '^\- ' { Write-Host $_ -ForegroundColor Red }
                    default { Write-Host "    $_" }
                }
            }
            Write-Host "--------------------`n" -ForegroundColor Yellow
        }

        try { $diffText | Out-File -LiteralPath $diffFile -Encoding utf8 } catch { }
    }
}

Remove-Item saved* -ErrorAction SilentlyContinue

# ==============================
# Summary
# ==============================
Write-Host ""
Write-Host "==============================" -ForegroundColor White
Write-Host "📊 TEST SUMMARY" -ForegroundColor White
Write-Host ("Total tests : {0}" -f $total)
Write-Host ("Passed      : {0}" -f $passed) -ForegroundColor Green
Write-Host ("Failed      : {0}" -f ($total - $passed)) -ForegroundColor Red
$pct = if ($total) { [math]::Floor(100 * $passed / $total) } else { 0 }
Write-Host ("Success     : {0}%" -f $pct) -ForegroundColor White
Write-Host "=============================="
